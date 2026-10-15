package dev.mednikov.accounting.authz.roles.exceptions;

import dev.mednikov.accounting.authz.shared.exceptions.ItemAlreadyExistsException;

import java.util.UUID;

public final class RoleAlreadyExistsException extends ItemAlreadyExistsException {

    public RoleAlreadyExistsException(UUID organizationId, String name){
        super("Role", organizationId, name);
    }

}
