package com.example.courseapp.service;

import com.example.courseapp.dto.StudentRequest;
import com.example.courseapp.exception.BadRequestException;
import com.example.courseapp.exception.ResourceNotFoundException;
import com.example.courseapp.model.Student;
import com.example.courseapp.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
    }

    public Student create(StudentRequest request) {
        studentRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
            throw new BadRequestException("Email already used: " + request.getEmail());
        });
        Student student = new Student(request.getName(), request.getEmail());
        return studentRepository.save(student);
    }

    public Student update(Long id, StudentRequest request) {
        Student student = findById(id);
        studentRepository.findByEmail(request.getEmail())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new BadRequestException("Email already used: " + request.getEmail());
                });

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        return studentRepository.save(student);
    }

    public void delete(Long id) {
        Student student = findById(id);
        studentRepository.delete(student);
    }
}
