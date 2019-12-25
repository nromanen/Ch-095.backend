package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.UserSocial;

public interface UserSocialRepository extends BasicRepository<UserSocial, Long> {
    Long indexOf(UserSocial entity) throws IndexOutOfBoundsException;
}
