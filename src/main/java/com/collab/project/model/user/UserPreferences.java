package com.collab.project.model.user;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UserPreferences {
    String userId;

    String settingName;

    Map<String, Boolean> settingValues;
}
