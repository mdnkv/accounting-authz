package dev.mednikov.accounting.authz.roles.services;

import dev.mednikov.accounting.authz.authorities.models.Authority;
import dev.mednikov.accounting.authz.roles.domain.CreateUserRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.CurrentUserRoleResponse;
import dev.mednikov.accounting.authz.roles.domain.UpdateCurrentUserRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.UserRoleResponse;
import dev.mednikov.accounting.authz.roles.exceptions.RoleDoesNotExistException;
import dev.mednikov.accounting.authz.roles.exceptions.UserAlreadyMemberOrganization;
import dev.mednikov.accounting.authz.roles.exceptions.UserRoleDoesNotExistException;
import dev.mednikov.accounting.authz.roles.mappers.UserRoleResponseMapper;
import dev.mednikov.accounting.authz.roles.models.Role;
import dev.mednikov.accounting.authz.roles.models.UserRole;
import dev.mednikov.accounting.authz.roles.repositories.RoleRepository;
import dev.mednikov.accounting.authz.roles.repositories.UserRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserRoleServiceImpl implements UserRoleService{

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRoleResponseMapper mapper;

    public UserRoleServiceImpl(RoleRepository roleRepository, UserRoleRepository userRoleRepository, UserRoleResponseMapper mapper) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.mapper = mapper;
    }

    @Override
    public List<UserRoleResponse> getUserRoles(UUID userId) {
        return this.userRoleRepository.findAllByUserId(userId)
                .stream()
                .map(this.mapper)
                .toList();
    }

    @Override
    public CurrentUserRoleResponse getCurrentUserRole(UUID userId) {
        Optional<UserRole> userRoleResult = this.userRoleRepository.findCurrentForUser(userId);
        if (userRoleResult.isPresent()){
            return this.mapToCurrentUserRoleResponse(userRoleResult.get());
        } else {
            return new CurrentUserRoleResponse(userId, false, null, null, List.of());
        }
    }

    @Override
    public UserRoleResponse createUserRole(CreateUserRoleRequest request) {
        // Find role
        Role role = this.roleRepository.findById(request.roleId())
                .orElseThrow(() -> new RoleDoesNotExistException(request.roleId()));

        // Check that user is not yet a member of the organization
        UUID organizationId = role.getOrganizationId();
        if (this.userRoleRepository.existsByOrganizationIdAndUserId(organizationId, request.userId())){
            throw new UserAlreadyMemberOrganization(request.userId(), organizationId);
        }

        // If user has no other roles, set it current
        boolean isCurrent = this.userRoleRepository.countByUserId(request.userId()) == 0;

        // Save
        UserRole userRole = new UserRole();
        userRole.setCurrent(isCurrent);
        userRole.setRole(role);
        userRole.setUserId(request.userId());
        userRole.setOrganizationId(organizationId);

        // Return result
        UserRole result = this.userRoleRepository.save(userRole);
        return this.mapper.apply(result);
    }

    @Transactional
    @Override
    public UserRoleResponse updateCurrentUserRole(UpdateCurrentUserRoleRequest request) {
        // Find current active
        Optional<UserRole> currentResult = this.userRoleRepository.findCurrentForUser(request.userId());
        // Set current active as not active
        if (currentResult.isPresent()) {
            UserRole current = currentResult.get();
            if (current.getId().equals(request.userRoleId())) {
                // User just wants a current role to be active,
                // then we just return it
                return this.mapper.apply(current);
            }
            current.setCurrent(false);
            this.userRoleRepository.save(current);
        }
        // Find user role by id
        UserRole userRole = this.userRoleRepository
                .findById(request.userRoleId())
                .orElseThrow(() -> new UserRoleDoesNotExistException(request.userRoleId()));
        // Set as active
        userRole.setCurrent(true);
        // Return result
        UserRole result = this.userRoleRepository.save(userRole);

        // TODO emit CurrentUserRoleUpdated event

        return this.mapper.apply(result);
    }

    CurrentUserRoleResponse mapToCurrentUserRoleResponse (UserRole userRole){
        List<String> authorities = userRole.getRole().getAuthorities().stream()
                .map(Authority::getName).toList();
        return new CurrentUserRoleResponse(
                userRole.getUserId(),
                true,
                userRole.getOrganizationId(),
                userRole.getRole().getName(),
                authorities);
    }
}
