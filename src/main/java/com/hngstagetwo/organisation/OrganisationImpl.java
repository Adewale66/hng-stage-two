    package com.hngstagetwo.organisation;

import com.hngstagetwo.dtos.CreateOrg;
import com.hngstagetwo.dtos.OrgDto;
import com.hngstagetwo.errors.ResourceNotFound;
import com.hngstagetwo.users.User;
import com.hngstagetwo.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganisationImpl implements OrganisationService {
    private final OrganisationRepository organisationRepository;
    private final UsersService usersService;
    private final ModelMapper modelMapper;
    @Override
    public Organisation create(String firstName) {
        return Organisation.builder()
                .name(firstName + "'s" + " Organisation")
                .build();
    }

    @Override
    public void save(Organisation organisation) {
        organisationRepository.save(organisation);
    }

    @Override
    public ResponseEntity<Object> getAll(String username) {
        var user = usersService.findByEmail(username).orElseThrow(ResourceNotFound::new);
        var or = user.getOrganisations();
        System.out.println(or.size());
        Map<String, Object> orgs = new HashMap<>(){{
            put("organisations", or.stream().map(v -> modelMapper.map(v, OrgDto.class)));
        }};
        var response = new HashMap<>(){{
            put("status", "success");
            put("message", "All organisations found");
            put("data", orgs);
        }};
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getOne(UUID id, String username) {
        Organisation organisation = getOrg(id, username);
        OrgDto orgDto = modelMapper.map(organisation, OrgDto.class);
        var response = new HashMap<>(){{
            put("status", "success");
            put("message", "Organisation found");
            put("data", orgDto);
        }};
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> add(CreateOrg org, String username) {
        User user = usersService.findByEmail(username).orElseThrow();
        Organisation organisation = Organisation.builder()
                .name(org.name())
                .description(org.description())
                .build();
        user.getOrganisations().add(organisation);
        usersService.save(user);
        organisation.getMembers().add(user);
        organisationRepository.save(organisation);
        OrgDto orgDto = modelMapper.map(organisation, OrgDto.class);
        var response = new HashMap<>(){{
            put("status", "success");
            put("message", "Organisation created successfully");
            put("data", orgDto);
        }};
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> addUser(UUID id, String username, UUID userId) {
        var userToAdd = usersService.findById(userId).orElseThrow();
        Organisation organisation = getOrg(id, username);
        userToAdd.getOrganisations().add(organisation);
        organisation.getMembers().add(userToAdd);

        usersService.save(userToAdd);
        organisationRepository.save(organisation);


        return new ResponseEntity<>(new HashMap<>(){{
            put("status", "success");
            put("message", "User added to organisation successfully");
        }}, HttpStatus.OK);
    }

    private Organisation getOrg(UUID id, String username) {
        var user = usersService.findByEmail(username).orElseThrow(ResourceNotFound::new);
        var orgs = user.getOrganisations();

        for (Organisation org : orgs) {
            if (org.getOrgId().equals(id)) {
               return org;
            }
        }
        throw new ResourceNotFound();
    }

}
