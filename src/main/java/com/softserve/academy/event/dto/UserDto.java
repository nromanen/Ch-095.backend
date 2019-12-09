package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.Roles;
import com.softserve.academy.event.validation.EmailValidator;
import com.softserve.academy.event.validation.PasswordValidator;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;
import java.util.Date;

@Getter
@Setter
@UserDto.PasswordMatches
public class UserDto {

    private Long id;
    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;
    private boolean active;
    private Date creationDate;
    private Roles role;

//    private UserDto() {
//    }

    public static UserDto toSimpleUser(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setActive(user.isActive());
        dto.setCreationDate(user.getCreationDate());
        dto.setRole(user.getRole());
        return dto;
    }

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
