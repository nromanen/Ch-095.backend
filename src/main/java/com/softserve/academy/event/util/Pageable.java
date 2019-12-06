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

    int currentPage;
    int lastPage;
    int size;
    Sort sort;

}
