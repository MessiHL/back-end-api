package com.example.request_learn.entity;

import org.springframework.web.multipart.MultipartFile;

public class User {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    private String id;
    private String name;
    private MultipartFile file;
}
