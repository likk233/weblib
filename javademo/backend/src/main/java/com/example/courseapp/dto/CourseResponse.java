package com.example.courseapp.dto;

public class CourseResponse {
    private Long id;
    private String code;
    private String title;
    private String description;
    private Integer capacity;

    public CourseResponse() {
    }

    public CourseResponse(Long id, String code, String title, String description, Integer capacity) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
