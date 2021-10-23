package com.collab.project.util;

import com.collab.project.model.artist.Artist;
import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.repositories.ArtistCategoryRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Utils {


    @Autowired
    ArtistCategoryRepository artistCategoryRepository;



    public static String getSHA256(String originalString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (byte hash : encodedHash) {
            String hex = Integer.toHexString(0xff & hash);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public Boolean isNewUser(Artist artist) {
        List<ArtistCategory> artistCategoryList = artistCategoryRepository
            .findByArtistId(artist.getArtistId());
        return artistCategoryList.stream().filter(k -> k.getArtId() > 1).count() == 0;
    }

}
