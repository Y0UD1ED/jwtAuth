package com.example.jwtAuth.models;

public class Level {
    Integer id;
    String name;

    public Level() {
    }
    public Level(Integer id) {
        this.id = id;
    }

    public Level(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
