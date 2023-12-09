package com.collab.project.util;

import jdk.internal.joptsimple.internal.Strings;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

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

    public static String getSortDate(String s) {
        String[] split = s.split(" ");
        switch (split[1].toLowerCase()) {
            case "jan":
                split[1] = "01";
                break;
            case "feb":
                split[1] = "02";
                break;
            case "mar":
                split[1] = "03";
                break;
            case "apr":
                split[1] = "04";
                break;
            case "may":
                split[1] = "05";
                break;
            case "jun":
                split[1] = "06";
                break;
            case "jul":
                split[1] = "07";
                break;
            case "aug":
                split[1] = "08";
                break;
            case "sep":
                split[1] = "09";
                break;
            case "oct":
                split[1] = "10";
                break;
            case "nov":
                split[1] = "11";
                break;
            case "dec":
                split[1] = "12";
                break;
        }
        return split[2] + split[1] + split[0];
    }
}
