package org.example.jwt_authentication_api.exceptions;

import lombok.Getter;

@Getter
public class JwtPersonalizedException extends RuntimeException {
    private final int statusCode;

    public JwtPersonalizedException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
