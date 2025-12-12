package com.example.courseapp.repository;

import com.example.courseapp.model.Enrollment;
import com.example.courseapp.model.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourseId(Long courseId);
    long countByCourseId(Long courseId);
}
