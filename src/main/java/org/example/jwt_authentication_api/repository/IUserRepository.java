package org.example.jwt_authentication_api.repository;

import org.example.jwt_authentication_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
}
