package org.example.jwt_authentication_api.service;

import org.example.jwt_authentication_api.dto.MessageDto;
import org.example.jwt_authentication_api.dto.NewUserDto;

public interface ISellerService {
    MessageDto register(NewUserDto newSeller);
}
