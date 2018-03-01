package com.mib.io.journals.service;

import com.mib.io.journals.model.Role;
import com.mib.io.journals.model.User;
import com.mib.io.journals.model.Subscription;
import org.springframework.security.core.authority.AuthorityUtils;


@Service("getUser")
public class CurrentUser extends org.springframework.security.core.userdetails.User {

	private User user;
	private Subscription subscription;

	public CurrentUser(User user) {
		super(user.getLoginName(), user.getPwd(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Long getId() {
		return user.getId();
	}

	public Role getRole() {
		return user.getRole();
	}

}