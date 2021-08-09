package com.collab.project.service;

import com.collab.project.model.user.UserPreferences;

public interface UserPreferencesService {

    public void updateUserPreferences(UserPreferences userPreferences) throws Exception;

    public UserPreferences getUserPreferences(String userId);
}
