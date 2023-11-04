package com.reddit.clone.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.reddit.clone.DTO.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @PostMapping("/signup")
    public void signup(@RequestBody RegisterRequest RegisterRequest){

    }

}
