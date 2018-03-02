package com.mib.io.journals.service;

import com.mib.io.journals.model.Journal;
import com.mib.io.journals.model.Publisher;
import com.mib.io.journals.model.User;

import java.util.List;

public interface IJournalService {

	List<Journal> listAll(User user);

	List<Journal> publisherList(Publisher publisher);

	Journal publish(Publisher publisher, Journal journal, Long categoryId);

	void unPublish(Publisher publisher, Long journalId);
}
