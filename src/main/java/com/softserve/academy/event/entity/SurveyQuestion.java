package com.softserve.academy.event.entity;

import com.softserve.academy.event.entity.enums.EventQuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "survey_questions")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SurveyQuestion implements Serializable {

    private static final long serialVersionUID = -2673922858877977323L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Survey survey;

    @Column(nullable = false)
    private String question;

    @Enumerated
    private EventQuestionType type;

    @Column(nullable = false)
    private String answers;

    private boolean required;

}