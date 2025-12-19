package com.gestion.eventos.api.security.controller;

import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.security.dto.JwtAuthResponseDto;
import com.gestion.eventos.api.security.dto.LoginDto;
import com.gestion.eventos.api.security.dto.RegisterDto;
import com.gestion.eventos.api.mapper.UserMapper;
import com.gestion.eventos.api.repository.UserRepository;
import com.gestion.eventos.api.security.jwt.JwtGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Operaciones de autenticación y registro de usuarios")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario", description = "Autentica un usuario con username y password y retorna un JWT válido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa, JWT generado"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<JwtAuthResponseDto> authenticateUser(@RequestBody LoginDto loginDto){
       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
       );

       SecurityContextHolder.getContext().setAuthentication(authentication);

       String token =  jwtGenerator.generateToken(authentication);

       return new ResponseEntity<>(new JwtAuthResponseDto(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Registra un nuevo usuario en el sistema. El username y el email deben ser únicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Username o email ya existente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto){
        if(userRepository.existsByUsername(registerDto.getUsername())){
            return new ResponseEntity<>("Nombre de usuario, ya existe...", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            return new ResponseEntity<>("Email de usuario, ya existe...", HttpStatus.BAD_REQUEST);
        }
        User user = userMapper.registerDtoToUser(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        userRepository.save(user);

        return new ResponseEntity<>("Usuario registrado", HttpStatus.CREATED);
    }

}
