package com.collab.project.service.impl;

import com.collab.project.model.art.ArtCategory;
import com.collab.project.model.inputs.SocialPlatformInput;
import com.collab.project.model.socialprospectus.SocialPlatform;
import com.collab.project.repositories.SocialPlatformRepository;
import com.collab.project.service.SocialPlatformService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

public class SocialPlatformServiceImpl implements SocialPlatformService {
    @Autowired
    private SocialPlatformRepository socialPlatformRepository;

    public SocialPlatform addSocialPlatform(SocialPlatformInput socialPlatformInput) {

        SocialPlatform platform = socialPlatformRepository.findByPlatformId(socialPlatformInput.getId());
        if (platform == null) {
            platform = new SocialPlatform(FALLBACK_ID, socialPlatformInput.getName(),
                    socialPlatformInput.getBase_url(), socialPlatformInput.getDescription(), socialPlatformInput.getApproved());
        } else {
            platform.setName(socialPlatformInput.getName());
            platform.setBase_url(socialPlatformInput.getBase_url());
            platform.setDescription(socialPlatformInput.getDescription());
            platform.setApproved(socialPlatformInput.getApproved());
        }
        socialPlatformRepository.save(platform);
        return platform;
    }

    public SocialPlatform updateSocialPlatform(SocialPlatform socialPlatform) {
        SocialPlatform platform = socialPlatformRepository.findByPlatformId(socialPlatform.getId());
        if (platform == null) {
            platform = new SocialPlatform(FALLBACK_ID, socialPlatform.getName(), socialPlatform.getBase_url(),
                    socialPlatform.getDescription(), socialPlatform.getApproved());
        } else {
            platform.setName(socialPlatform.getName());
            platform.setBase_url(socialPlatform.getBase_url());
            platform.setDescription(socialPlatform.getDescription());
            platform.setApproved(socialPlatform.getApproved());
        }
        socialPlatformRepository.save(platform);
        return platform;
    }

    public void delete(String name) {
        SocialPlatform platform = socialPlatformRepository.findByPlatformName(name);
        if (platform != null) {
            socialPlatformRepository.delete(platform);
        }
    }

    public SocialPlatform getSocialPlatformById(Long socialPlatformId) {
        return socialPlatformRepository.findByPlatformId(socialPlatformId);
    }

    public SocialPlatform getSocialPlatformByName(String name) {
        return socialPlatformRepository.findByPlatformName(name);
    }

    public List<SocialPlatform> getDefaultSocialPlatform() {
        List<SocialPlatform> platforms = socialPlatformRepository.findAll();
        return platforms;
    }
}
