package org.example.jwt_authentication_api.repository;

import org.example.jwt_authentication_api.entity.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IWarehouseRepository extends JpaRepository<WareHouse, Long> {
    Optional<WareHouse> findByCode(Long code);
}
