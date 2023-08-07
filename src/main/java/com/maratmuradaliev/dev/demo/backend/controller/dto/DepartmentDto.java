package com.maratmuradaliev.dev.demo.backend.controller.dto;

import java.util.List;

public class DepartmentDto {
    private Long id;
    private String name;
    private List<String> userNames;

    public DepartmentDto() {
    }

    public DepartmentDto(String name, List<String> userNames) {
        this.name = name;
        this.userNames = userNames;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }
}
