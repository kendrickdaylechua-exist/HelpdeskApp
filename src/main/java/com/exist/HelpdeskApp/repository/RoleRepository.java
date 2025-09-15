package com.exist.HelpdeskApp.repository;

import com.exist.HelpdeskApp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    List<Role> findAllByDeletedFalse();
    Optional<Role> findByIdAndDeletedFalse(Integer id);
}
