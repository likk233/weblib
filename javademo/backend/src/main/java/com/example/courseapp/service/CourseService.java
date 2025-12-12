package com.example.courseapp.service;

import com.example.courseapp.dto.CourseRequest;
import com.example.courseapp.exception.BadRequestException;
import com.example.courseapp.exception.ResourceNotFoundException;
import com.example.courseapp.model.Course;
import com.example.courseapp.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + id));
    }

    public Course create(CourseRequest request) {
        courseRepository.findByCode(request.getCode()).ifPresent(existing -> {
            throw new BadRequestException("Course code already used: " + request.getCode());
        });
        Course course = new Course(request.getCode(), request.getTitle(), request.getDescription(), request.getCapacity());
        return courseRepository.save(course);
    }

    public Course update(Long id, CourseRequest request) {
        Course course = findById(id);
        courseRepository.findByCode(request.getCode())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new BadRequestException("Course code already used: " + request.getCode());
                });

        course.setCode(request.getCode());
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCapacity(request.getCapacity());
        return courseRepository.save(course);
    }

    public void delete(Long id) {
        Course course = findById(id);
        courseRepository.delete(course);
    }
}
