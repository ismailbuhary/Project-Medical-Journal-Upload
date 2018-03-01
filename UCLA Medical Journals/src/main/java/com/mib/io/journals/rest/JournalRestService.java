package com.mib.io.journals.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mib.io.journals.model.Journal;
import com.mib.io.journals.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mib.io.journals.dto.SubscriptionDTO;
import com.mib.io.journals.model.Category;
import com.mib.io.journals.model.Publisher;
import com.mib.io.journals.model.Subscription;
import com.mib.io.journals.model.User;
import com.mib.io.journals.repository.CategoryRepository;
import com.mib.io.journals.repository.PublisherRepository;
import com.mib.io.journals.service.CurrentUser;
import com.mib.io.journals.service.UserService;

@RestController
@RequestMapping("/rest/journals")
public class JournalRestService {

	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private JournalService journalService;

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryRepository categoryRepository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<Object> browse(@AuthenticationPrincipal Principal principal) {
		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
		return ResponseEntity.ok(journalService.listAll(activeUser.getUser()));
	}

	@RequestMapping(value = "/published", method = RequestMethod.GET)
	public List<Journal> publishedList(@AuthenticationPrincipal Principal principal) {
		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
		Optional<Publisher> publisher = publisherRepository.findByUser(activeUser.getUser());
		return journalService.publisherList(publisher.get());
	}

	@RequestMapping(value = "/unPublish/{id}", method = RequestMethod.DELETE)
	public void unPublish(@PathVariable("id") Long id, @AuthenticationPrincipal Principal principal) {
		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
		Optional<Publisher> publisher = publisherRepository.findByUser(activeUser.getUser());
		journalService.unPublish(publisher.get(), id);
	}

	@RequestMapping(value = "/subscriptions")
	public List<SubscriptionDTO> getUserSubscriptions(@AuthenticationPrincipal Principal principal) {
		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
		User persistedUser = userService.findById(activeUser.getId());
		List<Subscription> subscriptions = persistedUser.getSubscriptions();
		List<Category> categories = categoryRepository.findAll();
		List<SubscriptionDTO> subscriptionDTOs = new ArrayList<>(categories.size());
		categories.stream().forEach(c -> {
			SubscriptionDTO subscr = new SubscriptionDTO(c);
			Optional<Subscription> subscription = subscriptions.stream().filter(s -> s.getCategory().getId().equals(c.getId())).findFirst();
			subscr.setActive(subscription.isPresent());
			subscriptionDTOs.add(subscr);
		});
		return subscriptionDTOs;
	}

	@RequestMapping(value = "/subscribe/{categoryId}", method = RequestMethod.POST)
	public void subscribe(@PathVariable("categoryId") Long categoryId, @AuthenticationPrincipal Principal principal) {
		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
		User user = userService.findById(activeUser.getUser().getId());
		userService.subscribe(user, categoryId);
	}
}
