package com.zimadtestproject;

public enum AnimalType {
    CAT(0, "cat"), DOG(1, "dog");

    private String type;
    private int id;

    AnimalType(int id, String type) {
        this.type = type;
        this.id = id;
    }

    public String type() {
        return type;
    }

    public int getId() {
        return id;
    }


    @Override
    public String toString() {
        return "AnimalType{" +
                "type='" + type + '\'' +
                ", id=" + id +
                '}';
    }
}
