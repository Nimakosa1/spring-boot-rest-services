package com.in28minutes.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.in28minutes.springboot.model.Course;
import com.in28minutes.springboot.repository.CourseRepository;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> updateCourse(Long id, Course courseDetails) {
        return courseRepository.findById(id)
            .map(course -> {
                course.setName(courseDetails.getName());
                course.setDescription(courseDetails.getDescription());
                course.setSteps(courseDetails.getSteps());
                return courseRepository.save(course);
            });
    }

    public boolean deleteCourse(Long id) {
        return courseRepository.findById(id)
            .map(course -> {
                courseRepository.delete(course);
                return true;
            })
            .orElse(false);
    }
} 