package com.sept.authmicroservice.repository;

import com.sept.authmicroservice.model.Role;
import com.sept.authmicroservice.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName roleName);

    Boolean existsByName(RoleName roleName);
}
