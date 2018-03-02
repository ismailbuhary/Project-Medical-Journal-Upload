package com.mib.io.journals.controller;

import com.mib.io.journals.Application;
import com.mib.io.journals.model.Journal;
import com.mib.io.journals.model.Publisher;
import com.mib.io.journals.repository.PublisherRepo;
import com.mib.io.journals.service.CurrentUser;
import com.mib.io.journals.service.IJournalService;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@EnableAutoConfiguration
public class PublisherController {


    static final Logger LOG = LoggerFactory.getLogger(PublisherController.class);


	@Autowired
	private PublisherRepo publisherRepository;

	@Autowired
	private IJournalService journalService;

	@RequestMapping(method = RequestMethod.GET, value = "/publisher/publish")
	public String provideUploadInfo(Model model) {
		return "publisher/publish";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/publisher/publish")
	@PreAuthorize("hasRole('PUBLISHER')")
	public String handleFileUpload(@RequestParam("name") String name, @RequestParam("category")Long categoryId, @RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes, @AuthenticationPrincipal Principal principal) {

		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
		Optional<Publisher> publisher = publisherRepository.findByUser(activeUser.getUser());

		String uuid = UUID.randomUUID().toString();
		File dir = new File(getDirectory(publisher.get().getId()));
		createDirectoryIfNotExist(dir);

		File f = new File(getFileName(publisher.get().getId(), uuid));
		if (!file.isEmpty()) {
			try {
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f));
				FileCopyUtils.copy(file.getInputStream(), stream);
				stream.close();
				Journal journal = new Journal();
				journal.setUuid(uuid);
				journal.setName(name);
				journalService.publish(publisher.get(), journal, categoryId);
				return "redirect:/publisher/browse";
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message",
						"You failed to publish " + name + " => " + e.getMessage());
			}
		} else {
			redirectAttributes.addFlashAttribute("message",
					"You failed to upload " + name + " because the file was empty");
		}

		return "redirect:/publisher/publish";
	}

	private boolean createDirectoryIfNotExist(File dir) {
		if (!dir.exists()) {
			boolean created = dir.mkdirs();
			if (!created) {
				return false;
			}
		}
		return true;
	}

	public static String getFileName(long publisherId, String uuid) {
		return getDirectory(publisherId) + "/" + uuid + ".pdf";
	}

	public static String getDirectory(long publisherId) {
		return Application.ROOT + "/" + publisherId;
	}

}