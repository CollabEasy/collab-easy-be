package com.collab.project.model.collab;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "collab_requests")
@Getter
@Setter
public class CollabRequest {
    @Id
    @NonNull
    @Column(nullable = false, unique = true)
    private String requestId;
    @NonNull
    @Column(nullable = false, unique = true)
    private String senderId;
    @NonNull
    @Column(nullable = false, unique = true)
    private String recevierId;

    @Lob
    @Convert(converter = RequestDataConvertor.class)
    private RequestData requestData;

    private Timestamp scheduledAt;

    private Boolean accepted;

    private Timestamp createdAt;

    private Timestamp updatedAt;


    @Slf4j
    static
    class RequestDataConvertor implements AttributeConverter<RequestData, String> {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String convertToDatabaseColumn(RequestData customObject) {
            String jsonObject = null;
            try {
                jsonObject = objectMapper.writeValueAsString(customObject);
            } catch (final JsonProcessingException e) {
                log.error("JSON writing error", e);
            }
            return jsonObject;
        }

        @Override
        public RequestData convertToEntityAttribute(String customObject) {
            RequestData jsonObject = null;
            try {
                if (null == customObject) {
                    return null;
                }
                jsonObject = objectMapper.readValue(customObject, RequestData.class);
            } catch (final Exception e) {
                log.error("JSON reading error", e);
            }
            return jsonObject;
        }

    }
}