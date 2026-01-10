package model.state;

import model.TrafficLight;
import javafx.scene.paint.Color;

public class GreenState implements LightState {
    private double timer = 0;
    private final double duration = 15.0; // Antes era 7.0

    @Override
    public void update(TrafficLight light, double dt) {
        timer += dt;
        if (timer >= duration) {
            light.setState(new YellowState());
        }
    }

    @Override
    public Color getColor() { return Color.LIME; }

    @Override
    public boolean canPass() { return true; }
}