package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.dto.JwtAuthResponseDto;
import com.gestion.eventos.api.dto.LoginDto;
import com.gestion.eventos.api.security.jwt.JwtGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> authenticateUser(@RequestBody LoginDto loginDto){
       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
       );

       SecurityContextHolder.getContext().setAuthentication(authentication);

       String token =  jwtGenerator.generateToken(authentication);

       return new ResponseEntity<>(new JwtAuthResponseDto(token), HttpStatus.OK);
    }
}
