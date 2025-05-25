package com.project.musicwebbe.repository.permission;

import com.project.musicwebbe.entities.permission.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<AppRole, Long> {
    AppRole findByRoleName(String roleName);
}
