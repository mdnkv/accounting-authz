package dev.mednikov.accounting.authz.roles.mappers;

import dev.mednikov.accounting.authz.roles.domain.RoleResponse;
import dev.mednikov.accounting.authz.roles.models.Role;

import java.util.function.Function;

public interface RoleResponseMapper extends Function<Role, RoleResponse> {
}
