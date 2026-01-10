package model;

import util.Metrics;

public class Vehicle {
    protected double position;
    protected double speed = 50.0;
    protected final double length = 25.0; // Comprimento visual do carro

    private double stopTimer = 0;   // acumula tempo enquanto esta parado
    private boolean wasStopped = false;

    public Vehicle(double startingPos) {
        this.position = startingPos;
    }

    public void update(double dt, World world, Vehicle carInFront) {
        // determina qual o proximo semaforo e linha de paragem
        TrafficLight targetLight;
        double targetLine;

        if (this.position < world.getStopLine() - 10) {
            targetLight = world.getLight();
            targetLine = world.getStopLine();
        } else {
            targetLight = world.getLight2();
            targetLine = world.getStopLine2();
        }

        boolean canMove = true;

        if (!targetLight.canPass()) {
            // Se for um carro civil e estiverentre 45 e 100 unidades
            if (!(this instanceof EmergencyVehicle)) {
                if (this.position < targetLine - 45 && this.position > targetLine - 100) {
                    canMove = false;
                }
            }
            // a ambulancia ignora isto sempre
        }

        //colisao de distancia de segurança
        if (carInFront != null) {
            double distance = carInFront.getPosition() - this.position;
            double safeGap = length + 30; // Comprimento + folga para não colarem

            if (distance > 0 && distance < safeGap) {
                // Se for ambulância ela salta para a frente
                if (this instanceof EmergencyVehicle) {
                    this.position = carInFront.getPosition() + length + 10;
                } else {
                    canMove = false;
                }
            }
        }

        //movimento e tempo de espera
        double oldPosition = this.position;

        if (canMove) {
            // Se estava parado e agora vai andar, envia o tempo acumulado para as métricas
            if (wasStopped) {
                Metrics.addWaitingTime(stopTimer);
                stopTimer = 0;
                wasStopped = false;
            }
            position += speed * dt;
        } else {
            // incrementa o cronómetro de espera
            stopTimer += dt;
            wasStopped = true;
        }

        // verifica se o carro cruzou a linha de paragem 1 ou 2
        if ((oldPosition <= world.getStopLine() && position > world.getStopLine()) ||
                (oldPosition <= world.getStopLine2() && position > world.getStopLine2())) {
            Metrics.addCarServed();
        }

        // 6. Reset da Estrada (Loop infinito)
        if (position > 1200) {
            position = -200;
        }
    }

    public double getPosition() {
        return position;
    }
}