package com.example.courseapp.service;

import com.example.courseapp.dto.EnrollmentRequest;
import com.example.courseapp.exception.BadRequestException;
import com.example.courseapp.exception.ResourceNotFoundException;
import com.example.courseapp.model.Course;
import com.example.courseapp.model.Enrollment;
import com.example.courseapp.model.EnrollmentId;
import com.example.courseapp.model.Student;
import com.example.courseapp.repository.CourseRepository;
import com.example.courseapp.repository.EnrollmentRepository;
import com.example.courseapp.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Enrollment enroll(EnrollmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + request.getCourseId()));

        EnrollmentId id = new EnrollmentId(student.getId(), course.getId());
        enrollmentRepository.findById(id).ifPresent(existing -> {
            throw new BadRequestException("Student already enrolled in course");
        });

        long enrolledCount = enrollmentRepository.countByCourseId(course.getId());
        if (course.getCapacity() != null && enrolledCount >= course.getCapacity()) {
            throw new BadRequestException("Course is at capacity");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setId(id);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        return enrollmentRepository.save(enrollment);
    }

    public void drop(EnrollmentRequest request) {
        EnrollmentId id = new EnrollmentId(request.getStudentId(), request.getCourseId());
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        enrollmentRepository.delete(enrollment);
    }

    @Transactional(readOnly = true)
    public List<Course> listCoursesForStudent(Long studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Student> listStudentsForCourse(Long courseId) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));
        return enrollmentRepository.findByCourseId(courseId)
                .stream()
                .map(Enrollment::getStudent)
                .collect(Collectors.toList());
    }
}
