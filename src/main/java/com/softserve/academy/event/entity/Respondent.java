package com.softserve.academy.event.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class Respondent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private Contact contact;

    @JoinColumn
    @ManyToOne
    private Anonym anonym;

    public Respondent(){}

    public Respondent(Contact contact){
        this.contact = contact;
        this.anonym = null;
    }

    public Respondent(Anonym anonym){
        this.contact = null;
        this.anonym = anonym;
    }
}
