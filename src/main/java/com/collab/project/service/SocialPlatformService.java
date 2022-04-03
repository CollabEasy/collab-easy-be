package com.collab.project.service;

import com.collab.project.model.inputs.SocialPlatformInput;
import com.collab.project.model.socialprospectus.SocialPlatform;

import java.util.List;

public interface SocialPlatformService {

    public SocialPlatform addSocialPlatform(SocialPlatformInput socialPlatformInput);

    public SocialPlatform updateSocialPlatform(SocialPlatform socialPlatform);

    public void delete(String name);

    public SocialPlatform getSocialPlatformByName(String name);

    public List<SocialPlatform> getDefaultSocialPlatform();
}