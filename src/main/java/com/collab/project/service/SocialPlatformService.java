package com.collab.project.service;

import com.collab.project.model.inputs.SocialPlatformInput;
import com.collab.project.model.socialprospectus.SocialPlatform;

public interface SocialPlatformService {

    public SocialPlatform createSocialPlatform(SocialPlatformInput socialPlatformInput);

    public void updateSocialPlatform(SocialPlatform socialPlatform);

    public void delete(SocialPlatformInput socialPlatformInput);

    public SocialPlatform getSocialPlatformById(Long socialPlatformId);

    public SocialPlatform getSocialPlatformByName(String name);
}