package dev.mednikov.accounting.authz.roles.services;

import dev.mednikov.accounting.authz.authorities.mappers.AuthorityResponseMapper;
import dev.mednikov.accounting.authz.authorities.mappers.AuthorityResponseMapperImpl;
import dev.mednikov.accounting.authz.authorities.models.Authority;
import dev.mednikov.accounting.authz.authorities.repositories.AuthorityRepository;
import dev.mednikov.accounting.authz.roles.domain.CreateRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.RoleResponse;
import dev.mednikov.accounting.authz.roles.domain.UpdateRoleRequest;
import dev.mednikov.accounting.authz.roles.exceptions.RoleAlreadyExistsException;
import dev.mednikov.accounting.authz.roles.exceptions.RoleDoesNotExistException;
import dev.mednikov.accounting.authz.roles.mappers.RoleResponseMapper;
import dev.mednikov.accounting.authz.roles.mappers.RoleResponseMapperImpl;
import dev.mednikov.accounting.authz.roles.models.Role;
import dev.mednikov.accounting.authz.roles.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    private final List<Authority> authorities = List.of(
            new Authority(UUID.randomUUID(), "journals:create"),
            new Authority(UUID.randomUUID(), "journals:update"),
            new Authority(UUID.randomUUID(), "journals:view"),
            new Authority(UUID.randomUUID(), "currencies:create"),
            new Authority(UUID.randomUUID(), "currencies:update"),
            new Authority(UUID.randomUUID(), "currencies:view")
    );

    @Mock private AuthorityRepository authorityRepository;
    @Mock private RoleRepository roleRepository;
    private RoleServiceImpl service;

    List<UUID> getAuthoritiesIdsList(){
        return this.authorities.stream().map(Authority::getId).toList();
    }

    @BeforeEach
    void setup (){
        AuthorityResponseMapper authorityResponseMapper = new AuthorityResponseMapperImpl();
        RoleResponseMapper roleResponseMapper = new RoleResponseMapperImpl(authorityResponseMapper);
        this.service = new RoleServiceImpl(roleRepository, authorityRepository, roleResponseMapper);
    }

    @Test
    void createRole_alreadyExistsTest(){
        UUID organizationId = UUID.randomUUID();
        String roleName = "Administrator";
        CreateRoleRequest request = new CreateRoleRequest(roleName, organizationId, getAuthoritiesIdsList());

        when(roleRepository.existsByNameAndOrganizationId(roleName, organizationId)).thenReturn(true);

        assertThatThrownBy(() -> service.createRole(request)).isInstanceOf(RoleAlreadyExistsException.class);

    }

    @Test
    void createRole_successTest(){
        UUID roleId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        String roleName = "Administrator";
        List<UUID> authoritiesIds = getAuthoritiesIdsList();
        CreateRoleRequest request = new CreateRoleRequest(roleName, organizationId, authoritiesIds);

        Role role = new Role();
        role.setName(roleName);
        role.setAuthorities(new HashSet<>(authorities));
        role.setId(roleId);
        role.setOrganizationId(organizationId);

        when(roleRepository.existsByNameAndOrganizationId(roleName, organizationId)).thenReturn(false);
        when(authorityRepository.findAllById(any())).thenReturn(authorities);
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        RoleResponse result = service.createRole(request);
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("id", roleId)
                .hasFieldOrPropertyWithValue("name", roleName)
                .hasFieldOrPropertyWithValue("organizationId", organizationId)
                .hasFieldOrProperty("authorities");
    }

    @Test
    void updateRole_doesNotExistTest(){
        UUID roleId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        String roleName = "Administrator";
        List<UUID> authoritiesIds = getAuthoritiesIdsList();
        UpdateRoleRequest request = new UpdateRoleRequest(roleId, roleName, organizationId, authoritiesIds);

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateRole(request)).isInstanceOf(RoleDoesNotExistException.class);
    }

    @Test
    void updateRole_alreadyExistsTest(){
        UUID roleId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        String roleName = "Administrador";
        List<UUID> authoritiesIds = getAuthoritiesIdsList();
        UpdateRoleRequest request = new UpdateRoleRequest(roleId, roleName, organizationId, authoritiesIds);

        Role role = new Role();
        role.setName("Administrator");
        role.setAuthorities(new HashSet<>(authorities));
        role.setId(roleId);
        role.setOrganizationId(organizationId);

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleRepository.existsByNameAndOrganizationId(roleName, organizationId)).thenReturn(true);

        assertThatThrownBy(() -> service.updateRole(request)).isInstanceOf(RoleAlreadyExistsException.class);
    }

    @Test
    void updateRole_successTest(){
        UUID roleId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        String roleName = "Administrador";
        List<UUID> authoritiesIds = getAuthoritiesIdsList();
        UpdateRoleRequest request = new UpdateRoleRequest(roleId, roleName, organizationId, authoritiesIds);

        Role role = new Role();
        role.setName("Administrator");
        role.setAuthorities(new HashSet<>(authorities));
        role.setId(roleId);
        role.setOrganizationId(organizationId);

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleRepository.existsByNameAndOrganizationId(roleName, organizationId)).thenReturn(false);
        when(authorityRepository.findAllById(any())).thenReturn(authorities);
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        RoleResponse result = service.updateRole(request);
        assertThat(result).isNotNull();
    }



}
