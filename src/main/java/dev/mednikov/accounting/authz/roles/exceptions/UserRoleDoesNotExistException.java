package dev.mednikov.accounting.authz.roles.exceptions;

import java.util.UUID;

public final class UserRoleDoesNotExistException extends RuntimeException{

    public UserRoleDoesNotExistException(UUID userRoleId){
        super(String.format("User role %s does not exist", userRoleId.toString()));
    }

}
