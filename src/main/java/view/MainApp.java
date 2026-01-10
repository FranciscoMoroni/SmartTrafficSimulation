package view;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.World;
import util.Metrics;
import controller.FixedCycle;
import controller.AdaptiveCycle;

public class MainApp extends Application {
    private World world;
    private CanvasView canvas;
    private boolean isPaused = false;
    private Label statsLabel;

    @Override
    public void start(Stage primaryStage) {
        //inicia Modelo e a View
        world = new World();
        canvas = new CanvasView(1000, 400);

        // Painel de Controlos
        Button pauseBtn = new Button("Pausa / Resumo");
        Button resetBtn = new Button("Reiniciar Simulação");
        Button exportBtn = new Button("Exportar para CSV");

        //  Estratégia
        Label modeLabel = new Label("Modo de Controlo:");
        modeLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        ComboBox<String> modeCombo = new ComboBox<>();
        modeCombo.getItems().addAll("Fixed (Fixo)", "Adaptive (Inteligente)");
        modeCombo.setValue("Adaptive (Inteligente)");

        // Slider da Velocidade
        Label speedLabel = new Label("Velocidade:");
        speedLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Slider speedSlider = new Slider(0.5, 5.0, 1.0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setMajorTickUnit(1.0);

        // Painel das Estatísticas
        statsLabel = new Label("Carros Servidos: 0 | Espera Média: 0.00s");
        statsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #00FF00; -fx-font-family: 'Courier New';");

        // Conntrolos layout
        HBox topControls = new HBox(20, modeLabel, modeCombo, speedLabel, speedSlider);
        topControls.setAlignment(Pos.CENTER);
        topControls.setPadding(new Insets(10));

        HBox bottomButtons = new HBox(20, pauseBtn, resetBtn, exportBtn);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setPadding(new Insets(10));

        VBox controlPanel = new VBox(10, statsLabel, topControls, bottomButtons);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setPadding(new Insets(15));
        controlPanel.setStyle("-fx-background-color: #222222; -fx-border-color: #444444; -fx-border-width: 2 0 0 0;");

        //Lógica de Interação
        pauseBtn.setOnAction(e -> isPaused = !isPaused);

        resetBtn.setOnAction(e -> {
            world = new World();
            Metrics.reset();
        });

        exportBtn.setOnAction(e -> {
            Metrics.exportToCSV();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportação Concluída");
            alert.setHeaderText(null);
            alert.setContentText("Os dados foram guardados em 'simulation_results.csv'");
            alert.showAndWait();
        });

        modeCombo.setOnAction(e -> {
            if (modeCombo.getValue().contains("Fixed")) {
                world.setStrategy(new FixedCycle());
                System.out.println("Modo alterado para: FIXED");
            } else {
                world.setStrategy(new AdaptiveCycle());
                System.out.println("Modo alterado para: ADAPTIVE");
            }
        });

        // 5. Motor de Animação (M1/M2)
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused) {
                    double dt = 0.016 * speedSlider.getValue(); // 60 FPS
                    world.tick(dt);
                    updateStats();
                }
                canvas.draw(world);
            }
        }.start();

        //janela principal
        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setBottom(controlPanel);

        Scene scene = new Scene(root, 1024, 600);
        primaryStage.setTitle("Smart Traffic Simulator - Projeto de POO");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void updateStats() {
        statsLabel.setText(String.format("Carros Servidos: %d | Espera Média: %.2fs",
                Metrics.getCarsServed(),
                Metrics.getAverageWait()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}