package com.softserve.academy.event.annotation;

import com.softserve.academy.event.util.Sort;

import javax.validation.constraints.Size;
import java.lang.annotation.*;


/**
 * Annotation use params :
 * size
 * page
 * sort
 * direction
 * If you need same params
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageableDefault {

    int size() default 12;

    int page() default 0;

    String[] sort() default {};

    Sort.Direction direction() default Sort.Direction.ASC;

    @Size(min = 4, max = 4)
    String[] params() default {"size", "page", "sort", "direction"};

}
