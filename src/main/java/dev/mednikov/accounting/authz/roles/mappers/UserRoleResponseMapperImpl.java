package dev.mednikov.accounting.authz.roles.mappers;

import dev.mednikov.accounting.authz.roles.domain.RoleResponse;
import dev.mednikov.accounting.authz.roles.domain.UserRoleResponse;
import dev.mednikov.accounting.authz.roles.models.UserRole;
import org.springframework.stereotype.Component;

@Component
public final class UserRoleResponseMapperImpl implements UserRoleResponseMapper {

    private final RoleResponseMapper roleResponseMapper;

    public UserRoleResponseMapperImpl(RoleResponseMapper roleResponseMapper) {
        this.roleResponseMapper = roleResponseMapper;
    }

    @Override
    public UserRoleResponse apply(UserRole userRole) {
        RoleResponse role = this.roleResponseMapper.apply(userRole.getRole());
        return new UserRoleResponse(userRole.getId(), userRole.getOrganizationId(), role, userRole.isCurrent());
    }

}
