package com.mib.io.journals.service;

import java.util.Optional;

import com.mib.io.journals.model.User;

public interface IUserService {

    Optional<User> getUserByLoginName(String loginName);

    void subscribe(User user, Long categoryId);

    User findById(Long id);

}