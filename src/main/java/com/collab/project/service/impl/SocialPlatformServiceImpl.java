package com.collab.project.service.impl;

import com.collab.project.model.inputs.SocialPlatformInput;
import com.collab.project.model.socialprospectus.SocialPlatform;
import com.collab.project.repositories.SocialPlatformRepository;
import com.collab.project.service.SocialPlatformService;
import org.springframework.beans.factory.annotation.Autowired;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

public class SocialPlatformServiceImpl implements SocialPlatformService {
    @Autowired
    private SocialPlatformRepository socialPlatformRepository;

    public SocialPlatform createSocialPlatform(SocialPlatformInput socialPlatformInput) {

        SocialPlatform platform = new SocialPlatform(socialPlatformInput);
        socialPlatformRepository.save(platform);
        return platform;
    }

    public void updateSocialPlatform(SocialPlatform socialPlatform) {
        socialPlatform.setId(FALLBACK_ID);
        socialPlatformRepository.save(socialPlatform);
    }

    public void delete(SocialPlatformInput socialPlatformInput) {
    }

    public SocialPlatform getSocialPlatformById(Long socialPlatformId) {
        return socialPlatformRepository.findByPlatformId(socialPlatformId);
    }

    public SocialPlatform getSocialPlatformByName(String name) {
        return socialPlatformRepository.findByPlatformName(name);
    }
}
