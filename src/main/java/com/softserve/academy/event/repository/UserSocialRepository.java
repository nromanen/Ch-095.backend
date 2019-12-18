package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.UserSocial;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSocialRepository extends BasicRepository<UserSocial, Long> {
}
