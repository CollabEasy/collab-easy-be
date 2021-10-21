package com.collab.project.model.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class NotificationReadInput {
    @JsonProperty("notification_ids")
    List<String> notificationIds;
}
