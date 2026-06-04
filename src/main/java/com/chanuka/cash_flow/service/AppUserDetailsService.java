package com.chanuka.cash_flow.service;

import com.chanuka.cash_flow.entity.ProfileEntity;
import com.chanuka.cash_flow.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    // loads user details from the database using email as username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ProfileEntity existingProfile = profileRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: " + username));
        return User.builder()
                .username(existingProfile.getEmail())
                .password(existingProfile.getPassword())
                .authorities(List.of())
                .build();
    }
}
