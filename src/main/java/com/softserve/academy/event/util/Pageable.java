package com.softserve.academy.event.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pageable {

    int size;
    int currentPage;
    int lastPage;
    Sort sort;

}
