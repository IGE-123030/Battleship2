package battleship;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BoardGUI extends Application {

    private static IGame game;   // Armazena o game que veio do Tasks
    private static boolean started = false;
    private static GridPane grid = new GridPane();
    private static BoardGUI instance;

    public static void showBoard(IGame g) {
        game = g;

        if (!started) {
            started = true;
            new Thread(() -> launch()).start();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        primaryStage.setTitle("Battleship");

        grid = new GridPane();
        updateBoard();

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateBoard() {
        grid.getChildren().clear();

        for (int r = 0; r < Game.BOARD_SIZE; r++) {
            for (int c = 0; c < Game.BOARD_SIZE; c++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(40, 40);
                cell.setStyle("-fx-border-color: black; -fx-background-color: lightblue;");

                // Desenha navios
                for (IShip ship : game.getMyFleet().getShips()) {
                    for (IPosition pos : ship.getPositions()) {
                        if (pos.getRow() == r && pos.getColumn() == c) {
                            cell.setStyle("-fx-background-color: gray; -fx-border-color: black;");
                        }
                    }
                }

                grid.add(cell, c, r);

                // Desenha tiros
                for (IMove move : game.getAlienMoves()) {
                    for (IPosition shot : move.getShots()) {
                        if (shot.getRow() == r && shot.getColumn() == c) {

                            if (game.getMyFleet().shipAt(shot) != null) {
                                cell.setStyle("-fx-background-color: red; -fx-border-color: black;");
                            } else {
                                cell.setStyle("-fx-background-color: white; -fx-border-color: black;");
                            }
                        }
                    }
                }

            }
        }
    }

    public static void refresh() {
        if (instance != null) {
            Platform.runLater(() -> {
                instance.updateBoard(); // 🔥 redesenha o tabuleiro
            });
        }
    }
}