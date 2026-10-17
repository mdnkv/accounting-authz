package dev.mednikov.accounting.authz.roles.services;

import dev.mednikov.accounting.authz.authorities.mappers.AuthorityResponseMapper;
import dev.mednikov.accounting.authz.authorities.mappers.AuthorityResponseMapperImpl;
import dev.mednikov.accounting.authz.authorities.models.Authority;
import dev.mednikov.accounting.authz.roles.domain.CreateUserRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.CurrentUserRoleResponse;
import dev.mednikov.accounting.authz.roles.domain.UpdateCurrentUserRoleRequest;
import dev.mednikov.accounting.authz.roles.exceptions.UserAlreadyMemberOrganization;
import dev.mednikov.accounting.authz.roles.exceptions.UserRoleDoesNotExistException;
import dev.mednikov.accounting.authz.roles.mappers.RoleResponseMapper;
import dev.mednikov.accounting.authz.roles.mappers.RoleResponseMapperImpl;
import dev.mednikov.accounting.authz.roles.mappers.UserRoleResponseMapper;
import dev.mednikov.accounting.authz.roles.mappers.UserRoleResponseMapperImpl;
import dev.mednikov.accounting.authz.roles.models.Role;
import dev.mednikov.accounting.authz.roles.models.UserRole;
import dev.mednikov.accounting.authz.roles.repositories.RoleRepository;
import dev.mednikov.accounting.authz.roles.repositories.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceImplTest {

    @Mock private UserRoleRepository userRoleRepository;
    @Mock private RoleRepository roleRepository;

    private UserRoleService service;

    @BeforeEach
    void setup(){
        AuthorityResponseMapper authorityResponseMapper = new AuthorityResponseMapperImpl();
        RoleResponseMapper roleResponseMapper = new RoleResponseMapperImpl(authorityResponseMapper);
        UserRoleResponseMapper userRoleResponseMapper = new UserRoleResponseMapperImpl(roleResponseMapper);
        this.service = new UserRoleServiceImpl(roleRepository, userRoleRepository, userRoleResponseMapper);
    }

    @Test
    void createUserRole_alreadyExistsTest(){
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        Role role = new Role();
        role.setOrganizationId(organizationId);
        role.setId(roleId);
        role.setAuthorities(Set.of(new Authority(UUID.randomUUID(), "accounts:view")));
        role.setName("User");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRoleRepository.existsByOrganizationIdAndUserId(organizationId, userId)).thenReturn(true);

        CreateUserRoleRequest request = new CreateUserRoleRequest(userId, roleId);

        assertThatThrownBy(() -> service.createUserRole(request)).isInstanceOf(UserAlreadyMemberOrganization.class);
    }

    @Test
    void updateCurrentUserRole_userRoleDoesNotExistTest(){
        UUID userId = UUID.randomUUID();
        UUID userRoleId = UUID.randomUUID();

        UpdateCurrentUserRoleRequest request = new UpdateCurrentUserRoleRequest(userId, userRoleId);

        when(userRoleRepository.findCurrentForUser(userId)).thenReturn(Optional.empty());
        when(userRoleRepository.findById(userRoleId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCurrentUserRole(request)).isInstanceOf(UserRoleDoesNotExistException.class);

    }

    @Test
    void updateCurrentUserRole_noCurrentUserRoleTest(){
        UUID userId = UUID.randomUUID();
        UUID userRoleId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        Set<Authority> authorities = Set.of(
                new Authority(UUID.randomUUID(), "journals:create"),
                new Authority(UUID.randomUUID(), "journals:update"),
                new Authority(UUID.randomUUID(), "journals:view"),
                new Authority(UUID.randomUUID(), "currencies:create"),
                new Authority(UUID.randomUUID(), "currencies:update"),
                new Authority(UUID.randomUUID(), "currencies:view")
        );

        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName("Administrator");
        role.setOrganizationId(organizationId);
        role.setAuthorities(authorities);

        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setOrganizationId(organizationId);
        userRole.setCurrent(true);
        userRole.setRole(role);
        userRole.setId(userRoleId);

        UpdateCurrentUserRoleRequest request = new UpdateCurrentUserRoleRequest(userId, userRoleId);

        when(userRoleRepository.findCurrentForUser(userId)).thenReturn(Optional.empty());
        when(userRoleRepository.findById(userRoleId)).thenReturn(Optional.of(userRole));
        when(userRoleRepository.save(any(UserRole.class))).thenReturn(userRole);

        CurrentUserRoleResponse result = service.updateCurrentUserRole(request);
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("hasRole", true)
                .hasFieldOrPropertyWithValue("roleName", "Administrator")
                .hasFieldOrProperty("authorities")
                .hasFieldOrPropertyWithValue("organizationId", organizationId);
    }

    @Test
    void updateCurrentUserRole_hasCurrentUserRoleTest(){
        UUID userId = UUID.randomUUID();
        UUID userRoleId = UUID.randomUUID();
        UUID oldOrganizationId = UUID.randomUUID();
        UUID newOrganizationId = UUID.randomUUID();

        Set<Authority> authorities = Set.of(
                new Authority(UUID.randomUUID(), "journals:create"),
                new Authority(UUID.randomUUID(), "journals:update"),
                new Authority(UUID.randomUUID(), "journals:view"),
                new Authority(UUID.randomUUID(), "currencies:create"),
                new Authority(UUID.randomUUID(), "currencies:update"),
                new Authority(UUID.randomUUID(), "currencies:view")
        );

        Role oldRole = new Role();
        oldRole.setId(UUID.randomUUID());
        oldRole.setName("Administrator");
        oldRole.setOrganizationId(oldOrganizationId);
        oldRole.setAuthorities(authorities);

        UserRole oldUserRole = new UserRole();
        oldUserRole.setUserId(userId);
        oldUserRole.setOrganizationId(oldOrganizationId);
        oldUserRole.setCurrent(true);
        oldUserRole.setRole(oldRole);
        oldUserRole.setId(UUID.randomUUID());

        Role newRole = new Role();
        newRole.setId(UUID.randomUUID());
        newRole.setName("Accountant");
        newRole.setOrganizationId(newOrganizationId);
        newRole.setAuthorities(authorities);

        UserRole newUserRole = new UserRole();
        newUserRole.setUserId(userId);
        newUserRole.setOrganizationId(newOrganizationId);
        newUserRole.setCurrent(false);
        newUserRole.setRole(newRole);
        newUserRole.setId(userRoleId);

        UpdateCurrentUserRoleRequest request = new UpdateCurrentUserRoleRequest(userId, userRoleId);

        when(userRoleRepository.findCurrentForUser(userId)).thenReturn(Optional.of(oldUserRole));
        when(userRoleRepository.findById(userRoleId)).thenReturn(Optional.of(newUserRole));
        when(userRoleRepository.save(any(UserRole.class))).thenReturn(newUserRole);

        CurrentUserRoleResponse result = service.updateCurrentUserRole(request);
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("hasRole", true)
                .hasFieldOrPropertyWithValue("roleName", "Accountant")
                .hasFieldOrProperty("authorities")
                .hasFieldOrPropertyWithValue("organizationId", newOrganizationId);
    }

    @Test
    void getCurrentUserRole_hasRoleTest(){
        UUID userId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        Set<Authority> authorities = Set.of(
                new Authority(UUID.randomUUID(), "journals:create"),
                new Authority(UUID.randomUUID(), "journals:update"),
                new Authority(UUID.randomUUID(), "journals:view"),
                new Authority(UUID.randomUUID(), "currencies:create"),
                new Authority(UUID.randomUUID(), "currencies:update"),
                new Authority(UUID.randomUUID(), "currencies:view")
        );

        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName("Administrator");
        role.setOrganizationId(organizationId);
        role.setAuthorities(authorities);

        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setOrganizationId(organizationId);
        userRole.setCurrent(true);
        userRole.setRole(role);

        when(userRoleRepository.findCurrentForUser(userId)).thenReturn(Optional.of(userRole));

        CurrentUserRoleResponse result = service.getCurrentUserRole(userId);
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("hasRole", true)
                .hasFieldOrPropertyWithValue("roleName", "Administrator")
                .hasFieldOrProperty("authorities")
                .hasFieldOrPropertyWithValue("organizationId", organizationId);

    }

    @Test
    void getCurrentUserRole_hasNotRoleTest(){
        UUID userId = UUID.randomUUID();
        when(userRoleRepository.findCurrentForUser(userId)).thenReturn(Optional.empty());

        CurrentUserRoleResponse result = service.getCurrentUserRole(userId);
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("hasRole", false);

    }

}
