package org.example.jwt_authentication_api.repository;

import org.example.jwt_authentication_api.entity.Representative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRepresentativeRepository extends JpaRepository<Representative, Long> {
    Optional<Representative> findByUserName(String username);
}
