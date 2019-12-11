package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.Roles;
import com.softserve.academy.event.validation.EmailValidator;
import com.softserve.academy.event.validation.PasswordValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String matchingPassword;
    private boolean active;
    private Date creationDate;
    private Roles role;

//    private UserDto() {
//    }

    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = EmailValidator.class)
    @Documented
    public @interface ValidEmail {
        String message() default "Invalid email";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = PasswordValidator.class)
    @Documented
    public @interface PasswordMatches {
        String message() default "Password don't match";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
}
