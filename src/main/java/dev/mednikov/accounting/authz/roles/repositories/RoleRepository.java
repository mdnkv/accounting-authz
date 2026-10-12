package dev.mednikov.accounting.authz.roles.repositories;

import dev.mednikov.accounting.authz.roles.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
}
