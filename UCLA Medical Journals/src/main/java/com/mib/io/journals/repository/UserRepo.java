package com.mib.io.journals.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mib.io.journals.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByLoginName(String loginName);

}
