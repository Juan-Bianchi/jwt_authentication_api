package org.example.jwt_authentication_api.service;


import org.example.jwt_authentication_api.dto.LoginSuccessfulDto;
import org.example.jwt_authentication_api.dto.LoginUserDto;

public interface IAuthService {
    LoginSuccessfulDto authenticate(LoginUserDto user);
}
