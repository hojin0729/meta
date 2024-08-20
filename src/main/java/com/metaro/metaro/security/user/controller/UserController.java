package com.metaro.metaro.security.user.controller;

import com.metaro.metaro.security.user.entity.User;
import com.metaro.metaro.security.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Controller
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @PostMapping("/signup")
    public ResponseEntity singup(@RequestBody User user){

        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        User value = userRepository.save(user);

        if(Objects.isNull(value)){
            return ResponseEntity.status(500).body("회원가입 실패");
        }else{
            return ResponseEntity.ok("회원가입 실패");
        }

    }
}