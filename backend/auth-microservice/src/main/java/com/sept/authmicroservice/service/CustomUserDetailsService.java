package com.sept.authmicroservice.service;

import com.sept.authmicroservice.model.User;
import com.sept.authmicroservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) new UsernameNotFoundException("User not found");
        return user;
    }


    @Transactional
    public User loadUserById(int id) {
        User user = userRepository.getByUserID(id);
        if (user == null) new UsernameNotFoundException("User not found");
        return user;

    }
}