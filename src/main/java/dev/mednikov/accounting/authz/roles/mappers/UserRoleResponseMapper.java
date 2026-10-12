package dev.mednikov.accounting.authz.roles.mappers;

import dev.mednikov.accounting.authz.roles.domain.UserRoleResponse;
import dev.mednikov.accounting.authz.roles.models.UserRole;

import java.util.function.Function;

public interface UserRoleResponseMapper extends Function<UserRole, UserRoleResponse> {
}
