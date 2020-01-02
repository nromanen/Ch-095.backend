package com.softserve.academy.event.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.softserve.academy.event.util.Constants.*;

@Entity
@Table(name = "surveys")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(of = {"title"})
@Filter(name = SURVEY_STATUS_FILTER_NAME, condition = "status = :" + SURVEY_STATUS_FILTER_ARGUMENT)
@Filter(name = SURVEY_DEFAULT_FILTER_NAME, condition = "status != " + SURVEY_DEFAULT_TEMPLATE_NUMBER)
@FilterDef(name = SURVEY_STATUS_FILTER_NAME, parameters = @ParamDef(name = SURVEY_STATUS_FILTER_ARGUMENT, type = "integer"))
@FilterDef(name = SURVEY_DEFAULT_FILTER_NAME)
public class Survey implements Serializable {

    private static final long serialVersionUID = 2943648242656547434L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String title;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate creationDate = LocalDate.now();

    @Enumerated
    @Column(nullable = false)
    private SurveyStatus status = SurveyStatus.NON_ACTIVE;

    private String imageUrl;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active = true;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "survey_contacts",
            joinColumns = {@JoinColumn(name = "survey_id")},
            inverseJoinColumns = {@JoinColumn(name = "contact_id")}
    )
    private Set<Contact> contacts = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "survey_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<SurveyQuestion> surveyQuestions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "survey")
    private Set<SurveyContact> surveyContacts = new HashSet<>();

    public Survey(Long id) {
        this.id = id;
    }

    public void addQuestion(SurveyQuestion surveyQuestion) {
        surveyQuestions.add(surveyQuestion);
        surveyQuestion.setSurvey(this);
    }

}
