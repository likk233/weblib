package com.example.courseapp.controller;

import com.example.courseapp.dto.CourseResponse;
import com.example.courseapp.dto.EnrollmentRequest;
import com.example.courseapp.dto.Mapper;
import com.example.courseapp.dto.StudentResponse;
import com.example.courseapp.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/enrollments")
    @ResponseStatus(HttpStatus.CREATED)
    public void enroll(@Valid @RequestBody EnrollmentRequest request) {
        enrollmentService.enroll(request);
    }

    @DeleteMapping("/enrollments")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void drop(@Valid @RequestBody EnrollmentRequest request) {
        enrollmentService.drop(request);
    }

    @GetMapping("/students/{id}/courses")
    public List<CourseResponse> coursesForStudent(@PathVariable Long id) {
        return enrollmentService.listCoursesForStudent(id)
                .stream()
                .map(Mapper::toCourseResponse)
                .toList();
    }

    @GetMapping("/courses/{id}/students")
    public List<StudentResponse> studentsForCourse(@PathVariable Long id) {
        return enrollmentService.listStudentsForCourse(id)
                .stream()
                .map(Mapper::toStudentResponse)
                .toList();
    }
}
