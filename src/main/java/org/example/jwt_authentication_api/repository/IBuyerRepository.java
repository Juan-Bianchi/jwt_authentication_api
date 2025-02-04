package org.example.jwt_authentication_api.repository;

import org.example.jwt_authentication_api.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IBuyerRepository extends JpaRepository<Buyer, Long> {
    Optional<Buyer> findByUserName(String username);
}
