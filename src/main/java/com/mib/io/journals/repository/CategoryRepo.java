package com.mib.io.journals.repository;

import com.mib.io.journals.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
