package dev.mednikov.accounting.authz.roles.services;

import dev.mednikov.accounting.authz.roles.domain.UpdateCurrentUserRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.UserRoleResponse;
import dev.mednikov.accounting.authz.roles.exceptions.UserRoleDoesNotExistException;
import dev.mednikov.accounting.authz.roles.mappers.UserRoleResponseMapper;
import dev.mednikov.accounting.authz.roles.models.UserRole;
import dev.mednikov.accounting.authz.roles.repositories.UserRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserRoleServiceImpl implements UserRoleService{

    private final UserRoleRepository userRoleRepository;
    private final UserRoleResponseMapper mapper;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, UserRoleResponseMapper mapper) {
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
    public Optional<UserRoleResponse> getCurrentUserRole(UUID userId) {
        return this.userRoleRepository.findCurrentForUser(userId).map(this.mapper);
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
}
