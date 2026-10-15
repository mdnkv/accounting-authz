package dev.mednikov.accounting.authz.roles.services;

import dev.mednikov.accounting.authz.roles.domain.CreateRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.RoleResponse;
import dev.mednikov.accounting.authz.roles.domain.UpdateRoleRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleService {

    RoleResponse createRole (CreateRoleRequest request);

    RoleResponse updateRole (UpdateRoleRequest request);

    void deleteRole (UUID roleId);

    Optional<RoleResponse> getRoleById (UUID roleId);

    List<RoleResponse> getRolesForOrganization (UUID organizationId);

}
