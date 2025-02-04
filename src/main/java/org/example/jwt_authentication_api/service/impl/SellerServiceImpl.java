package org.example.jwt_authentication_api.service.impl;

import jakarta.transaction.Transactional;
import org.example.jwt_authentication_api.dto.MessageDto;
import org.example.jwt_authentication_api.dto.NewUserDto;
import org.example.jwt_authentication_api.entity.Role;
import org.example.jwt_authentication_api.entity.RoleEnum;
import org.example.jwt_authentication_api.entity.Seller;
import org.example.jwt_authentication_api.exceptions.ConflictException;
import org.example.jwt_authentication_api.exceptions.NotFoundException;
import org.example.jwt_authentication_api.repository.IRoleRepository;
import org.example.jwt_authentication_api.repository.ISellerRepository;
import org.example.jwt_authentication_api.service.ISellerService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerServiceImpl implements ISellerService {

    private final ISellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final IRoleRepository roleRepository;

    public SellerServiceImpl(ISellerRepository sellerRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, IRoleRepository roleRepository) {
        this.sellerRepository = sellerRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public MessageDto register(NewUserDto newSeller) {
        Optional<Seller> oldSeller = sellerRepository.findByUserName(newSeller.getUserName());
        if(oldSeller.isPresent()) {
            throw new ConflictException("Username is already taken");
        }
        Role role = validateRoleId(newSeller.getRoleId());

        Seller sellerToSave = modelMapper.map(newSeller, Seller.class);
        sellerToSave.setPassword(passwordEncoder.encode(newSeller.getPassword()));
        sellerToSave.setRole(role);

        Seller savedSeller = sellerRepository.save(sellerToSave);

        return new MessageDto("The Seller " + savedSeller.getUserName() + " has been successfully registered");
    }

    private Role validateRoleId(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if(role.isEmpty()) {
            throw new NotFoundException("Role not found");
        }
        boolean roleIsSeller = RoleEnum.SELLER.name().equals(role.get().getRoleName());
        if(!roleIsSeller) {
            throw new ConflictException("Role sent is not Seller");
        }

        return role.get();
    }
}
