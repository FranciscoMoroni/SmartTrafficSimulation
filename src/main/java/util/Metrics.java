package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Metrics {
    private static double totalWaitingTime = 0;
    private static int carsServed = 0;

    public static void addWaitingTime(double time) {
        totalWaitingTime += time;
    }

    public static void addCarServed() {
        carsServed++;
    }

    public static int getCarsServed() {
        return carsServed;
    }

    public static double getAverageWait() {
        if (carsServed == 0) return 0.0;
        return totalWaitingTime / carsServed;
    }

    public static void reset() {
        totalWaitingTime = 0;
        carsServed = 0;
    }

    public static void exportToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("simulation_results.csv"))) {
            writer.println("Metric,Value");
            writer.println("Cars Served," + carsServed);
            writer.println("Average Wait," + String.format("%.2f", getAverageWait()));
        } catch (IOException e) { e.printStackTrace(); }
    }
}