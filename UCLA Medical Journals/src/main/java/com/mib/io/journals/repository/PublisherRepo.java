package com.mib.io.journals.repository;

import com.mib.io.journals.model.Publisher;
import com.mib.io.journals.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepo extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByUser(User user);

}
