package com.in28minutes.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.in28minutes.springboot.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
} 