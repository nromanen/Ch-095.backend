package com.softserve.academy.event.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.softserve.academy.event.util.Constants.*;

@Entity
@Table(name = "surveys")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Data
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

}
