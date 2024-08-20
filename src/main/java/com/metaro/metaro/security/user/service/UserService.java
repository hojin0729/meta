package com.metaro.metaro.security.user.service;

import com.metaro.metaro.security.user.entity.User;
import com.metaro.metaro.security.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUser(String email){
        Optional<User> user = userRepository.findByUserEmail(email);

        return user;
    }
}