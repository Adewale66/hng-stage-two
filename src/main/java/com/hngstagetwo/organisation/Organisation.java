package com.hngstagetwo.organisation;

import com.hngstagetwo.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "organisations")
public class Organisation {

    @ManyToMany(mappedBy = "organisations")
    Set<User> members;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orgId;
    @Column(nullable = false)
    private String name;
    @Column
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organisation that = (Organisation) o;
        return Objects.equals(orgId, that.orgId);
    }

}
