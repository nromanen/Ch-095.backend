package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String password;
//    private String matchingPassword;
    private boolean active;
    private Date creationDate;
    private Roles role;

}
