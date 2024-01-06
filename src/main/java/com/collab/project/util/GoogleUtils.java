package com.collab.project.util;

import com.collab.project.model.inputs.ArtistInput;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GoogleUtils {


    @Autowired
    RequestInstanceImpl requestInstance;

    @Value("${google.client.id:896694875793-05dp2a045lu38ki42dbb1t010rmr5hr9.apps.googleusercontent.com}")
    private String clientId;


    public boolean isValid(ArtistInput artistInput) {
        try {

            HttpEntity<?> requestEntity = new HttpEntity<Object>(null);
            String url = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + artistInput
                .getIdToken();
            ResponseEntity<String> responseEntity = requestInstance
                .makeRequest(url, HttpMethod.GET, requestEntity, String.class);

            JSONObject resObject = new JSONObject(responseEntity.getBody());

//            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
//                new NetHttpTransport(),
//                new GsonFactory())
//                // Specify the CLIENT_ID of the app that accesses the backend:
//                .setAudience(Collections.singletonList(
//                    "265324139647-lv38dkdpnqq06e66mkt9a6ab6i4r1cik.apps.googleusercontent.com"))
//                // Or, if multiple clients access the backend:
//                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
//                .build();
            if (clientId.equals(resObject.optString("aud"))) {
                artistInput.setValid(true);
                // Get profile information from payload
                artistInput.setEmail(resObject.getString("email"));
                artistInput.setFirstName(resObject.getString("given_name"));
                artistInput.setLastName(resObject.has("family_name") ? resObject.getString("family_name") : "");
                artistInput.setProfilePicUrl(resObject.getString("picture"));
                artistInput.setEmail(resObject.getString("email"));
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            artistInput.setValid(false);
            log.error("Exception", e);
            return false;
        }
    }
}
