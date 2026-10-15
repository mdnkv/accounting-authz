package dev.mednikov.accounting.authz.roles.repositories;

import dev.mednikov.accounting.authz.roles.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    List<UserRole> findAllByUserId (UUID userId);

    @Query("SELECT ur FROM UserRole ur WHERE ur.userId = :userId AND ur.current = true")
    Optional<UserRole> findCurrentForUser (UUID userId);

    long countByUserId(UUID userId);

    boolean existsByRoleIdAndUserId (UUID roleId, UUID userId);

}
