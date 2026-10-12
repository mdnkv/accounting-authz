package dev.mednikov.accounting.authz.authorities.mappers;

import dev.mednikov.accounting.authz.authorities.domain.AuthorityResponse;
import dev.mednikov.accounting.authz.authorities.models.Authority;

import java.util.function.Function;

public interface AuthorityResponseMapper extends Function<Authority, AuthorityResponse> {
}
