package com.softserve.academy.event.entity;

import com.softserve.academy.event.entity.enums.OauthType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_socials")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserSocial implements Serializable {

    private static final long serialVersionUID = 2075478469881612722L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private OauthType type;

    @Column(unique = true, length = 128, nullable = false)
    private String email;

    @ManyToOne
    private User user;

}
