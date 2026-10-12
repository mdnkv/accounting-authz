package dev.mednikov.accounting.authz.roles.domain;

import java.util.UUID;

public record UserRoleResponse(
        UUID id,
        RoleResponse role,
        boolean current
) {
}
