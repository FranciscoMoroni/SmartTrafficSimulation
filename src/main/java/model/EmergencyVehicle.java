package model;

public class EmergencyVehicle extends Vehicle {
    public EmergencyVehicle(double startingPos) {
        super(startingPos);
    }

    @Override
    public void update(double dt, World world, Vehicle carInFront) {
        super.update(dt, world, carInFront);
    }
}