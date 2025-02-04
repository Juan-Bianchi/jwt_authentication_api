package org.example.jwt_authentication_api.repository;

import org.example.jwt_authentication_api.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUserName(String username);
}
