package com.collab.project.email;

import com.collab.project.model.artist.Artist;
import com.collab.project.model.email.EmailEnumHistory;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.EmailEnumHistoryRepository;
import com.collab.project.service.impl.ArtistGroupServiceImpl;
import com.collab.project.util.EmailUtils;
import com.collab.project.util.FileUtils;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.gmail.Gmail;
import org.apache.commons.codec.binary.Base64;

import com.google.api.services.gmail.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Component
public class EmailService {


    final Gmail gmailService;

    final EmailUtils emailUtils;

    final ArtistRepository artistRepository;

    final ArtistGroupServiceImpl artistGroupService;

    final EmailEnumHistoryRepository emailEnumHistoryRepository;

    String sender = "Wondor <noreply@wondor.art>";

    public EmailService(@Autowired Gmail gmailService, @Autowired EmailUtils emailUtils,
                        @Autowired ArtistRepository artistRepository,
                        @Autowired ArtistGroupServiceImpl artistGroupService,
                        @Autowired EmailEnumHistoryRepository emailEnumHistoryRepository) {
        this.gmailService = gmailService;
        this.emailUtils = emailUtils;
        this.artistRepository = artistRepository;
        this.artistGroupService = artistGroupService;
        this.emailEnumHistoryRepository = emailEnumHistoryRepository;
    }

    public MimeMessage createEmailFromFile(String toEmailAddress, String subject, String filename) throws MessagingException,
            IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(sender));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(toEmailAddress));
        email.setSubject(subject);
        email.setSender(new InternetAddress(sender));
        email.setContent(FileUtils.getHTMLContentFromFile(filename), "text/html; charset=utf-8");
        return email;
    }

    public MimeMessage createEmailFromEncodedMessage(String toEmailAddress, String subject, String message) throws MessagingException, IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(sender));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(toEmailAddress));
        email.setSubject(subject);
        email.setSender(new InternetAddress(sender));
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

    @Async
    public void sendEmailToGroup(String groupEnum, String subject, String content) {
        List<String> emails;
        switch (groupEnum) {
            case "ADMINS":
                emails = Arrays.asList("prashant.joshi056@gmail.com", "rahulgupta6007@gmail.com");
                break;
            case "INCOMPLETE_PROFILE":
                emails = artistGroupService.getEmailListWithIncompleteProfile();
                break;
            default:
                return;
        }

        emails.stream().parallel().forEach(email -> {
            try {
                sendEmailFromString(subject, null, email, content);
            } catch (Exception ex) {
                System.out.println("Unable to send email : " + ex.getMessage());
            }
        });

        Optional<EmailEnumHistory> history = emailEnumHistoryRepository.findByEmailEnum(groupEnum);
        EmailEnumHistory enumHistory = history.orElseGet(() -> new EmailEnumHistory(groupEnum,
                Timestamp.from(Instant.now())));

        enumHistory.setLastSent(Timestamp.from(Instant.now()));
        emailEnumHistoryRepository.save(enumHistory);
    }
}
