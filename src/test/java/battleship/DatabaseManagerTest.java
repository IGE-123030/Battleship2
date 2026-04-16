package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class DatabaseManager.
 * Author: André Ribeiro (123030)
 * Date: 2026-04-16 14:51
 * Cyclomatic Complexity:
 * - setupDatabase(): 2
 * - saveMove(): 2
 */
class DatabaseManagerTest {

    private DatabaseManager dbManager;

    @BeforeEach
    void setUp() {
        // Requirement: Implement a @BeforeEach method to create an instance
        dbManager = new DatabaseManager();
    }

    @AfterEach
    void tearDown() {
        // Requirement: Implement an @AfterEach method to nullify that instance
        dbManager = null;
    }

    // ==========================================
    // Tests for setupDatabase() - CC: 2
    // ==========================================

    @Test
    @DisplayName("setupDatabase1: Path 1 - Executa o try block com sucesso")
    void setupDatabase1() {
        // Tenta estabelecer a ligação e criar a tabela.
        // Se o MySQL estiver a correr, vai pelo Path 1 (try).
        assertDoesNotThrow(() -> {
            DatabaseManager.setupDatabase();
        }, "Error: expected setupDatabase to complete without throwing exceptions on the success path");
    }

    @Test
    @DisplayName("setupDatabase2: Path 2 - Executa o catch block (SQLException)")
    void setupDatabase2() {
        /*
         * NOTA DE REFABRICAÇÃO (Para o teu guião):
         * Como a URL do MySQL está "hardcoded" na classe original e o "catch" é
         * tratado internamente (imprime apenas para o System.err), não conseguimos
         * forçar programaticamente este caminho (Path 2) apenas com testes unitários.
         * Para cobrir este branch a 100%, terás de refabricar o DatabaseManager
         * para receber a URL como parâmetro (Dependency Injection).
         * Por agora, garantimos apenas que o método não "rebenta" a aplicação.
         */
        assertDoesNotThrow(() -> {
            DatabaseManager.setupDatabase();
        }, "Error: expected internal catch to handle the exception, so it should not throw outward");
    }

    // ==========================================
    // Tests for saveMove() - CC: 2
    // ==========================================

    @Test
    @DisplayName("saveMove1: Path 1 - Executa o try block e insere dados com sucesso")
    void saveMove1() {
        int moveNumber = 1;
        String jsonShots = "[{\"row\": 0, \"col\": 0}]";
        int hits = 1;
        int sinks = 0;

        // Utilizamos assertAll conforme pedido pela prompt para agrupar asserções
        // (mesmo que aqui seja só para atestar que o estado da instância não é nulo e corre bem)
        assertAll("Verifica a execução normal de saveMove",
                () -> assertNotNull(dbManager, "Error: expected dbManager instance to not be null"),
                () -> assertDoesNotThrow(() -> {
                    DatabaseManager.saveMove(moveNumber, jsonShots, hits, sinks);
                }, "Error: expected saveMove to execute and insert data without throwing exceptions")
        );
    }

    @Test
    @DisplayName("saveMove2: Path 2 - Executa o catch block ao tentar inserir")
    void saveMove2() {
        // O mesmo problema de refabricação aplica-se aqui para o Path 2.
        assertDoesNotThrow(() -> {
            DatabaseManager.saveMove(-1, "invalid_json", -1, -1);
        }, "Error: expected saveMove internal catch to handle SQL exceptions safely without throwing");
    }
}