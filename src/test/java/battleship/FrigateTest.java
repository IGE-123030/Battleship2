package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Frigate.
 * Author: André (123030)
 * Date: 2026-04-16
 * Cyclomatic Complexity:
 * - constructor: 5
 */
class FrigateTest {

    private IPosition startPos;

    @BeforeEach
    void setUp() {
        // Inicializa a posição base para instanciar as fragatas nos testes
        startPos = new Position(5, 5);
    }

    @AfterEach
    void tearDown() {
        startPos = null;
    }

    // ==========================================
    // Tests for Constructor - CC: 5
    // Paths: NORTH, SOUTH, EAST, WEST, null
    // ==========================================

    @Test
    @DisplayName("constructor1: Path 1 - Bearing NORTH (Soma linhas)")
    void constructor1() {
        Frigate frigate = new Frigate(Compass.NORTH, startPos);
        List<IPosition> positions = frigate.getPositions();

        assertAll("Verifica a criação da Fragata para NORTH",
                () -> assertNotNull(frigate, "Error: expected Frigate instance not to be null"),
                () -> assertEquals("fragata", frigate.getCategory(), "Error: expected category 'fragata'"),
                () -> assertEquals(4, positions.size(), "Error: expected Frigate size to be exactly 4"),
                () -> assertEquals(5, positions.get(0).getRow(), "Error: expected first position row to be 5"),
                () -> assertEquals(5, positions.get(0).getColumn(), "Error: expected first position col to be 5"),
                () -> assertEquals(8, positions.get(3).getRow(), "Error: expected fourth position row to be 8")
        );
    }

    @Test
    @DisplayName("constructor2: Path 2 - Bearing SOUTH (Soma linhas na implementação atual)")
    void constructor2() {
        Frigate frigate = new Frigate(Compass.SOUTH, startPos);
        List<IPosition> positions = frigate.getPositions();

        assertAll("Verifica a criação da Fragata para SOUTH",
                () -> assertEquals(4, positions.size(), "Error: expected Frigate size to be exactly 4"),
                () -> assertEquals(5, positions.get(0).getRow(), "Error: expected first position row to be 5"),
                () -> assertEquals(8, positions.get(3).getRow(), "Error: expected fourth position row to be 8")
        );
    }

    @Test
    @DisplayName("constructor3: Path 3 - Bearing EAST (Soma colunas)")
    void constructor3() {
        Frigate frigate = new Frigate(Compass.EAST, startPos);
        List<IPosition> positions = frigate.getPositions();

        assertAll("Verifica a criação da Fragata para EAST",
                () -> assertEquals(4, positions.size(), "Error: expected Frigate size to be exactly 4"),
                () -> assertEquals(5, positions.get(0).getColumn(), "Error: expected first position col to be 5"),
                () -> assertEquals(8, positions.get(3).getColumn(), "Error: expected fourth position col to be 8")
        );
    }

    @Test
    @DisplayName("constructor4: Path 4 - Bearing WEST (Soma colunas na implementação atual)")
    void constructor4() {
        Frigate frigate = new Frigate(Compass.WEST, startPos);
        List<IPosition> positions = frigate.getPositions();

        assertAll("Verifica a criação da Fragata para WEST",
                () -> assertEquals(4, positions.size(), "Error: expected Frigate size to be exactly 4"),
                () -> assertEquals(5, positions.get(0).getColumn(), "Error: expected first position col to be 5"),
                () -> assertEquals(8, positions.get(3).getColumn(), "Error: expected fourth position col to be 8")
        );
    }

    @Test
    @DisplayName("constructor5: Path 5 - Bearing Null (Lança NullPointerException no switch)")
    void constructor5() {
        // Quando o Enum passado a um switch é null, o Java lança automaticamente uma NullPointerException.
        assertThrows(NullPointerException.class, () -> {
            new Frigate(null, startPos);
        }, "Error: expected NullPointerException when bearing is null and evaluated in switch statement");
    }
}