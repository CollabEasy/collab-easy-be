package com.collab.project.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.gmail.Gmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class EmailConfig {

    @Value("${gmail.client.id}")
    String clientId = "785164760314-0vlabkd13fvcd2rhfo4arp4b0ra85im1.apps.googleusercontent.com";

    @Value("${gmail.client.secret}")
    String clientSecret = "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";

    @Value("${gmail.client.refreshToken}")
    String refreshToken = "1//04VlbpVkbGhaoCgYIARAAGAQSNwF-L9Ir3c5zWKgeH63ILM88Nr2LgH8bYZuyRm85REnV4yBLKrXh-6c1I39ig58ehxEZ7NcXSfc";

    private static final String APPLICATION_NAME = "Wondor";

    @Bean
    public Gmail gmailService(@Autowired ConfigurationBeans configurationBeans) throws GeneralSecurityException,
            IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Gmail.Builder(httpTransport, configurationBeans.gsonFactory(), getCredentials(configurationBeans))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private Credential getCredentials(ConfigurationBeans configurationBeans)
            throws IOException {

        List<String> scopes = Arrays.asList("https://mail.google.com/");
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                // Sends requests to the OAuth server
                new NetHttpTransport(),
                // Converts between JSON and Java
                configurationBeans.gsonFactory(),
                // Your OAuth client ID
                clientId,
                // Your OAuth client secret
                clientSecret,
                // Tells the user what permissions they're giving you
                scopes)
                // Stores the user's credential in memory
                .setDataStoreFactory(configurationBeans.memoryDataStoreFactory()).build();

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
}
