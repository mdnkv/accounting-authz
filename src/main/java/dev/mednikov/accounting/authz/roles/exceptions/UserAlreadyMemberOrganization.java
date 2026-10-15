package dev.mednikov.accounting.authz.roles.exceptions;

import dev.mednikov.accounting.authz.shared.exceptions.ItemAlreadyExistsException;

import java.util.UUID;

public final class UserAlreadyMemberOrganization extends ItemAlreadyExistsException {

    public UserAlreadyMemberOrganization(UUID userId, UUID organizationId){
        super("User role for user", organizationId, userId.toString());
    }
}
