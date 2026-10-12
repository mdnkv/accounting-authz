package dev.mednikov.accounting.authz.roles.domain;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateCurrentUserRoleRequest(
        @NotNull(message = "User id cannot be empty") UUID userId,
        @NotNull(message = "User role id cannot be empty") UUID userRoleId
) {
}
