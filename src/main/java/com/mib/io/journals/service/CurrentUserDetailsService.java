package com.mib.io.journals.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mib.io.journals.model.User;

@Service
public class CurrentUserDetailsService implements UserDetailsService {

	private final IUserService userService;

	@Autowired
	public CurrentUserDetailsService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public CurrentUser loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.getUserByLoginName(email).orElseThrow(
				() -> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));
		return new CurrentUser(user);
	}

}
