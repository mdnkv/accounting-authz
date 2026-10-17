package dev.mednikov.accounting.authz.roles.services;

import dev.mednikov.accounting.authz.roles.domain.CreateUserRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.CurrentUserRoleResponse;
import dev.mednikov.accounting.authz.roles.domain.UpdateCurrentUserRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.UserRoleResponse;

import java.util.List;
import java.util.UUID;

public interface UserRoleService {

    UserRoleResponse createUserRole (CreateUserRoleRequest request);

    CurrentUserRoleResponse updateCurrentUserRole (UpdateCurrentUserRoleRequest request);

    List<UserRoleResponse> getUserRoles (UUID userId);

    CurrentUserRoleResponse getCurrentUserRole (UUID userId);


}
