package com.in28minutes.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.in28minutes.springboot.StudentApplication;
import com.in28minutes.springboot.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StudentApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser
public class CourseControllerIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void setup() {
        headers.setBasicAuth("user1", "secret1");
    }

    @Test
    public void testGetAllCourses() {
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/courses"),
                HttpMethod.GET, entity, String.class);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testCreateCourse() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        Course course = new Course("Spring Boot", "Learn Spring Boot", List.of("Step 1", "Step 2"));

        HttpEntity<Course> entity = new HttpEntity<>(course, headers);

        ResponseEntity<Course> response = restTemplate.exchange(
                createURLWithPort("/api/courses"),
                HttpMethod.POST, entity, Course.class);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getHeaders().getLocation().toString().contains("/api/courses/"));
    }

    @Test
    public void testGetCourseById() {
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Course> response = restTemplate.exchange(
                createURLWithPort("/api/courses/1"),
                HttpMethod.GET, entity, Course.class);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateCourse() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        Course course = new Course("Spring Boot Updated", "Learn Spring Boot Updated", List.of("Step 1", "Step 2"));

        HttpEntity<Course> entity = new HttpEntity<>(course, headers);

        ResponseEntity<Course> response = restTemplate.exchange(
                createURLWithPort("/api/courses/1"),
                HttpMethod.PUT, entity, Course.class);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testDeleteCourse() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                createURLWithPort("/api/courses/1"),
                HttpMethod.DELETE, entity, Void.class);

        assertEquals(200, response.getStatusCodeValue());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
} 