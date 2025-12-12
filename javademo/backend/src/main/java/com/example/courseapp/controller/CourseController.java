package com.example.courseapp.controller;

import com.example.courseapp.dto.CourseRequest;
import com.example.courseapp.dto.CourseResponse;
import com.example.courseapp.dto.Mapper;
import com.example.courseapp.model.Course;
import com.example.courseapp.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseResponse> list() {
        return courseService.findAll()
                .stream()
                .map(Mapper::toCourseResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CourseResponse get(@PathVariable Long id) {
        return Mapper.toCourseResponse(courseService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse create(@Valid @RequestBody CourseRequest request) {
        Course created = courseService.create(request);
        return Mapper.toCourseResponse(created);
    }

    @PutMapping("/{id}")
    public CourseResponse update(@PathVariable Long id, @Valid @RequestBody CourseRequest request) {
        Course updated = courseService.update(id, request);
        return Mapper.toCourseResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }
}
