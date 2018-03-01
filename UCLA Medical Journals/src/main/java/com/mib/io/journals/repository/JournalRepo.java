package com.mib.io.journals.repository;

import com.mib.io.journals.model.Journal;
import com.mib.io.journals.model.Publisher;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface JournalRepo extends CrudRepository<Journal, Long> {

    Collection<Journal> findByPublisher(Publisher publisher);

    List<Journal> findByCategoryIdIn(List<Long> ids);

}
