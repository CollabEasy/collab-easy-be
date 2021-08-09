package com.collab.project.model.user;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

@Entity
@Table(name = "user-preferences")
@Getter
@Setter
public class UserPreference {
    String userId;

    String settingName;

    Map<String, Boolean> settingValues;
}
