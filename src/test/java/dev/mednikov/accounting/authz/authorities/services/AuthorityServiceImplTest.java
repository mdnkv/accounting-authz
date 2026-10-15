package dev.mednikov.accounting.authz.authorities.services;

import dev.mednikov.accounting.authz.authorities.domain.AuthorityResponse;
import dev.mednikov.accounting.authz.authorities.mappers.AuthorityResponseMapper;
import dev.mednikov.accounting.authz.authorities.mappers.AuthorityResponseMapperImpl;
import dev.mednikov.accounting.authz.authorities.models.Authority;
import dev.mednikov.accounting.authz.authorities.repositories.AuthorityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorityServiceImplTest {

    private final List<Authority> authorities = List.of(
            new Authority(UUID.randomUUID(), "journals:create"),
            new Authority(UUID.randomUUID(), "journals:update"),
            new Authority(UUID.randomUUID(), "journals:view"),
            new Authority(UUID.randomUUID(), "currencies:create"),
            new Authority(UUID.randomUUID(), "currencies:update"),
            new Authority(UUID.randomUUID(), "currencies:view")
    );

    @Mock private AuthorityRepository authorityRepository;
    private AuthorityServiceImpl service;

    @BeforeEach
    void setup(){
        AuthorityResponseMapper authorityResponseMapper = new AuthorityResponseMapperImpl();
        this.service = new AuthorityServiceImpl(authorityRepository, authorityResponseMapper);
    }

    @Test
    void getAllAuthoritiesTest(){
        when(authorityRepository.findAll()).thenReturn(authorities);

        List<AuthorityResponse> result = service.getAllAuthorities();
        assertThat(result).isNotNull().hasSize(6);
    }

}
