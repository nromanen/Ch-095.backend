package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.Roles;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SimpleUserDTO {

    private Long id;
    private String email;
    private String password;
    private boolean active;
    private Date creationDate;
    private Roles role;

    private SimpleUserDTO() {
    }

    public static SimpleUserDTO toSimpleUser(User user) {
        SimpleUserDTO dto = new SimpleUserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setActive(user.isActive());
        dto.setCreationDate(user.getCreationDate());
        dto.setRole(user.getRole());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
