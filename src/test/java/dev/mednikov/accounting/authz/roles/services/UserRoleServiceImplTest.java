package dev.mednikov.accounting.authz.roles.services;

import dev.mednikov.accounting.authz.authorities.mappers.AuthorityResponseMapper;
import dev.mednikov.accounting.authz.authorities.mappers.AuthorityResponseMapperImpl;
import dev.mednikov.accounting.authz.authorities.models.Authority;
import dev.mednikov.accounting.authz.roles.domain.CreateUserRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.UpdateCurrentUserRoleRequest;
import dev.mednikov.accounting.authz.roles.exceptions.UserAlreadyMemberOrganization;
import dev.mednikov.accounting.authz.roles.exceptions.UserRoleDoesNotExistException;
import dev.mednikov.accounting.authz.roles.mappers.RoleResponseMapper;
import dev.mednikov.accounting.authz.roles.mappers.RoleResponseMapperImpl;
import dev.mednikov.accounting.authz.roles.mappers.UserRoleResponseMapper;
import dev.mednikov.accounting.authz.roles.mappers.UserRoleResponseMapperImpl;
import dev.mednikov.accounting.authz.roles.models.Role;
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

}
