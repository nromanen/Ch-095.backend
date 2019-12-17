package com.softserve.academy.event.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "survey_contacts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SurveyContactConnector implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Survey survey;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Contact contact;

    @Column(name = "enable")
    private boolean enable;
}
