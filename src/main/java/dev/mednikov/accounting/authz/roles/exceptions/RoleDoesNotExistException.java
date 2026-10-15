package dev.mednikov.accounting.authz.roles.exceptions;

import dev.mednikov.accounting.authz.shared.exceptions.ItemDoesNotExistException;

import java.util.UUID;

public final class RoleDoesNotExistException extends ItemDoesNotExistException {

    public RoleDoesNotExistException(UUID id){
        super("Role", id);
    }

}
