package com.softserve.academy.event.entity;

import com.softserve.academy.event.entity.enums.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "surveys")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Filter(name = "surveyStatusField", condition = "status != :status")
@FilterDef(name = "surveyStatusField", parameters = @ParamDef(name = "status", type = "integer"))
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

    public Survey(Long id){
        this.id = id;
    }

}
