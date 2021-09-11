package com.collab.project.model.collab;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestData {

    @JsonProperty("message")
    private String message;
    @JsonProperty("collabTheme")
    private String collabTheme;

    public RequestData(String message, String collabTheme) {
        this.message = message;
        this.collabTheme = collabTheme;
    }

    public RequestData()  {
    }
}
