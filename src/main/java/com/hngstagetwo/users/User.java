package com.hngstagetwo.users;

import com.hngstagetwo.organisation.Organisation;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity()
@Getter()
@Setter()
@Builder()
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column()
    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable (
            name = "user_organisations",
            joinColumns = @JoinColumn(name = "org_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<Organisation> organisations;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getUsername() {
        return getEmail();
    }
}
