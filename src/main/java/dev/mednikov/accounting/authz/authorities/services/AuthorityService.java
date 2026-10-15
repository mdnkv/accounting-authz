package dev.mednikov.accounting.authz.authorities.services;

import dev.mednikov.accounting.authz.authorities.domain.AuthorityResponse;

import java.util.List;

public interface AuthorityService {

    List<AuthorityResponse> getAllAuthorities();

}
