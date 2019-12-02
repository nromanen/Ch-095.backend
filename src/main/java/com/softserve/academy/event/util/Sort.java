package com.softserve.academy.event.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sort {

    private Direction direction;
    private String field;
    private Sort nextSort;

    public Sort(Direction direction, String field) {
        this.direction = direction;
        this.field = field;
    }

    public Sort setNextSort(Sort sort){
        this.nextSort = sort;
        return this;
    }

    public enum Direction{
        ASC,
        DESC;
    }

}
