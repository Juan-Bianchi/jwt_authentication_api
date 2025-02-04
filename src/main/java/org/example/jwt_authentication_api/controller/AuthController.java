package org.example.jwt_authentication_api.controller;

import jakarta.validation.Valid;
import org.example.jwt_authentication_api.dto.LoginSuccessfulDto;
import org.example.jwt_authentication_api.dto.LoginUserDto;
import org.example.jwt_authentication_api.dto.MessageDto;
import org.example.jwt_authentication_api.dto.NewUserDto;
import org.example.jwt_authentication_api.service.IAuthService;
import org.example.jwt_authentication_api.service.IBuyerService;
import org.example.jwt_authentication_api.service.IRepresentativeService;
import org.example.jwt_authentication_api.service.ISellerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final IAuthService authService;
    private final ISellerService sellerService;
    private final IRepresentativeService representativeService;
    private final IBuyerService buyerService;

    public AuthController(IAuthService authService, ISellerService sellerService, IRepresentativeService representativeService, IBuyerService buyerService) {
        this.authService = authService;
        this.sellerService = sellerService;
        this.representativeService = representativeService;
        this.buyerService = buyerService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginSuccessfulDto> authenticateUser(@RequestBody @Valid LoginUserDto user) {
        return new ResponseEntity<>(authService.authenticate(user), HttpStatus.OK);
    }

    @PostMapping("/seller/signup")
    public ResponseEntity<MessageDto> registerSeller(@RequestBody @Valid NewUserDto user) {
        System.out.println(user);
        return new ResponseEntity<>(sellerService.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/buyer/signup")
    public ResponseEntity<MessageDto> registerBuyer(@RequestBody @Valid NewUserDto user) {
        return new ResponseEntity<>(buyerService.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/representative/signup")
    public ResponseEntity<MessageDto> registerRepresentative(@RequestBody @Valid NewUserDto user) {
        return new ResponseEntity<>(representativeService.register(user), HttpStatus.CREATED);
    }
}
