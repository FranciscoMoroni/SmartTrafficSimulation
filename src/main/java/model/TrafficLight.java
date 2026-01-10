package model;

import javafx.scene.paint.Color;
import model.state.LightState;
import model.state.GreenState;
import model.state.RedState;

public class TrafficLight {
    private LightState state;

    public TrafficLight() {
        this.state = new RedState(); //começa no vermelho
    }

    public void setState(LightState state) {
        this.state = state;
    }

    public LightState getState() {
        return state;
    }

    //faz com que a ambulancia force o verde
    public void forceGreen() {
        if (!(this.getState() instanceof GreenState)) {
            this.setState(new GreenState());
        }
    }

    public void tickState(double dt) {
        state.update(this, dt);
    }

    public Color getColor() {
        return state.getColor();
    }

    public boolean canPass() {
        return state.canPass();
    }
}