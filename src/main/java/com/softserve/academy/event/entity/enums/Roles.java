package com.softserve.academy.event.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    USER,
    MANAGER;

    @Override
    public String getAuthority() {
        return name();
    }

}
