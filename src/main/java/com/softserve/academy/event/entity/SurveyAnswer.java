package com.softserve.academy.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "survey_answers")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SurveyAnswer implements Serializable {

    private static final long serialVersionUID = -1003597080168505177L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @ManyToOne
    private SurveyQuestion question;

    @Column
    @ManyToOne
    private Contact contact;

    @Column
    private String value;

}
