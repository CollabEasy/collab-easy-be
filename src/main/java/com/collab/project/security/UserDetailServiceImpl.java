package com.collab.project.security;

import com.collab.project.model.artist.Artist;
import com.collab.project.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    ArtistRepository artistRepository;


    @Override
    public UserDetails loadUserByUsername(String artistId) throws UsernameNotFoundException {
        Artist artist = artistRepository.findByArtistId(artistId);
        return UserDetailsImpl.build(artist);
    }
}
