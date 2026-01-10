package controller;

import model.TrafficLight;
import model.Road;

public class FixedCycle implements Strategy {
    @Override
    public void updateLight(TrafficLight light, double dt, Road road) {
        light.tickState(dt);
    }
}