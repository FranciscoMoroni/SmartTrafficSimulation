package model;

import controller.Strategy;
import controller.FixedCycle;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class Road {
    protected List<Vehicle> vehicles = new ArrayList<>();
    protected TrafficLight light = new TrafficLight();
    protected Strategy strategy = new FixedCycle();
    protected double stopLine = 350.0;

    public void setStrategy(Strategy strategy) { this.strategy = strategy; }
    public void setStopLine(double pos) { this.stopLine = pos; }
    public void addVehicle(Vehicle v) { vehicles.add(v); }

    public void update(double dt) {
        // A estratégia principal atualiza o primeiro semáforo
        strategy.updateLight(light, dt, this);

        // Ordenar veículos para o sensor de colisão funcionar (do mais à frente para o mais atrás)
        vehicles.sort(Comparator.comparingDouble(Vehicle::getPosition).reversed());

        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle carInFront = (i == 0) ? null : vehicles.get(i - 1);
            // Passamos o 'World' para que o veículo veja ambos os semáforos
            if (this instanceof World) {
                vehicles.get(i).update(dt, (World) this, carInFront);
            }
        }
    }

    public List<Vehicle> getVehicles() { return vehicles; }
    public TrafficLight getLight() { return light; }
    public double getStopLine() { return stopLine; }
    public Strategy getStrategy() { return strategy; }
}