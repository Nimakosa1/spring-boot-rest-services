package com.in28minutes.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.in28minutes.springboot.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
} 