package org.example.jwt_authentication_api.repository;

import org.example.jwt_authentication_api.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IOrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    Optional<OrderStatus> findByStatusCode(String statusCode);
}
