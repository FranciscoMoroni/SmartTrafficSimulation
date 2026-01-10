package model;

import controller.Strategy;
import controller.AdaptiveCycle;
import controller.FixedCycle;

public class World extends Road {
    private TrafficLight light2 = new TrafficLight();
    private double stopLine2 = 850.0;
    private Strategy strategy2 = new FixedCycle();

    public World() {
        super();
        this.setStopLine(350.0);

        // posicionamento inicial
        addVehicle(new Vehicle(200));
        addVehicle(new EmergencyVehicle(0));
        addVehicle(new Vehicle(-250));

        this.setStrategy(new AdaptiveCycle());
    }

    public void tick(double dt) {
        this.update(dt);
    }

    @Override
    public void update(double dt) {
        super.update(dt);

        strategy2.updateLight(light2, dt, this);
    }

    public TrafficLight getLight2() { return light2; }
    public double getStopLine2() { return stopLine2; }

    public boolean isEmergencyNear(double linePos) {
        return getVehicles().stream()
                .anyMatch(v -> v instanceof EmergencyVehicle && Math.abs(v.getPosition() - linePos) < 250);
    }
}