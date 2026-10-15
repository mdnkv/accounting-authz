package dev.mednikov.accounting.authz.roles.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.UUID;

public record UpdateRoleRequest(
        @NotNull(message = "Role id cannot be null")
        UUID id,

        @NotNull(message = "Role name cannot be null")
        @NotBlank(message = "Role name cannot be blank")
        @Length(message = "Role name cannot exceed 255 characters", max = 255)
        String name,

        @NotNull(message = "Organization Id cannot be null")
        UUID organizationId,

        @NotNull(message = "Authorities cannot be blank")
        @NotEmpty(message = "Authorities list cannot be empty")
        List<UUID> authorities
) {
}
