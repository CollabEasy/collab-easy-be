package com.collab.project.model.collab;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "collab_requests")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CollabRequest {

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;

    @NonNull
    @Column(name = "sender_id", nullable = false, unique = true)
    private String senderId;
    @NonNull
    @Column(name = "receiver_id", nullable = false, unique = true)
    private String receiverId;

    @Column(name = "scheduled_at")
    private Timestamp collabDate;

    @Lob
    @Convert(converter = RequestDataConvertor.class)
    private RequestData requestData;

    private String status;

    @Column(name = "created_At")
    private LocalDateTime createdAt;

    @Column(name = "updated_At")
    private LocalDateTime updatedAt;


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
                if (null == customObject || "null" == customObject) {
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