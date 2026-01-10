package controller;

import model.TrafficLight;
import model.Road;
import model.EmergencyVehicle;

public class AdaptiveCycle implements Strategy {
    @Override
    public void updateLight(TrafficLight light, double dt, Road road) {
        // acelera o ciclo se tiver transito
        long waiting = road.getVehicles().stream()
                .filter(v -> v.getPosition() > road.getStopLine() - 150 && v.getPosition() < road.getStopLine())
                .count();

        double speedUp = (waiting >= 3) ? 2.5 : 1.0;
        light.tickState(dt * speedUp);
    }
}