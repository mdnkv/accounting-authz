package dev.mednikov.accounting.authz.authorities.repositories;

import dev.mednikov.accounting.authz.authorities.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
}
