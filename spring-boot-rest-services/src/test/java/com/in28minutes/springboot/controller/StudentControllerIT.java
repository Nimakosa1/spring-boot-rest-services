package com.in28minutes.springboot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.in28minutes.springboot.StudentApplication;
import com.in28minutes.springboot.model.Student;
import org.json.JSONException;
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
public class StudentControllerIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void setup() {
        headers.setBasicAuth("user1", "secret1");
    }

    @Test
    public void testGetAllStudents() {
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/students"),
                HttpMethod.GET, entity, String.class);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testCreateStudent() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        Student student = new Student("John Doe", "john@example.com", "Computer Science");

        HttpEntity<Student> entity = new HttpEntity<>(student, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                createURLWithPort("/api/students"),
                HttpMethod.POST, entity, Student.class);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getHeaders().getLocation().toString().contains("/api/students/"));
    }

    @Test
    public void testGetStudentById() {
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                createURLWithPort("/api/students/1"),
                HttpMethod.GET, entity, Student.class);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateStudent() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        Student student = new Student("John Doe Updated", "john@example.com", "Physics");

        HttpEntity<Student> entity = new HttpEntity<>(student, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                createURLWithPort("/api/students/1"),
                HttpMethod.PUT, entity, Student.class);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testDeleteStudent() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                createURLWithPort("/api/students/1"),
                HttpMethod.DELETE, entity, Void.class);

        assertEquals(200, response.getStatusCodeValue());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
