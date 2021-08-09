package com.collab.project.service;

import com.collab.project.model.user.UserPreference;

public interface UserPreferencesService {

    public void updateUserPreferences(UserPreference userPreferences) throws Exception;

    public UserPreference getUserPreferences(String userId);
}
