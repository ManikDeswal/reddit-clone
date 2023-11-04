package com.reddit.clone.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity  
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    
    /*CRSF attacks occur when there are session and when we are using cookies 
    to auth and as REST APIs are stateless by defination we can safely dissable */
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable()
        .authorizeRequests()
        .antMatchers("/api/auth/**")
        .permitAll()
        .anyRequest()
        .authenticated();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }  

}
