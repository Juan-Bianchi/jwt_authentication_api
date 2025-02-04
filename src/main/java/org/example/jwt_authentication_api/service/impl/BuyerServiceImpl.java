package org.example.jwt_authentication_api.service.impl;

import jakarta.transaction.Transactional;
import org.example.jwt_authentication_api.dto.MessageDto;
import org.example.jwt_authentication_api.dto.NewUserDto;
import org.example.jwt_authentication_api.entity.*;
import org.example.jwt_authentication_api.exceptions.ConflictException;
import org.example.jwt_authentication_api.exceptions.NotFoundException;
import org.example.jwt_authentication_api.repository.IBuyerRepository;
import org.example.jwt_authentication_api.repository.IOrderStatusRepository;
import org.example.jwt_authentication_api.repository.IRoleRepository;
import org.example.jwt_authentication_api.service.IBuyerService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuyerServiceImpl implements IBuyerService {

    private final IBuyerRepository buyerRepository;
    private final IOrderStatusRepository orderStatusRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final IRoleRepository roleRepository;

    public BuyerServiceImpl(IBuyerRepository buyerRepository, IOrderStatusRepository orderStatusRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, IRoleRepository roleRepository) {
        this.buyerRepository = buyerRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }


    @Override
    @Transactional
    public MessageDto register(NewUserDto newBuyer) {
        Optional<OrderStatus> orderStatus = orderStatusRepository.findByStatusCode("carrito");
        if(orderStatus.isEmpty()) {
            throw new NotFoundException("The order status 'carrito' was not found");
        }
        Optional<Buyer> oldBuyer = buyerRepository.findByUserName(newBuyer.getUserName());
        if(oldBuyer.isPresent()) {
            throw new ConflictException("Username is already taken");
        }
        Role role = validateRoleId(newBuyer.getRoleId());

        Buyer buyerToSave = modelMapper.map(newBuyer, Buyer.class);
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setBuyer(buyerToSave);
        purchaseOrder.setStatus(orderStatus.get());
        buyerToSave.setPassword(passwordEncoder.encode(newBuyer.getPassword()));
        buyerToSave.setPurchaseOrder(purchaseOrder);
        buyerToSave.setRole(role);

        Buyer savedBuyer = buyerRepository.save(buyerToSave);
        return new MessageDto("The buyer " + savedBuyer.getUserName() + " has been successfully registered");
    }

    private Role validateRoleId(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if(role.isEmpty()) {
            throw new NotFoundException("Role not found");
        }
        boolean roleIsSeller = RoleEnum.BUYER.name().equals(role.get().getRoleName());
        if(!roleIsSeller) {
            throw new ConflictException("Role sent is not Buyer");
        }

        return role.get();
    }
}
