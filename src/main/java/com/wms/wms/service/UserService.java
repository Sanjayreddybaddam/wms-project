package com.wms.wms.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wms.wms.dto.UserRequest;
import com.wms.wms.entity.User;
import com.wms.wms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    public User createUser(UserRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
