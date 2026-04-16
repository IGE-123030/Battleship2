package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Ship.
 * Author: André (123030)
 * Date: 2026-04-16
 * Cyclomatic Complexity:
 * - constructor: 1
 * - getCategory / getSize / getBearing / getPosition / getPositions: 1
 * - stillFloating: 3
 * - shoot: 3
 * - sink: 2
 * - occupies: 3
 * - tooCloseTo(IShip): 3
 * - tooCloseTo(IPosition): 3
 * - getTopMostPos / getBottomMostPos / getLeftMostPos / getRightMostPos: 3
 */
public class ShipTest {

    private Ship ship;
    private IPosition pos1;
    private IPosition pos2;

    @BeforeEach
    void setUp() {
        // Criamos Mocks para garantir isolamento e forçar caminhos específicos.
        pos1 = createMockPosition(5, 5, false);
        pos2 = createMockPosition(6, 5, false);

        // Em vez de usar a Barge (size 1), criamos uma instância anónima com Size = 2.
        // Isto é OBRIGATÓRIO para testar os ciclos 'for' que começam em i=1.
        ship = new Ship("TestShip", Compass.NORTH, pos1, 2) {};

        // Injetar posições no navio
        ship.getPositions().add(pos1);
        ship.getPositions().add(pos2);
    }

    @AfterEach
    void tearDown() {
        ship = null;
        pos1 = null;
        pos2 = null;
    }

    // Helper method para criar IPositions controlados
    private IPosition createMockPosition(int row, int col, boolean isHit) {
        return new IPosition() {
            private boolean hit = isHit;
            @Override public int getRow() { return row; }
            @Override public int getColumn() { return col; }

            @Override
            public char getClassicRow() {
                return 0;
            }

            @Override
            public int getClassicColumn() {
                return 0;
            }

            @Override public boolean isHit() { return hit; }
            @Override public void shoot() { hit = true; }

            @Override
            public boolean isOccupied() {
                return false;
            }

            @Override public List<IPosition> adjacentPositions() { return new ArrayList<>(); }
            // Simula adjacência se a diferença for 1
            @Override public boolean isAdjacentTo(IPosition other) {
                return Math.abs(row - other.getRow()) <= 1 && Math.abs(col - other.getColumn()) <= 1;
            }

            @Override
            public void occupy() {

            }

            @Override public boolean isInside() { return true; }
        };
    }

    // ==========================================
    // Métodos Simples (Getters) - CC: 1
    // ==========================================

    @Test
    @DisplayName("constructor1: Verifica instanciação correta")
    void constructor1() {
        assertAll("Verifica atributos base",
                () -> assertNotNull(ship, "Error: Ship instance should not be null."),
                () -> assertEquals("TestShip", ship.getCategory(), "Error: Ship category is incorrect."),
                () -> assertEquals(Compass.NORTH, ship.getBearing(), "Error: Ship bearing is incorrect."),
                () -> assertEquals(2, ship.getSize(), "Error: Ship size is incorrect.")
        );
    }

    @Test
    @DisplayName("getPositions1: Verifica a devolução da lista")
    void getPositions1() {
        List<IPosition> positions = ship.getPositions();
        assertEquals(2, positions.size(), "Error: Ship should have exactly two positions.");
        assertEquals(5, positions.get(0).getRow(), "Error: First position row should be 5.");
    }

    // ==========================================
    // stillFloating() - CC: 3 (for + if)
    // ==========================================

    @Test
    @DisplayName("stillFloating1: Path 1 - Loop não executa (Size 0)")
    void stillFloating1() {
        Ship emptyShip = new Ship("Ghost", Compass.NORTH, pos1, 0) {};
        assertFalse(emptyShip.stillFloating(), "Error: Ship with 0 size should not float.");
    }

    @Test
    @DisplayName("stillFloating2: Path 2 - Loop encontra posição intacta (True)")
    void stillFloating2() {
        assertTrue(ship.stillFloating(), "Error: Ship should still be floating.");
    }

    @Test
    @DisplayName("stillFloating3: Path 3 - Todas posições destruídas (False)")
    void stillFloating3() {
        ship.getPositions().get(0).shoot();
        ship.getPositions().get(1).shoot();
        assertFalse(ship.stillFloating(), "Error: Ship should sink when all positions hit.");
    }

    // ==========================================
    // shoot(IPosition) - CC: 3 (for + if)
    // ==========================================

    @Test
    @DisplayName("shoot1: Path 1 - Lista vazia, loop não executa")
    void shoot1() {
        Ship emptyShip = new Ship("Ghost", Compass.NORTH, pos1, 0) {};
        assertDoesNotThrow(() -> emptyShip.shoot(pos1), "Error: Shooting empty ship should not throw.");
    }

