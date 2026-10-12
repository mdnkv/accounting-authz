package dev.mednikov.accounting.authz.roles.domain;

import dev.mednikov.accounting.authz.authorities.domain.AuthorityResponse;

import java.util.Set;
import java.util.UUID;

public record RoleResponse(
        UUID id,
        UUID organizationId,
        String name,
        Set<AuthorityResponse> authorities
) {
}
