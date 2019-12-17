package com.softserve.academy.event.entity;

import com.softserve.academy.event.entity.embeded.QuestionContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "survey_answers")
@EqualsAndHashCode(of = {"questionContact"})
@NoArgsConstructor
@AllArgsConstructor
@Data
@NamedQuery(name="SurveyAnswer.findByQuestionId",
        query="from SurveyAnswer where questionId = :questionId ")
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
