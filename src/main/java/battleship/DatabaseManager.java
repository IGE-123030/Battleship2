package battleship;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    // Ajusta as credenciais de acordo com o teu servidor MySQL local
    private static final String URL = "jdbc:mysql://localhost:3306/battleship_db?createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void setupDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS moves (" +
                    "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "move_number INTEGER, " +
                    "shots_json TEXT, " +
                    "hits_count INTEGER, " +
                    "sinks_count INTEGER, " +
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao configurar BD: " + e.getMessage());
        }
    }

    public static void saveMove(int moveNumber, String jsonShots, int hits, int sinks) {
        String sql = "INSERT INTO moves (move_number, shots_json, hits_count, sinks_count) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, moveNumber);
            pstmt.setString(2, jsonShots);
            pstmt.setInt(3, hits);
            pstmt.setInt(4, sinks);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao gravar jogada: " + e.getMessage());
        }
    }
}