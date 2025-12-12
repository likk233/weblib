package com.example.courseapp.dto;

import com.example.courseapp.model.Course;
import com.example.courseapp.model.Student;

public final class Mapper {
    private Mapper() {
    }

    public static StudentResponse toStudentResponse(Student student) {
        if (student == null) {
            return null;
        }
        return new StudentResponse(student.getId(), student.getName(), student.getEmail());
    }

    public static CourseResponse toCourseResponse(Course course) {
        if (course == null) {
            return null;
        }
        return new CourseResponse(course.getId(), course.getCode(), course.getTitle(), course.getDescription(), course.getCapacity());
    }
}
