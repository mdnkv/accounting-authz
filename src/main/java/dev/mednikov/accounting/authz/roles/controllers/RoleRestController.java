package dev.mednikov.accounting.authz.roles.controllers;

import dev.mednikov.accounting.authz.roles.domain.CreateRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.RoleResponse;
import dev.mednikov.accounting.authz.roles.domain.UpdateRoleRequest;
import dev.mednikov.accounting.authz.roles.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
public class RoleRestController {

    private final RoleService roleService;

    public RoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody RoleResponse createRole (@RequestBody @Valid CreateRoleRequest body){
        return this.roleService.createRole(body);
    }

    @PutMapping
    public @ResponseBody RoleResponse updateRole (@RequestBody @Valid UpdateRoleRequest body){
        return this.roleService.updateRole(body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole (@PathVariable UUID id){
        this.roleService.deleteRole(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById (@PathVariable UUID id){
        Optional<RoleResponse> result = this.roleService.getRoleById(id);
        return ResponseEntity.of(result);
    }

    @GetMapping
    public @ResponseBody List<RoleResponse> getRolesForOrganization (@RequestParam(required = true) UUID organizationId){
        return this.roleService.getRolesForOrganization(organizationId);
    }

}
