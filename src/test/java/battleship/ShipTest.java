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

    // ==========================================
    // getLeftMostPos() - CC: 3
    // ==========================================

    @Test
    @DisplayName("getLeftMostPos1: Path 1 - Tamanho 1 (Loop não itera)")
    void getLeftMostPos1() {
        Ship tinyShip = new Ship("Tiny", Compass.NORTH, pos1, 1) {};
        tinyShip.getPositions().add(pos1); // A nossa pos1 tem col=5
        assertEquals(5, tinyShip.getLeftMostPos(), "Error: LeftMost should be 5.");
    }

    @Test
    @DisplayName("getLeftMostPos2: Path 2 - Itera mas condição if falsa (coluna maior)")
    void getLeftMostPos2() {
        // As posições base são (5,5) e (6,5). Nenhuma coluna é menor que 5.
        assertEquals(5, ship.getLeftMostPos(), "Error: LeftMost should remain 5.");
    }

    @Test
    @DisplayName("getLeftMostPos3: Path 3 - Itera e condição if verdadeira (coluna menor)")
    void getLeftMostPos3() {
        // Colocamos a segunda posição com coluna 2 (que é menor que 5)
        ship.getPositions().set(1, createMockPosition(5, 2, false));
        assertEquals(2, ship.getLeftMostPos(), "Error: LeftMost should update to 2.");
    }

    // ==========================================
    // getRightMostPos() - CC: 3
    // ==========================================

    @Test
    @DisplayName("getRightMostPos1: Path 1 - Tamanho 1 (Loop não itera)")
    void getRightMostPos1() {
        Ship tinyShip = new Ship("Tiny", Compass.NORTH, pos1, 1) {};
        tinyShip.getPositions().add(pos1);
        assertEquals(5, tinyShip.getRightMostPos(), "Error: RightMost should be 5.");
    }

    @Test
    @DisplayName("getRightMostPos2: Path 2 - Itera mas condição if falsa (coluna menor)")
    void getRightMostPos2() {
        assertEquals(5, ship.getRightMostPos(), "Error: RightMost should remain 5.");
    }

    @Test
    @DisplayName("getRightMostPos3: Path 3 - Itera e condição if verdadeira (coluna maior)")
    void getRightMostPos3() {
        ship.getPositions().set(1, createMockPosition(5, 9, false)); // Coluna 9 > 5
        assertEquals(9, ship.getRightMostPos(), "Error: RightMost should update to 9.");
    }

    // ==========================================
    // buildShip() - CC: 6 (Testar o switch todo)
    // ==========================================

    @Test
    @DisplayName("buildShip1: Path 1 - Constrói uma Barca")
    void buildShip1() {
        assertNotNull(Ship.buildShip("barca", Compass.NORTH, new Position(0,0)), "Error: expected Barge not to be null");
    }

    @Test
    @DisplayName("buildShip2: Path 2 - Constrói uma Caravela")
    void buildShip2() {
        assertNotNull(Ship.buildShip("caravela", Compass.NORTH, new Position(0,0)), "Error: expected Caravel not to be null");
    }

    @Test
    @DisplayName("buildShip3: Path 3 - Constrói uma Nau")
    void buildShip3() {
        assertNotNull(Ship.buildShip("nau", Compass.NORTH, new Position(0,0)), "Error: expected Carrack not to be null");
    }

    @Test
    @DisplayName("buildShip4: Path 4 - Constrói uma Fragata")
    void buildShip4() {
        assertNotNull(Ship.buildShip("fragata", Compass.NORTH, new Position(0,0)), "Error: expected Frigate not to be null");
    }

    @Test
    @DisplayName("buildShip5: Path 5 - Constrói um Galeão")
    void buildShip5() {
        assertNotNull(Ship.buildShip("galeao", Compass.NORTH, new Position(0,0)), "Error: expected Galleon not to be null");
    }

    @Test
    @DisplayName("buildShip6: Path 6 - Default branch retorna null para strings inválidas")
    void buildShip6() {
        assertNull(Ship.buildShip("submarino", Compass.NORTH, new Position(0,0)), "Error: expected null for unknown category");
    }

    // ==========================================
    // tooCloseTo(IShip) - CC: 3
    // ==========================================

    @Test
    @DisplayName("tooCloseToShip1: Path 1 - Outro navio sem posições (loop não itera)")
    void tooCloseToShip1() {
        Ship emptyShip = new Ship("Ghost", Compass.NORTH, pos1, 0) {};
        assertFalse(ship.tooCloseTo(emptyShip), "Error: expected false since other ship is empty");
    }

    @Test
    @DisplayName("tooCloseToShip2: Path 2 - Navios não estão perto")
    void tooCloseToShip2() {
        Ship farShip = new Ship("Far", Compass.NORTH, pos1, 1) {};
        farShip.getPositions().add(createMockPosition(10, 10, false));
        assertFalse(ship.tooCloseTo(farShip), "Error: expected false for distant ships");
    }

    @Test
    @DisplayName("tooCloseToShip3: Path 3 - Navios estão perto")
    void tooCloseToShip3() {
        Ship nearShip = new Ship("Near", Compass.NORTH, pos1, 1) {};
        nearShip.getPositions().add(createMockPosition(5, 6, false));
        assertTrue(ship.tooCloseTo(nearShip), "Error: expected true for adjacent ships");
    }

    // ==========================================
    // sink() & toString()
    // ==========================================

    @Test
    @DisplayName("sink1: Verifica se afunda marcando todas as posições como hit")
    void sink1() {
        ship.sink();
        assertAll("Verifica hit em todas as posições",
                () -> assertTrue(ship.getPositions().get(0).isHit(), "Error: first pos should be hit"),
                () -> assertTrue(ship.getPositions().get(1).isHit(), "Error: second pos should be hit")
        );
    }

    @Test
    @DisplayName("toString1: Garante que a formatação não lança erros")
    void toString1() {
        assertDoesNotThrow(() -> ship.toString(), "Error: toString shouldn't throw exceptions");
    }

    // ==========================================
    // getAdjacentPositions() - CC: 5
    // ==========================================

    @Test
    @DisplayName("getAdjacentPositions: Abrange as validações de exclusão de duplicação")
    void getAdjacentPositions1() {
        // Usamos instâncias reais de Position apenas aqui para facilitar os testes de adjacência cruzada
        Ship testShip = new Ship("Test", Compass.NORTH, new Position(5,5), 2) {};
        testShip.getPositions().add(new Position(5,5));
        testShip.getPositions().add(new Position(5,6));

        List<IPosition> adjacents = testShip.getAdjacentPositions();

        assertAll("Verifica lista de adjacências gerada",
                () -> assertFalse(adjacents.isEmpty(), "Error: list should not be empty"),
                // Não pode conter posições do próprio barco
                () -> assertFalse(adjacents.contains(new Position(5,5)), "Error: shouldn't contain own pos"),
                // Deve conter uma posição validamente adjacente
                () -> assertTrue(adjacents.contains(new Position(4,5)), "Error: should contain adjacent pos")
        );
    }
    // ==========================================
    // EXCEPTION & ASSERTION TESTS (Os 12% que faltavam!)
    // ==========================================

    @Test
    @DisplayName("constructor2: Path 2 - Category null lança NPE")
    void constructor2() {
        assertThrows(NullPointerException.class, () -> new Ship(null, Compass.NORTH, pos1, 2) {}, "Error: expected NullPointerException");
    }

    @Test
    @DisplayName("constructor3: Path 3 - Bearing null lança NPE")
    void constructor3() {
        assertThrows(NullPointerException.class, () -> new Ship("Test", null, pos1, 2) {}, "Error: expected NullPointerException");
    }

    @Test
    @DisplayName("constructor4: Path 4 - Position null lança NPE")
    void constructor4() {
        assertThrows(NullPointerException.class, () -> new Ship("Test", Compass.NORTH, null, 2) {}, "Error: expected NullPointerException");
    }

    @Test
    @DisplayName("buildShip7: Assert shipKind != null")
    void buildShip7() {
        assertThrows(AssertionError.class, () -> Ship.buildShip(null, Compass.NORTH, new Position(0,0)), "Error: expected AssertionError");
    }

    @Test
    @DisplayName("buildShip8: Assert bearing != null")
    void buildShip8() {
        assertThrows(AssertionError.class, () -> Ship.buildShip("barca", null, new Position(0,0)), "Error: expected AssertionError");
    }

    @Test
    @DisplayName("buildShip9: Assert pos != null")
    void buildShip9() {
        assertThrows(AssertionError.class, () -> Ship.buildShip("barca", Compass.NORTH, null), "Error: expected AssertionError");
    }

    @Test
    @DisplayName("occupies4: Assert pos != null")
    void occupies4() {
        assertThrows(AssertionError.class, () -> ship.occupies(null), "Error: expected AssertionError");
    }

    @Test
    @DisplayName("tooCloseToShip4: Assert other != null")
    void tooCloseToShip4() {
        assertThrows(AssertionError.class, () -> ship.tooCloseTo((IShip)null), "Error: expected AssertionError");
    }

    @Test
    @DisplayName("tooCloseToPos4: Assert pos != null")
    void tooCloseToPos4() {
        assertThrows(AssertionError.class, () -> ship.tooCloseTo((IPosition)null), "Error: expected AssertionError");
    }

    @Test
    @DisplayName("shoot4: Assert pos != null")
    void shoot4() {
        assertThrows(AssertionError.class, () -> ship.shoot(null), "Error: expected AssertionError");
    }

    @Test
    @DisplayName("shoot5: Assert pos.isInside()")
    void shoot5() {
        // Criamos uma posição propositadamente fora do tabuleiro
        IPosition outsidePos = new Position(100, 100) {
            @Override public boolean isInside() { return false; }
        };
        assertThrows(AssertionError.class, () -> ship.shoot(outsidePos), "Error: expected AssertionError for outside position");
    }

    @Test
    @DisplayName("getAdjacentPositions2: Garante a cobertura da duplicação de adjacentes no &&")
    void getAdjacentPositions2() {
        Ship testShip = new Ship("Test", Compass.NORTH, new Position(5,5), 2) {};
        // Colocamos duas posições separadas por 1 casa (ex: 5,5 e 5,7)
        // Ambas terão o (5,6) como posição adjacente. Quando o (5,7) tentar adicionar o (5,6),
        // a lista já o contém, o que força a condição !adjacentPositions.contains(adj) a ser FALSA.
        testShip.getPositions().add(new Position(5,5));
        testShip.getPositions().add(new Position(5,7));

        List<IPosition> adjacents = testShip.getAdjacentPositions();

        assertAll("Verifica lista gerada com interseção",
                () -> assertFalse(adjacents.isEmpty(), "Error: list should not be empty"),
                () -> assertTrue(adjacents.contains(new Position(5,6)), "Error: should contain the common adjacent pos")
        );
    }
}