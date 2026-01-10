package controller;

import model.TrafficLight;

public interface Strategy {
    void updateLight(TrafficLight light, double dt, model.Road road);
}