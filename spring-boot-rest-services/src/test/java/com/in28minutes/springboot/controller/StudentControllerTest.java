package com.in28minutes.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Arrays;
import java.util.Optional;

import com.in28minutes.springboot.model.Student;
import com.in28minutes.springboot.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StudentController.class)
@WithMockUser
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    public void getAllStudents_ShouldReturnList() throws Exception {
        Student student1 = new Student("John Doe", "john@example.com", "Computer Science");
        Student student2 = new Student("Jane Doe", "jane@example.com", "Mathematics");

        when(studentService.getAllStudents()).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    public void getStudentById_ShouldReturnStudent() throws Exception {
        Student student = new Student("John Doe", "john@example.com", "Computer Science");
        when(studentService.getStudentById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void createStudent_ShouldReturnCreated() throws Exception {
        Student student = new Student("John Doe", "john@example.com", "Computer Science");
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/students")
                .with(csrf())
                .content("{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"major\":\"Computer Science\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void updateStudent_ShouldReturnUpdated() throws Exception {
        Student student = new Student("John Doe Updated", "john@example.com", "Physics");
        when(studentService.updateStudent(any(Long.class), any(Student.class)))
                .thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/students/1")
                .with(csrf())
                .content("{\"name\":\"John Doe Updated\",\"email\":\"john@example.com\",\"major\":\"Physics\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe Updated"));
    }

    @Test
    public void deleteStudent_ShouldReturnNoContent() throws Exception {
        when(studentService.deleteStudent(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/students/1")
                .with(csrf()))
                .andExpect(status().isOk());
    }
}
