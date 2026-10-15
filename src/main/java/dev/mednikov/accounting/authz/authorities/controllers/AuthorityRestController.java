package dev.mednikov.accounting.authz.authorities.controllers;

import dev.mednikov.accounting.authz.authorities.domain.AuthorityResponse;
import dev.mednikov.accounting.authz.authorities.services.AuthorityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authorities")
public class AuthorityRestController {

    private final AuthorityService authorityService;


    public AuthorityRestController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping
    public @ResponseBody List<AuthorityResponse> getAllAuthorities(){
        return this.authorityService.getAllAuthorities();
    }

}
