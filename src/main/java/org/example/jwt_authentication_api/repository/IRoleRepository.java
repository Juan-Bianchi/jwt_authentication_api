package org.example.jwt_authentication_api.repository;

import org.example.jwt_authentication_api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {
}
