package com.hngstagetwo.organisation;

import com.hngstagetwo.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "organisations")
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orgId;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToMany(mappedBy = "organisations")
    Set<User> members;
}
