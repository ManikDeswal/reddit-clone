package com.reddit.clone.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.reddit.Exceptions.SpringRedditException;
import com.reddit.clone.DTO.AuthenticationResponse;
import com.reddit.clone.DTO.RegisterRequest;
import com.reddit.clone.DTO.loginRequest;
import com.reddit.clone.Repository.UserRepository;
import com.reddit.clone.Repository.VerificationTokenRepository;
import com.reddit.clone.    Security.JwtProvider;
import com.reddit.clone.model.NotificationEmail;
import com.reddit.clone.model.User;
import com.reddit.clone.model.VerificationToken;


import lombok.AllArgsConstructor;   

@Service
@AllArgsConstructor
public class AuthService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;


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
        mailService.sendMail(new NotificationEmail("Please Activate your Account",user.getEmail(),"Thank you for signing up to Spring Reddit, " +
        "please click on the below url to activate your account : " +
        "http://localhost:9090/api/auth/accountVerification/"+token));
    }
    
    private String generateVerficationToken(User user){
            String token=UUID.randomUUID().toString();
            VerificationToken verificationToken=new VerificationToken();
            verificationToken.setToken(token);
            verificationToken.setUser(user);
            verificationTokenRepository.save(verificationToken);
            return token;
    }

     public void  verifyAccount(String token){
        Optional<VerificationToken> verificationToken=verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(()-> new SpringRedditException("Invalid Token"));
        fetchUserandEnable(verificationToken.get());
    }
    
    @Transactional
    private void fetchUserandEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUsername();
        User user=userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("User Not Found"));
        user.setEnabled(true);
        userRepository.save(user);  
    }

    public AuthenticationResponse login(loginRequest loginRequest){
        Authentication authenticate=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token=jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token,loginRequest.getUsername());
    } 

    public User getCurrentUser(){
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
        .getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
        .orElseThrow(()->new UsernameNotFoundException("Username not Found" + principal.getUsername()));
    }
     public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
