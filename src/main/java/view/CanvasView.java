package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import model.*;

public class CanvasView extends Canvas {
    private final double scale = 0.9;

    public CanvasView(double width, double height) {
        super(width, height);
    }

    public void draw(World world) {
        GraphicsContext gc = getGraphicsContext2D();

        // Fundo
        gc.setFill(Color.web("#2d5a27"));
        gc.fillRect(0, 0, getWidth(), getHeight());

        // Estrada com textura de asfalto
        drawRoad(gc);

        // Passadeiras e Semáforos
        drawIntersection(gc, world.getLight(), world.getStopLine(), world);
        drawIntersection(gc, world.getLight2(), world.getStopLine2(), world);

        // Veículos
        for (Vehicle v : world.getVehicles()) {
            drawVehicle(gc, v);
        }
    }

    private void drawRoad(GraphicsContext gc) {
        double yMid = getHeight() / 2;

        // Asfalto principal
        gc.setFill(Color.web("#333333"));
        gc.fillRect(0, yMid - 35, getWidth(), 70);

        // Linhas de borda
        gc.setStroke(Color.web("#555555"));
        gc.setLineWidth(4);
        gc.strokeLine(0, yMid - 35, getWidth(), yMid - 35);
        gc.strokeLine(0, yMid + 35, getWidth(), yMid + 35);

        // Linha descontínua central
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.setLineDashes(30);
        gc.strokeLine(0, yMid, getWidth(), yMid);
        gc.setLineDashes(null); // Reset dashes
    }

    private void drawIntersection(GraphicsContext gc, TrafficLight light, double stopLinePos, World world) {
        double x = stopLinePos * scale;
        double yMid = getHeight() / 2;

        // Passadeira
        gc.setFill(Color.web("#DDDDDD"));
        for (int i = 0; i < 7; i++) {
            gc.fillRect(x - 60, (yMid - 35) + (i * 10), 40, 6);
        }

        // Poste do Semáforo
        gc.setFill(Color.web("#222222"));
        gc.fillRect(x + 10, yMid - 90, 4, 60); // Poste vertical
        gc.fillRoundRect(x + 2, yMid - 100, 20, 45, 10, 10); // Caixa das luzes

        // Luzes do Semáforo
        drawLight(gc, x + 12, yMid - 95, light.getColor());

        // Emergência para Peões
        boolean ambulanceNear = world.isEmergencyNear(stopLinePos);
        boolean pedCanGo = !light.canPass() && !ambulanceNear;

        // Semáforo de Peões
        gc.setFill(Color.BLACK);
        gc.fillRect(x - 65, yMid + 45, 12, 22);
        Color pedColor = pedCanGo ? Color.LIME : Color.RED;
        gc.setFill(pedColor);
        gc.fillOval(x - 63, pedCanGo ? yMid + 55 : yMid + 47, 8, 8);

        // Peões
        if (pedCanGo) {
            gc.setFill(Color.web("#FFA500"));
            double anim = (System.currentTimeMillis() % 1000) / 10.0;
            // Peão 1
            gc.fillOval(x - 45, (yMid + 40) - anim, 6, 6); // Cabeça
            gc.fillRect(x - 45, (yMid + 47) - anim, 6, 10); // Corpo
            // Peão 2
            gc.fillOval(x - 25, (yMid - 40) + anim, 6, 6);
            gc.fillRect(x - 25, (yMid - 33) + anim, 6, 10);
        }
    }

    private void drawLight(GraphicsContext gc, double x, double y, Color color) {
        // brilho
        gc.setGlobalAlpha(0.3);
        gc.setFill(color);
        gc.fillOval(x - 4, y - 4, 16, 16);
        gc.setGlobalAlpha(1.0);
        gc.setFill(color);
        gc.fillOval(x, y, 8, 8);
    }

    private void drawVehicle(GraphicsContext gc, Vehicle v) {
        double x = v.getPosition() * scale;
        double y = getHeight() / 2;

        if (v instanceof EmergencyVehicle) {
            // ambulancia
            gc.setFill(Color.WHITE);
            gc.fillRoundRect(x, y - 18, 40, 20, 5, 5); // Corpo
            gc.setFill(Color.RED);
            gc.fillRect(x + 5, y - 10, 30, 4); // Faixa lateral
            gc.fillRect(x + 18, y - 18, 4, 20); // Cruz

            // Sirene
            Color sirenColor = (System.currentTimeMillis() / 150 % 2 == 0) ? Color.BLUE : Color.RED;
            gc.setFill(sirenColor);
            gc.fillOval(x + 15, y - 23, 10, 6);
        } else {
            // Carro normal
            LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.DODGERBLUE), new Stop(1, Color.NAVY));
            gc.setFill(gradient);
            gc.fillRoundRect(x, y - 15, 32, 18, 8, 8);

            // Vidros
            gc.setFill(Color.LIGHTBLUE);
            gc.fillRect(x + 20, y - 12, 8, 12);
        }

        // Rodas
        gc.setFill(Color.BLACK);
        gc.fillOval(x + 5, y + 2, 6, 6);
        gc.fillOval(x + 22, y + 2, 6, 6);
        gc.fillOval(x + 5, y - 20, 6, 6);
        gc.fillOval(x + 22, y - 20, 6, 6);
    }
}