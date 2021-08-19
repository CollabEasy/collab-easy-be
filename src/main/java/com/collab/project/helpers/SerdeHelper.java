package com.collab.project.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SerdeHelper {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static Object getObjectFromString(String value) throws IOException {
        return MAPPER.readValue(value, Object.class);
    }

    public static String getJsonStringFromObject(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }
}
