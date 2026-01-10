package model.state;

import model.TrafficLight;
import javafx.scene.paint.Color;

public interface LightState {
    void update(TrafficLight light, double dt);

    Color getColor();
    boolean canPass();
}