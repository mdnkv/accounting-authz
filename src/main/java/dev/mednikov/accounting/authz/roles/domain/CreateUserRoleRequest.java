package dev.mednikov.accounting.authz.roles.domain;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateUserRoleRequest(
        @NotNull(message = "User cannot be null") UUID userId,
        @NotNull(message = "Role cannot be null") UUID roleId
) {
}
