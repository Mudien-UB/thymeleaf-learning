package com.hehe.belajar_thymeleaf.repository;

import com.hehe.belajar_thymeleaf.model.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentsRepository extends JpaRepository<Students, Long> {
    boolean existsByEmail(String email);
}
