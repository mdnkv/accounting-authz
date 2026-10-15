package dev.mednikov.accounting.authz.authorities.services;

import dev.mednikov.accounting.authz.authorities.domain.AuthorityResponse;
import dev.mednikov.accounting.authz.authorities.mappers.AuthorityResponseMapper;
import dev.mednikov.accounting.authz.authorities.repositories.AuthorityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService{

    private final AuthorityRepository authorityRepository;
    private final AuthorityResponseMapper mapper;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository, AuthorityResponseMapper mapper) {
        this.authorityRepository = authorityRepository;
        this.mapper = mapper;
    }

    @Override
    public List<AuthorityResponse> getAllAuthorities() {
        return this.authorityRepository.findAll()
                .stream()
                .map(this.mapper)
                .toList();
    }

}
