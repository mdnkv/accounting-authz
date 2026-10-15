package dev.mednikov.accounting.authz.roles.exceptions;

import dev.mednikov.accounting.authz.shared.exceptions.ItemDoesNotExistException;

import java.util.UUID;

public final class UserRoleDoesNotExistException extends ItemDoesNotExistException {

    public UserRoleDoesNotExistException(UUID userRoleId){
       super("User role", userRoleId);
    }

}
