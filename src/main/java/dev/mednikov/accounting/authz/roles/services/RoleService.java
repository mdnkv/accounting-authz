package dev.mednikov.accounting.authz.roles.services;

import dev.mednikov.accounting.authz.roles.domain.CreateRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.RoleResponse;
import dev.mednikov.accounting.authz.roles.domain.UpdateRoleRequest;
import dev.mednikov.accounting.authz.shared.domain.PaginationRequest;
import dev.mednikov.accounting.authz.shared.domain.PaginationResponse;

import java.util.Optional;
import java.util.UUID;

public interface RoleService {

    RoleResponse createRole (CreateRoleRequest request);

    RoleResponse updateRole (UpdateRoleRequest request);

    void deleteRole (UUID roleId);

    Optional<RoleResponse> getRoleById (UUID roleId);

    PaginationResponse<RoleResponse> getRoles (UUID organizationId, PaginationRequest request);

}
