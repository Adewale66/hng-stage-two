package com.hngstagetwo.organisation;

import com.hngstagetwo.dtos.CreateOrg;
import com.hngstagetwo.dtos.IdDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/organisations")
@RequiredArgsConstructor
public class OrganisationController {

    private final OrganisationService organisationService;

    @GetMapping
    public ResponseEntity<Object> getAll(Principal principal) {
        return organisationService.getAll(principal.getName());
    }

    @GetMapping("{ordId}")
    public ResponseEntity<Object> getOne(@PathVariable("ordId") UUID id, Principal principal) {
        return organisationService.getOne(id, principal.getName());
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid CreateOrg createOrg, Principal principal) {
        return organisationService.add(createOrg, principal.getName());
    }

    @PostMapping("{orgId}/users")
    public ResponseEntity<Object> addUser(@PathVariable("orgId") UUID orgId, @RequestBody @Valid IdDto userId) {
        return organisationService.addUser(orgId, UUID.fromString(userId.userId()));
    }
}