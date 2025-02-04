package org.example.jwt_authentication_api.service.impl;

import org.example.jwt_authentication_api.dto.LoginSuccessfulDto;
import org.example.jwt_authentication_api.dto.LoginUserDto;
import org.example.jwt_authentication_api.entity.*;
import org.example.jwt_authentication_api.exceptions.NotFoundException;
import org.example.jwt_authentication_api.repository.*;
import org.example.jwt_authentication_api.security.JwtService;
import org.example.jwt_authentication_api.service.IAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {

    private final IUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(IUserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public LoginSuccessfulDto authenticate(LoginUserDto input) {
        Optional<User> user = userRepository.findByUserName(input.getUsername());
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return new LoginSuccessfulDto(jwtService.generateToken(userDetails));
    }
}