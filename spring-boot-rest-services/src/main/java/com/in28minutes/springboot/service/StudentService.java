package com.in28minutes.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.in28minutes.springboot.model.Student;
import com.in28minutes.springboot.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> updateStudent(Long id, Student studentDetails) {
        return studentRepository.findById(id)
            .map(student -> {
                student.setName(studentDetails.getName());
                student.setEmail(studentDetails.getEmail());
                student.setMajor(studentDetails.getMajor());
                return studentRepository.save(student);
            });
    }

    public boolean deleteStudent(Long id) {
        return studentRepository.findById(id)
            .map(student -> {
                studentRepository.delete(student);
                return true;
            })
            .orElse(false);
    }
}