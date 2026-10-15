package dev.mednikov.accounting.authz.roles.repositories;

import dev.mednikov.accounting.authz.roles.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    boolean existsByNameAndOrganizationId (String name, UUID organizationId);

    List<Role> findAllByOrganizationId (UUID id);

}
