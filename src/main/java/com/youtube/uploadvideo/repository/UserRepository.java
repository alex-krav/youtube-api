package com.youtube.uploadvideo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.youtube.uploadvideo.model.User;

public interface UserRepository extends Repository<User, Integer> {

    @Query("SELECT user FROM User user WHERE user.token =:token")
    @Transactional(readOnly = true)
    User findByToken(@Param("token") String token);
}