    @Test
    @DisplayName("shoot2: Path 2 - Posição não encontrada no loop")
    void shoot2() {
        IPosition miss = createMockPosition(10, 10, false);
        ship.shoot(miss);
        assertFalse(ship.getPositions().get(0).isHit(), "Error: Position should not be hit.");
    }

    @Test
    @DisplayName("shoot3: Path 3 - Posição encontrada e atingida")
    void shoot3() {
        ship.shoot(pos1);
        assertTrue(ship.getPositions().get(0).isHit(), "Error: Position should be marked as hit.");
    }

    // ==========================================
    // occupies(IPosition) - CC: 3 (for + if)
    // ==========================================

    @Test
    @DisplayName("occupies1: Path 1 - Lista vazia")
    void occupies1() {
        Ship emptyShip = new Ship("Ghost", Compass.NORTH, pos1, 0) {};
        assertFalse(emptyShip.occupies(pos1), "Error: Empty ship occupies nothing.");
    }

    @Test
    @DisplayName("occupies2: Path 2 - Não ocupa a posição")
    void occupies2() {
        IPosition pos = createMockPosition(1, 1, false);
        assertFalse(ship.occupies(pos), "Error: Ship should not occupy (1, 1).");
    }

    @Test
    @DisplayName("occupies3: Path 3 - Ocupa a posição")
    void occupies3() {
        assertTrue(ship.occupies(pos1), "Error: Ship should occupy its own position.");
    }

    // ==========================================
    // tooCloseTo(IPosition) - CC: 3 (for + if)
    // ==========================================

    @Test
    @DisplayName("tooCloseToPos1: Path 1 - Lista vazia")
    void tooCloseToPos1() {
        Ship emptyShip = new Ship("Ghost", Compass.NORTH, pos1, 0) {};
        assertFalse(emptyShip.tooCloseTo(pos1), "Error: Empty ship is not close to anything.");
    }

    @Test
    @DisplayName("tooCloseToPos2: Path 2 - Não é adjacente")
    void tooCloseToPos2() {
        IPosition farPos = createMockPosition(10, 10, false);
        assertFalse(ship.tooCloseTo(farPos), "Error: Ship should not be close to distant pos.");
    }

    @Test
    @DisplayName("tooCloseToPos3: Path 3 - É adjacente")
    void tooCloseToPos3() {
        IPosition adjPos = createMockPosition(5, 6, false);
        assertTrue(ship.tooCloseTo(adjPos), "Error: Ship should be close to adjacent pos.");
    }

    // ==========================================
    // getTopMostPos() - CC: 3 (for + if)
    // ==========================================

    @Test
    @DisplayName("getTopMostPos1: Path 1 - Tamanho 1 (Loop não itera)")
    void getTopMostPos1() {
        Ship tinyShip = new Ship("Tiny", Compass.NORTH, pos1, 1) {};
        tinyShip.getPositions().add(pos1); // row 5
        assertEquals(5, tinyShip.getTopMostPos(), "Error: TopMost should be 5.");
    }

    @Test
    @DisplayName("getTopMostPos2: Path 2 - Itera mas condição if falsa")
    void getTopMostPos2() {
        // As posições são (5,5) e (6,5). Row 6 não é menor que 5.
        assertEquals(5, ship.getTopMostPos(), "Error: TopMost should remain 5.");
    }

    @Test
    @DisplayName("getTopMostPos3: Path 3 - Itera e condição if verdadeira")
    void getTopMostPos3() {
        // Modificamos a segunda posição para estar "acima" (menor row)
        ship.getPositions().set(1, createMockPosition(2, 5, false));
        assertEquals(2, ship.getTopMostPos(), "Error: TopMost should update to 2.");
    }

    // ==========================================
    // getBottomMostPos() - CC: 3
    // ==========================================

    @Test
    @DisplayName("getBottomMostPos1: Path 1 - Tamanho 1")
    void getBottomMostPos1() {
        Ship tinyShip = new Ship("Tiny", Compass.NORTH, pos1, 1) {};
        tinyShip.getPositions().add(pos1);
        assertEquals(5, tinyShip.getBottomMostPos(), "Error: BottomMost should be 5.");
    }

    @Test
    @DisplayName("getBottomMostPos2: Path 2 - Itera mas condição if falsa")
    void getBottomMostPos2() {
        ship.getPositions().set(1, createMockPosition(3, 5, false)); // 3 não é > 5
        assertEquals(5, ship.getBottomMostPos(), "Error: BottomMost should remain 5.");
    }

    @Test
    @DisplayName("getBottomMostPos3: Path 3 - Itera e condição if verdadeira")
    void getBottomMostPos3() {
        // A segunda posição já é (6,5), logo 6 > 5. Condição executa!
        assertEquals(6, ship.getBottomMostPos(), "Error: BottomMost should update to 6.");
    }
}