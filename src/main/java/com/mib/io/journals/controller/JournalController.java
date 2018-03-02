package com.mib.io.journals.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import com.mib.io.journals.model.Journal;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mib.io.journals.model.Category;
import com.mib.io.journals.model.Subscription;
import com.mib.io.journals.model.User;
import com.mib.io.journals.repository.JournalRepo;
import com.mib.io.journals.repository.UserRepo;
import com.mib.io.journals.service.CurrentUser;

@Controller
@EnableAutoConfiguration
public class JournalController {

	@Autowired
	private JournalRepo journalRepository;

	@Autowired
	private UserRepo userRepository;

	@ResponseBody
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity renderDocument(@AuthenticationPrincipal Principal principal, @PathVariable("id") Long id)
			throws IOException {
		Journal journal = journalRepository.findOne(id);
		Category category = journal.getCategory();
		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
		User user = userRepository.findOne(activeUser.getUser().getId());
		List<Subscription> subscriptions = user.getSubscriptions();
		Optional<Subscription> subscription = subscriptions.stream()
				.filter(s -> s.getCategory().getId().equals(category.getId())).findFirst();
		if (subscription.isPresent() || journal.getPublisher().getId().equals(user.getId())) {
			File file = new File(PublisherController.getFileName(journal.getPublisher().getId(), journal.getUuid()));
			InputStream in = new FileInputStream(file);
			return ResponseEntity.ok(IOUtils.toByteArray(in));
		} else {
			return ResponseEntity.notFound().build();
		}

	}
}
