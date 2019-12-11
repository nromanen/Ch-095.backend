package com.softserve.academy.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contacts")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contact implements Serializable {

    private static final long serialVersionUID = 6144143087405979275L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(length = 128)
    private String name;

    @Column(length = 128, nullable = false)
    private String email;

    @ManyToMany(mappedBy = "contacts")
    private Set<Survey> surveys = new HashSet<>();

}
