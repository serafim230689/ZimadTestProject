package com.zimadtestproject.api;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnimalResponse {

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<Animal> data;

    public String getMessage() {
        return message;
    }

    public List<Animal> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "AnimalResponse{" +
                "message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
