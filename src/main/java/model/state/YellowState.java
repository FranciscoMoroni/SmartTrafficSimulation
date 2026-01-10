package model.state;

import model.TrafficLight;
import javafx.scene.paint.Color;

public class YellowState implements LightState {
    private double timer = 0;
    private final double duration = 4.0; // Antes era 2.0

    @Override
    public void update(TrafficLight light, double dt) {
        timer += dt;
        if (timer >= duration) {
            light.setState(new RedState());
        }
    }

    @Override
    public Color getColor() { return Color.YELLOW; }

    @Override
    public boolean canPass() { return true; }
}