package com.collab.project.email;

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

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class EmailService {

    String clientId = "785164760314-0vlabkd13fvcd2rhfo4arp4b0ra85im1.apps.googleusercontent.com";
    String clientSecret = "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";
    String refreshToken = "1//04VlbpVkbGhaoCgYIARAAGAQSNwF-L9Ir3c5zWKgeH63ILM88Nr2LgH8bYZuyRm85REnV4yBLKrXh-6c1I39ig58ehxEZ7NcXSfc";

    private static final String APPLICATION_NAME = "Wondor";

    NetHttpTransport httpTransport;
    static GsonFactory jsonFactory;
    Gmail gmailService;
    MemoryDataStoreFactory memoryDataStoreFactory;

    public EmailService() throws GeneralSecurityException, IOException {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        jsonFactory = GsonFactory.getDefaultInstance();
        memoryDataStoreFactory = MemoryDataStoreFactory.getDefaultInstance();
        gmailService = new Gmail.Builder(httpTransport, jsonFactory, getCredentials())
                .setApplicationName(APPLICATION_NAME)
                .build();

    }

    private Credential getCredentials()
            throws IOException {


        List<String> scopes = Arrays.asList("https://mail.google.com/");
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                // Sends requests to the OAuth server
                new NetHttpTransport(),
                // Converts between JSON and Java
                jsonFactory,
                // Your OAuth client ID
                clientId,
                // Your OAuth client secret
                clientSecret,
                // Tells the user what permissions they're giving you
                scopes)
                // Stores the user's credential in memory
                .setDataStoreFactory(memoryDataStoreFactory).build();

        Credential credential;
        try {
            credential = flow.loadCredential(clientId);
        } catch (IOException e) {
            // Error getting login status
            credential = null;
        }

        if (credential == null) {
            TokenResponse tokenResponse = new TokenResponse().setRefreshToken(refreshToken);
            credential = flow.createAndStoreCredential(tokenResponse, clientId);
            credential.refreshToken();
        }
        return credential;
    }

    public MimeMessage createEmail(String toEmailAddress, String subject, String filename) throws MessagingException,
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

    public Message sendEmail(String subject,
                                    String toEmailAddress,
                                    String filename) throws MessagingException, IOException, GeneralSecurityException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = createEmail(toEmailAddress, subject, filename);

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
            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
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

//    public static void main(String[] args) throws MessagingException, GeneralSecurityException, IOException, URISyntaxException {
//        EmailService emailService = new EmailService();
//        emailService.sendEmail("Welcome to Wondor", "prashant.joshi056@gmail.com",
//                Paths.get(EmailService.class.getResource("/new_user.html").toURI()).toFile());;
//    }
}
