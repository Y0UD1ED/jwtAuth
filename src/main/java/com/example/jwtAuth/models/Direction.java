package com.example.jwtAuth.models;

public class Direction {
    Integer id;
    String name;

    public Direction() {
    }

    public Direction(Integer id) {
        this.id = id;
    }

    public Direction(String name) {
        this.name = name;
    }

    public Direction(Integer id, String name) {
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
