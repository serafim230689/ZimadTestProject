package com.zimadtestproject.observer;

import com.zimadtestproject.api.Animal;

import java.util.Observable;

public class AnimalDataRepository extends Observable {
    private static AnimalDataRepository instance;
    private Animal data;

    private AnimalDataRepository() {
    }

    public static AnimalDataRepository getInstance() {
        if (instance == null) {
            instance = new AnimalDataRepository();
        }
        return instance;
    }

    public void setAnimal(Animal animal) {
        data = animal;
        setChanged();
        notifyObservers();
    }

    public Animal getData() {
        return data;
    }
}
