package com.softserve.academy.event.entity;

import com.softserve.academy.event.entity.embeded.QuestionContact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
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

    @EmbeddedId
    private QuestionContact questionContact;

    private String value;

}
