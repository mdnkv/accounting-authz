package dev.mednikov.accounting.authz.roles.controllers;

import dev.mednikov.accounting.authz.roles.domain.CreateUserRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.UpdateCurrentUserRoleRequest;
import dev.mednikov.accounting.authz.roles.domain.UserRoleResponse;
import dev.mednikov.accounting.authz.roles.services.UserRoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user-roles")
public class UserRoleRestController {

    private final UserRoleService userRoleService;

    public UserRoleRestController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody UserRoleResponse createUserRole (@RequestBody @Valid CreateUserRoleRequest body){
        return this.userRoleService.createUserRole(body);
    }

    @GetMapping("/user/{userId}")
    public @ResponseBody List<UserRoleResponse> getUserRoles (@PathVariable UUID userId){
        return this.userRoleService.getUserRoles(userId);
    }

    @GetMapping("/current/{userId}")
    public ResponseEntity<UserRoleResponse> getCurrentUserRole (@PathVariable UUID userId){
        Optional<UserRoleResponse> result = this.userRoleService.getCurrentUserRole(userId);
        return ResponseEntity.of(result);
    }

    @PostMapping("/current")
    public @ResponseBody UserRoleResponse setCurrentUserRole (@RequestBody @Valid UpdateCurrentUserRoleRequest body){
        return this.userRoleService.updateCurrentUserRole(body);
    }

}
