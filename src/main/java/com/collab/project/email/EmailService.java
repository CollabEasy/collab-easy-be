package com.collab.project.email;

import com.collab.project.config.EmailConfig;
import com.collab.project.model.artist.Artist;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.util.EmailUtils;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import org.apache.commons.codec.binary.Base64;

import com.google.api.services.gmail.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Component
public class EmailService {


    final Gmail gmailService;

    final EmailUtils emailUtils;

    final ArtistRepository artistRepository;

    public EmailService(@Autowired Gmail gmailService, @Autowired EmailUtils emailUtils,
                        @Autowired ArtistRepository artistRepository) {
        this.gmailService = gmailService;
        this.emailUtils = emailUtils;
        this.artistRepository = artistRepository;
    }

    public MimeMessage createEmailFromFile(String toEmailAddress, String subject, String filename) throws MessagingException,
            IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("noreply@wondor.art"));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(toEmailAddress));
        email.setSubject(subject);
        email.setSender(new InternetAddress("noreply@wondor.art"));
        email.setContent(getHTMLContentFromFile(filename), "text/html; charset=utf-8");
        return email;
    }

    public MimeMessage createEmailFromEncodedMessage(String toEmailAddress, String subject, String message) throws MessagingException, IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("noreply@wondor.art"));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(toEmailAddress));
        email.setSubject(subject);
        email.setSender(new InternetAddress("noreply@wondor.art"));
        email.setContent(emailUtils.decryptEmailContent(message), "text/html; charset=utf-8");
        return email;
    }

    @Async
    public Message sendEmailFromFile(String subject,
                                    String toEmailAddress,
                                    String filename) throws MessagingException, IOException, GeneralSecurityException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = createEmailFromFile(toEmailAddress, subject, filename);

        // Encode and wrap the MIME message into a gmail message
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message message = new Message();
        message.setRaw(encodedEmail);

        try {
            // Create send message
            message = gmailService.users().messages().send("me", message).execute();
            return message;
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
        return null;
    }

    @Async
    public void sendEmailToAllUsersFromString(String subject, String encodedMessage) {
        List<Artist> artists = artistRepository.findAll();
        artists.stream().parallel().forEach(artist -> {
            try {
                sendEmailFromString(subject, artist.getArtistId(), artist.getEmail(), encodedMessage);
            } catch (Exception ex) {
                System.out.println("Unable to send email : " + ex.getMessage());
            }
        });
    }

    @Async
    public Message sendEmailFromString(String subject,
                                     String artistId,
                                     String emailAddress,
                                     String encodedMessage) throws MessagingException, IOException,
            GeneralSecurityException {
        String artistEmail = emailAddress;
        if (artistEmail == null) {
            Artist artist = artistRepository.findByArtistId(artistId);
            artistEmail = artist.getEmail();
        }

        MimeMessage email = createEmailFromEncodedMessage(artistEmail, subject, encodedMessage);
        if (email == null) {
            throw new IllegalStateException("Email provided is null.");
        }
        // Encode and wrap the MIME message into a gmail message
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message message = new Message();
        message.setRaw(encodedEmail);

        try {
            // Create send message
            message = gmailService.users().messages().send("me", message).execute();
            return message;
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
        return null;
    }

    private String getHTMLContentFromFile(String filename) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (InputStream in = getClass().getResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            // Use resource
            String str;
            while ((str = reader.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();

            return contentBuilder.toString();
        }
    }
}
