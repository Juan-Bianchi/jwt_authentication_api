package org.example.jwt_authentication_api.service.impl;

import org.example.jwt_authentication_api.dto.MessageDto;
import org.example.jwt_authentication_api.dto.NewUserDto;
import org.example.jwt_authentication_api.entity.Representative;
import org.example.jwt_authentication_api.entity.Role;
import org.example.jwt_authentication_api.entity.RoleEnum;
import org.example.jwt_authentication_api.entity.WareHouse;
import org.example.jwt_authentication_api.exceptions.ConflictException;
import org.example.jwt_authentication_api.exceptions.NotFoundException;
import org.example.jwt_authentication_api.repository.IRepresentativeRepository;
import org.example.jwt_authentication_api.repository.IRoleRepository;
import org.example.jwt_authentication_api.repository.IWarehouseRepository;
import org.example.jwt_authentication_api.service.IRepresentativeService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RepresentativeServiceImpl implements IRepresentativeService {

    private final IRepresentativeRepository representativeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final IWarehouseRepository warehouseRepository;
    private final IRoleRepository roleRepository;

    public RepresentativeServiceImpl(IRepresentativeRepository representativeRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, IWarehouseRepository warehouseRepository, IRoleRepository roleRepository) {
        this.representativeRepository = representativeRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.warehouseRepository = warehouseRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public MessageDto register(NewUserDto newRepresentative) {
        Optional<Representative> oldRepresentative = representativeRepository.findByUserName(newRepresentative.getUserName());
        if(oldRepresentative.isPresent()) {
            throw new ConflictException("Username is already taken");
        }
        Optional<WareHouse> wareHouse = warehouseRepository.findByCode(newRepresentative.getWarehouseCode());
        if(wareHouse.isEmpty()) {
            throw new NotFoundException("The warehouse provided was not found");
        }
        Role role = validateRoleId(newRepresentative.getRoleId());

        Representative representativeToSave = modelMapper.map(newRepresentative, Representative.class);
        representativeToSave.setPassword(passwordEncoder.encode(newRepresentative.getPassword()));
        representativeToSave.setWareHouse(wareHouse.get());
        representativeToSave.setRole(role);

        Representative savedRepresentative = representativeRepository.save(representativeToSave);

        return new MessageDto("The Representative " + savedRepresentative.getUserName() + " has been successfully registered");
    }

    private Role validateRoleId(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if(role.isEmpty()) {
            throw new NotFoundException("Role not found");
        }
        boolean roleIsSeller = RoleEnum.REPRESENTATIVE.name().equals(role.get().getRoleName());
        if(!roleIsSeller) {
            throw new ConflictException("Role sent is not Representative");
        }

        return role.get();
    }
}
