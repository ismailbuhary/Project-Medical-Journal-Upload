package com.mib.io.journals.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mib.io.journals.model.Category;
import com.mib.io.journals.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mib.io.journals.model.Subscription;
import com.mib.io.journals.model.User;
import com.mib.io.journals.repository.UserRepo;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepo userRepository;

	@Autowired
	private CategoryRepo categoryRepository;

	@Autowired
	public UserServiceImpl(UserRepo userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Optional<User> getUserByLoginName(String loginName) {
		return Optional.ofNullable(userRepository.findByLoginName(loginName));
	}

	@Override
	public void subscribe(User user, Long categoryId) {
		List<Subscription> subscriptions;
		subscriptions = user.getSubscriptions();
		if (subscriptions == null) {
			subscriptions = new ArrayList<>();
		}
		Optional<Subscription> subscr = subscriptions.stream()
				.filter(s -> s.getCategory().getId().equals(categoryId)).findFirst();
		if (!subscr.isPresent()) {
			Subscription s = new Subscription();
			s.setUser(user);
			Category category = categoryRepository.findOne(categoryId);
			if(category == null) {
				throw new ServiceException("Category not found");
			}
			s.setCategory(category);
			subscriptions.add(s);
			userRepository.save(user);
		}
	}

	@Override
	public User findById(Long id) {
		return userRepository.findOne(id);
	}

}
