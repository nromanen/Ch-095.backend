package com.softserve.academy.event.entity.embeded;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class QuestionContact implements Serializable {

    private static final long serialVersionUID = -1951413322435119656L;

    private Long questionId;
    private Long contactId;

}
