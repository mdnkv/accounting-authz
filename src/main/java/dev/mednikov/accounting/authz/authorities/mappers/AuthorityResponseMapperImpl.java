package dev.mednikov.accounting.authz.authorities.mappers;

import dev.mednikov.accounting.authz.authorities.domain.AuthorityResponse;
import dev.mednikov.accounting.authz.authorities.models.Authority;
import org.springframework.stereotype.Component;

@Component
public final class AuthorityResponseMapperImpl implements AuthorityResponseMapper {

    @Override
    public AuthorityResponse apply(Authority authority) {
        return new AuthorityResponse(authority.getId(), authority.getName());
    }

}
