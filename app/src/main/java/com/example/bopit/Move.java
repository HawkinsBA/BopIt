package com.example.bopit;

public class Move {
    private String name;
    private double successThreshold, acceptableDeviationMargin;

    Move(String name, double successThreshold, double acceptableDeviationMargin) {
        this.name = name;
        this.successThreshold = successThreshold;
        this.acceptableDeviationMargin = acceptableDeviationMargin;
    }

    public String getName() {
        return name;
    }

    public double getSuccessThreshold() {
        return successThreshold;
    }

    public double getAcceptableDeviationMargin() {
        return acceptableDeviationMargin;
    }

}
