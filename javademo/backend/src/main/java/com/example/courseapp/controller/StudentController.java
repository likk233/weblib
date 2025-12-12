package com.example.courseapp.controller;

import com.example.courseapp.dto.Mapper;
import com.example.courseapp.dto.StudentRequest;
import com.example.courseapp.dto.StudentResponse;
import com.example.courseapp.model.Student;
import com.example.courseapp.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentResponse> list() {
        return studentService.findAll()
                .stream()
                .map(Mapper::toStudentResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public StudentResponse get(@PathVariable Long id) {
        return Mapper.toStudentResponse(studentService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse create(@Valid @RequestBody StudentRequest request) {
        Student created = studentService.create(request);
        return Mapper.toStudentResponse(created);
    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        Student updated = studentService.update(id, request);
        return Mapper.toStudentResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }
}
