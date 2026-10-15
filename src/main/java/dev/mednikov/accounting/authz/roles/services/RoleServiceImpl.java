package dev.mednikov.accounting.authz.roles.services;

import dev.mednikov.accounting.authz.authorities.models.Authority;
import dev.mednikov.accounting.authz.authorities.repositories.AuthorityRepository;
import dev.mednikov.accounting.authz.roles.domain.CreateRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.RoleResponse;
import dev.mednikov.accounting.authz.roles.domain.UpdateRoleRequest;
import dev.mednikov.accounting.authz.roles.exceptions.RoleAlreadyExistsException;
import dev.mednikov.accounting.authz.roles.exceptions.RoleDoesNotExistException;
import dev.mednikov.accounting.authz.roles.mappers.RoleResponseMapper;
import dev.mednikov.accounting.authz.roles.models.Role;
import dev.mednikov.accounting.authz.roles.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleResponseMapper mapper;

    public RoleServiceImpl(RoleRepository roleRepository, AuthorityRepository authorityRepository, RoleResponseMapper mapper) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.mapper = mapper;
    }

    @Override
    public RoleResponse createRole(CreateRoleRequest request) {
        String roleName = request.name();
        UUID organizationId = request.organizationId();
        if (this.roleRepository.existsByNameAndOrganizationId(roleName, organizationId)){
            // throw exception
            throw new RoleAlreadyExistsException(organizationId, roleName);
        }
        // Get authorities
        List<Authority> authorities = this.authorityRepository.findAllById(request.authorities());

        Role role = new Role();
        role.setAuthorities(new HashSet<>(authorities));
        role.setName(roleName);
        role.setOrganizationId(organizationId);

        Role result = this.roleRepository.save(role);
        return this.mapper.apply(result);
    }

    @Override
    public RoleResponse updateRole(UpdateRoleRequest request) {
        Role role = this.roleRepository.findById(request.id())
                .orElseThrow(() -> new RoleDoesNotExistException(request.id()));
        List<Authority> authorities = this.authorityRepository.findAllById(request.authorities());
        // if name is changed, check it is not yet in use
        String newName = request.name();
        if (!role.getName().equals(newName)){
            if (this.roleRepository.existsByNameAndOrganizationId(newName, request.organizationId())){
                throw new RoleAlreadyExistsException(request.organizationId(), newName);
            }
            role.setName(newName);
        }
        role.setAuthorities(new HashSet<>(authorities));
        Role result = this.roleRepository.save(role);
        return this.mapper.apply(result);
    }

    @Override
    public void deleteRole(UUID roleId) {
        this.roleRepository.deleteById(roleId);
    }

    @Override
    public Optional<RoleResponse> getRoleById(UUID roleId) {
        return this.roleRepository.findById(roleId).map(this.mapper);
    }

    @Override
    public List<RoleResponse> getRolesForOrganization(UUID organizationId) {
        return this.roleRepository
                .findAllByOrganizationId(organizationId)
                .stream()
                .map(this.mapper)
                .toList();
    }
}
