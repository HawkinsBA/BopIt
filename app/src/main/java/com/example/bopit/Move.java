package com.example.bopit;

public class Move {
    private String name;
    private double successThreshold;

    Move(String name, double successThreshold) {
        this.name = name;
        this.successThreshold = successThreshold;
    }

    public String getName() {
        return name;
    }

    public double getSuccessThreshold() {
        return successThreshold;
    }
}
