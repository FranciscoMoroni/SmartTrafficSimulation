package model.state;

import model.TrafficLight;
import javafx.scene.paint.Color;

public class RedState implements LightState {
    private double timer = 0;
    private final double duration = 12.0; // Antes era 5.0

    @Override
    public void update(TrafficLight light, double dt) {
        timer += dt;
        if (timer >= duration) {
            light.setState(new GreenState());
        }
    }

    @Override
    public Color getColor() { return Color.RED; }

    @Override
    public boolean canPass() { return false; }
}