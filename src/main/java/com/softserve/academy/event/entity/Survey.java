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

import static com.softserve.academy.event.util.Constants.*;

@Entity
@Table(name = "surveys")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(of = {"title"})
@Filters({
        @Filter(name = SURVEY_STATUS_FILTER_NAME, condition = "status = :" + SURVEY_STATUS_FILTER_ARGUMENT),
        @Filter(name = SURVEY_DEFAULT_FILTER_NAME, condition = "status != " + SURVEY_DEFAULT_TEMPLATE_NUMBER)
})
@FilterDefs({
        @FilterDef(name = SURVEY_STATUS_FILTER_NAME, parameters = @ParamDef(name = SURVEY_STATUS_FILTER_ARGUMENT, type = "integer")),
        @FilterDef(name = SURVEY_DEFAULT_FILTER_NAME)
})
public class Survey implements Serializable {

    private static final long serialVersionUID = 2943648242656547434L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String title;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    @Enumerated
    private SurveyStatus status = SurveyStatus.NON_ACTIVE;

    private String imageUrl;

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
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "survey_id")
    private List<SurveyQuestion> questions = new ArrayList<>();


    public Survey(Long id) {
        this.id = id;
    }

    public void addQuestion(SurveyQuestion surveyQuestion) {
        questions.add(surveyQuestion);
        surveyQuestion.setSurvey(this);
    }

}
