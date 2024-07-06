package com.hngstagetwo.organisation;

import com.hngstagetwo.dtos.CreateOrg;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface OrganisationService {
    void save(Organisation organisation);

    Organisation create(String firstName);

    ResponseEntity<Object> getAll(String username);

    ResponseEntity<Object> getOne(UUID id, String username);

    ResponseEntity<Object> add(CreateOrg org, String username);

    ResponseEntity<Object> addUser(UUID orgId, UUID userId);
}
