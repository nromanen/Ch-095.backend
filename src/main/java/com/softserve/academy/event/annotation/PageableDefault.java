package com.softserve.academy.event.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageableDefault {

    int size() default 12;

    int page() default 0;

}
