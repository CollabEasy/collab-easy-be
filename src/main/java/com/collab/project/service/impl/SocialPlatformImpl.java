package com.collab.project.service.impl;

import com.collab.project.model.inputs.SocialPlatformInput;
import com.collab.project.model.socialprospectus.SocialPlatform;
import com.collab.project.repositories.SocialPlatformRepository;
import com.collab.project.service.SocialPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

@Service
public class SocialPlatformImpl implements SocialPlatformService {

    @Autowired
    private SocialPlatformRepository socialPlatformRepository;

    @Override
    public SocialPlatform addSocialPlatform(SocialPlatformInput socialPlatformInput) {
        SocialPlatform platform = socialPlatformRepository.findByName(socialPlatformInput.getName());
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

    @Override
    public SocialPlatform updateSocialPlatform(SocialPlatform socialPlatform) {
        Optional<SocialPlatform> platform = socialPlatformRepository.findById(socialPlatform.getId());
        SocialPlatform new_platform;
        if (!platform.isPresent()) {
            new_platform = new SocialPlatform(FALLBACK_ID, socialPlatform.getName(), socialPlatform.getBase_url(),
                    socialPlatform.getDescription(), socialPlatform.getApproved());
        } else {
            new_platform = platform.get();
            new_platform.setName(socialPlatform.getName());
            new_platform.setBase_url(socialPlatform.getBase_url());
            new_platform.setDescription(socialPlatform.getDescription());
            new_platform.setApproved(socialPlatform.getApproved());
        }
        socialPlatformRepository.save(new_platform);
        return new_platform;
    }
    @Override
    public void delete(String name) {
        SocialPlatform platform = socialPlatformRepository.findByName(name);
        if (platform != null) {
            socialPlatformRepository.delete(platform);
        }
    }

    @Override
    public SocialPlatform getSocialPlatformByName(String name) {
        return socialPlatformRepository.findByName(name);
    }

    @Override
    public List<SocialPlatform> getDefaultSocialPlatform() {
        return socialPlatformRepository.findAll();
    }
}
