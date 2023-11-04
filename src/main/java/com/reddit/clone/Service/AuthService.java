package com.reddit.clone.Service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.reddit.clone.DTO.RegisterRequest;
import com.reddit.clone.Repository.UserRepository;
import com.reddit.clone.Repository.VerificationTokenRepository;
import com.reddit.clone.model.User;
import com.reddit.clone.model.VerificationToken;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user=new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        String token = generateVerficationToken(user);
    }
    
    private String generateVerficationToken(User user){
            String token=UUID.randomUUID().toString();
            VerificationToken verificationToken=new VerificationToken();
            verificationToken.setToken(token);
            verificationToken.setUser(user);
            verificationTokenRepository.save(verificationToken);
            return token;
        }
     
}
