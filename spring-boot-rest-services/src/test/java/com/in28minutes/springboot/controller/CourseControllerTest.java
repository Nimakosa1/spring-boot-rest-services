package com.in28minutes.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.in28minutes.springboot.model.Course;
import com.in28minutes.springboot.service.CourseService;
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
@WebMvcTest(CourseController.class)
@WithMockUser
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Test
    public void getAllCourses_ShouldReturnList() throws Exception {
        Course course1 = new Course("Spring Boot", "Learn Spring Boot", Arrays.asList("Step 1", "Step 2"));
        Course course2 = new Course("Spring MVC", "Learn Spring MVC", Arrays.asList("Step 1", "Step 2"));

        when(courseService.getAllCourses()).thenReturn(Arrays.asList(course1, course2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Spring Boot"))
                .andExpect(jsonPath("$[1].name").value("Spring MVC"));
    }

    @Test
    public void getCourseById_ShouldReturnCourse() throws Exception {
        Course course = new Course("Spring Boot", "Learn Spring Boot", Arrays.asList("Step 1", "Step 2"));
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Spring Boot"));
    }

    @Test
    public void createCourse_ShouldReturnCreated() throws Exception {
        Course course = new Course("Spring Boot", "Learn Spring Boot", Arrays.asList("Step 1", "Step 2"));
        when(courseService.createCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/courses")
                .with(csrf())
                .content("{\"name\":\"Spring Boot\",\"description\":\"Learn Spring Boot\",\"steps\":[\"Step 1\",\"Step 2\"]}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Spring Boot"));
    }

    @Test
    public void updateCourse_ShouldReturnUpdated() throws Exception {
        Course course = new Course("Spring Boot Updated", "Learn Spring Boot Updated", Arrays.asList("Step 1", "Step 2"));
        when(courseService.updateCourse(any(Long.class), any(Course.class)))
                .thenReturn(Optional.of(course));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/courses/1")
                .with(csrf())
                .content("{\"name\":\"Spring Boot Updated\",\"description\":\"Learn Spring Boot Updated\",\"steps\":[\"Step 1\",\"Step 2\"]}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Spring Boot Updated"));
    }

    @Test
    public void deleteCourse_ShouldReturnNoContent() throws Exception {
        when(courseService.deleteCourse(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/courses/1")
                .with(csrf()))
                .andExpect(status().isOk());
    }
} 