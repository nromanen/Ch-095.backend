package com.softserve.academy.event.entity;

import com.softserve.academy.event.entity.enums.Roles;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users")
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 8894016998310477567L;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = EMAIL_PATTERN)
    @Column(unique = true, length = 128)
    private String email;

    @Column(length = 100)
    private String password;

    private boolean active;

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    @Enumerated
    private Roles role = Roles.USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserSocial> userSocials = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Survey> surveys = new HashSet<>();

    public User() {
        this.active = false;
    }
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.active = false;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singleton(role);
//    }

    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return email;
    }

    public Roles getRole() {
        return role;
    }

    public boolean isEnabled() {
        return active;
    }

//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }



}
