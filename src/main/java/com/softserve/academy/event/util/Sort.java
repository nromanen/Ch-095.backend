package com.softserve.academy.event.util;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Sort {

    private Direction direction;
    private String[] fields;

    public static Sort from(Direction direction, String[] fields){
        return new Sort(direction,fields);
    }

    public enum Direction{
        ASC,
        DESC;
    }

}
