package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.UserDto;
import com.softserve.academy.event.entity.User;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import org.springframework.stereotype.Component;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-01-08T10:50:33+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.3 (JetBrains s.r.o)"
)
*/
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User userDtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDto.getId() );
        user.setEmail( userDto.getEmail() );
        user.setPassword( userDto.getPassword() );
        user.setActive( userDto.isActive() );
        if ( userDto.getCreationDate() != null ) {
            user.setCreationDate( LocalDateTime.ofInstant( userDto.getCreationDate().toInstant(), ZoneOffset.UTC ).toLocalDate() );
        }
        user.setRole( userDto.getRole() );

        return user;
    }

    @Override
    public UserDto userToDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setEmail( user.getEmail() );
        userDto.setPassword( user.getPassword() );
        userDto.setActive( user.isActive() );
        if ( user.getCreationDate() != null ) {
            userDto.setCreationDate( Date.from( user.getCreationDate().atStartOfDay( ZoneOffset.UTC ).toInstant() ) );
        }
        userDto.setRole( user.getRole() );

        return userDto;
    }
}
