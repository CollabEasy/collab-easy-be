package com.collab.project.email;

import com.collab.project.model.artist.Artist;
import com.collab.project.model.email.EmailEnumHistory;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.EmailEnumHistoryRepository;
import com.collab.project.service.impl.ArtistGroupServiceImpl;
import com.collab.project.service.impl.ScriptServiceImpl;
import com.collab.project.util.EmailUtils;
import com.collab.project.util.FileUtils;
import com.collab.project.util.emailTemplates.CompleteProfileEmail;
import com.collab.project.util.emailTemplates.GenericEmailTemplate;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.gmail.Gmail;
import lombok.SneakyThrows;
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

    final EmailEnumHistoryRepository emailEnumHistoryRepository;

    @Autowired
    ScriptServiceImpl scriptService;

    String sender = "Wondor <noreply@wondor.art>";

    public EmailService(@Autowired Gmail gmailService, @Autowired EmailUtils emailUtils,
                        @Autowired ArtistRepository artistRepository,
                        @Autowired ArtistGroupServiceImpl artistGroupService,
                        @Autowired EmailEnumHistoryRepository emailEnumHistoryRepository) {
        this.gmailService = gmailService;
        this.emailUtils = emailUtils;
        this.emailEnumHistoryRepository = emailEnumHistoryRepository;
    }


    public MimeMessage createEmailFromEncodedMessage(String toEmailAddress, String subject, String message, Boolean isEncoded) throws MessagingException, IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(sender));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(toEmailAddress));
        email.setSubject(subject);
        email.setSender(new InternetAddress(sender));
        if (isEncoded) {
            email.setContent(emailUtils.decryptEmailContent(message), "text/html; charset=utf-8");
        } else {
            email.setContent(message, "text/html; charset=utf-8");
        }
        return email;
    }

    @Async
    public void sendEmailToAllUsersFromString(List<Artist> artists, String subject, String encodedMessage) {
        new Thread(() -> {
            artists.stream().parallel().forEach(artist -> {
                try {
                    sendEmailToArtist(artist, subject, encodedMessage);
                    Thread.sleep(5000);
                } catch (Exception ex) {
                    System.out.println("Unable to send email : " + ex.getMessage());
                }
            });
        }).start();

    }

    @SneakyThrows
    public void sendEmailToArtist(
            Artist artist,
            String subject,
            String encodedMessage
    ) {
        String message = emailUtils.decryptEmailContent(encodedMessage);
        String content = GenericEmailTemplate.getContent(artist.getFirstName(), message);
        sendEmailFromStringFinal(subject, artist.getEmail(), content, false);
    }

    @Async
    public void sendEmailFromStringFinal(String subject,
                                     String emailAddress,
                                     String encodedMessage,
                                       Boolean isEncoded) throws MessagingException, IOException,
            GeneralSecurityException {
        if (emailAddress == null) {
            return;
        }
        MimeMessage email = createEmailFromEncodedMessage(emailAddress, subject, encodedMessage, isEncoded);
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
           gmailService.users().messages().send("me", message).execute();

        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
        return;
    }

    @SneakyThrows
    public void sendEmailFromStringToList(List<Artist> artists, String subject, String content) throws MessagingException,
            GeneralSecurityException, IOException {
        new Thread(() -> {
            for (Artist artist : artists) {
                try {
                    sendEmailToArtist(artist, subject, content);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Async
    @SneakyThrows
    public void sendEmailToGroup(String groupEnum, String subject, String content) {
        if (groupEnum.equals("INCOMPLETE_PROFILE")) {
            scriptService.emailIncompleteProfileUsers(false);
        } else {
            return;
        }

        Optional<EmailEnumHistory> history = emailEnumHistoryRepository.findByEmailEnum(groupEnum);
        EmailEnumHistory enumHistory = history.orElseGet(() -> new EmailEnumHistory(groupEnum,
                Timestamp.from(Instant.now())));

        enumHistory.setLastSent(Timestamp.from(Instant.now()));
        emailEnumHistoryRepository.save(enumHistory);
    }
}
