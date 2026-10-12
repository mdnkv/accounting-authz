package dev.mednikov.accounting.authz.roles.mappers;

import dev.mednikov.accounting.authz.authorities.domain.AuthorityResponse;
import dev.mednikov.accounting.authz.authorities.mappers.AuthorityResponseMapper;
import dev.mednikov.accounting.authz.roles.domain.RoleResponse;
import dev.mednikov.accounting.authz.roles.models.Role;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public final class RoleResponseMapperImpl implements RoleResponseMapper{

    private final AuthorityResponseMapper authorityResponseMapper;

    public RoleResponseMapperImpl(AuthorityResponseMapper authorityResponseMapper) {
        this.authorityResponseMapper = authorityResponseMapper;
    }

    @Override
    public RoleResponse apply(Role role) {
        Set<AuthorityResponse> authorities = role.getAuthorities()
                .stream()
                .map(this.authorityResponseMapper)
                .collect(Collectors.toSet());
        return new RoleResponse(role.getId(), role.getOrganizationId(), role.getName(), authorities);
    }
}
