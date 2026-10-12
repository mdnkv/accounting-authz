package dev.mednikov.accounting.authz.roles.services;

import dev.mednikov.accounting.authz.roles.domain.UpdateCurrentUserRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.UserRoleResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRoleService {

    UserRoleResponse updateCurrentUserRole (UpdateCurrentUserRoleRequest request);

    List<UserRoleResponse> getUserRoles (UUID userId);

    Optional<UserRoleResponse> getCurrentUserRole (UUID userId);


}
