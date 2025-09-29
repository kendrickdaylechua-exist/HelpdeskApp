package com.exist.HelpdeskApp.repository;

import com.exist.HelpdeskApp.model.SecurityRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityRoleRepository extends JpaRepository<SecurityRole, Integer> {
    Optional<SecurityRole> findByName(String name);
}
