package battleship;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

/**
 * Test class for Fleet.
 * Author: tiagobexiga
 * Date: 2026-04-16
 * Cyclomatic Complexity for each method:
 * - Fleet(): 1
 * - createRandom(): 2
 * - getShips(): 1
 * - addShip(): 4 (with null check & bounds)
 * - getShipsLike(): 4 (with null check & empty list)
 * - getFloatingShips(): 3 (with empty list)
 * - getSunkShips(): 3 (with empty list)
 * - shipAt(): 4 (with null check & empty list)
 * - isInsideBoard(): 6 (with null check)
 * - colisionRisk(): 4 (with null check & populated no collision)
 * - printShips(): 2 (with null check)
 * - printStatus(): 1
 * - printShipsByCategory(): 2 (with null check)
 * - printFloatingShips(): 1
 * - printAllShips(): 1
 */
public class FleetTest {

    private Fleet fleet;

    @BeforeEach
    void setUp() {
        fleet = new Fleet();
    }

    @AfterEach
    void tearDown() {
        fleet = null;
    }

    // =============================================
    // Fleet Constructor Tests - CC: 1
    // =============================================

    @Test
    void testConstructor() {
        assertAll("Constructor should initialize empty fleet",
                () -> assertNotNull(fleet, "Error: Fleet instance should not be null"),
                () -> assertNotNull(fleet.getShips(), "Error: Ships list should be initialized"),
                () -> assertTrue(fleet.getShips().isEmpty(), "Error: Fleet should be initialized with empty ships list"),
                () -> assertEquals(0, fleet.getShips().size(), "Error: Initial fleet size should be 0")
        );
    }

    // =============================================
    // addShip() Tests
    // =============================================

    @Test
    void testAddShip1_ValidShip() {
        IShip validShip = new Barge(Compass.NORTH, new Position(1, 1));
        assertTrue(fleet.addShip(validShip), "Error: Valid ship should be added successfully");
        assertEquals(1, fleet.getShips().size(), "Error: Fleet size should be 1 after adding ship");
        assertTrue(fleet.getShips().contains(validShip), "Error: Fleet should contain the added ship");
    }

    @Test
    void testAddShip2_SizeLimitExceeded() {
        // Para forçar (ships.size() <= FLEET_SIZE) a dar FALSO, a lista tem de ter um
        // tamanho estritamente MAIOR que o FLEET_SIZE. Contornamos o addShip para encher a lista.
        for (int i = 0; i <= IFleet.FLEET_SIZE; i++) {
            fleet.getShips().add(new Barge(Compass.NORTH, new Position(i, 0)));
        }

        IShip extraShip = new Barge(Compass.SOUTH, new Position(9, 9));
        assertFalse(fleet.addShip(extraShip), "Error: Should not add ship when fleet exceeds FLEET_SIZE limit");
    }

    @Test
    void testAddShip3_OutsideBoard() {
        IShip shipOutsideRight = new Barge(Compass.NORTH, new Position(0, Game.BOARD_SIZE + 5));
        assertFalse(fleet.addShip(shipOutsideRight), "Error: Should not add ship outside right boundary");

        IShip shipOutsideBottom = new Barge(Compass.EAST, new Position(Game.BOARD_SIZE + 5, 0));
        assertFalse(fleet.addShip(shipOutsideBottom), "Error: Should not add ship outside bottom boundary");
    }

    @Test
    void testAddShip4_CollisionRisk() {
        IShip ship1 = new Barge(Compass.NORTH, new Position(2, 2));
        fleet.addShip(ship1);
        assertEquals(1, fleet.getShips().size(), "Error: First ship should be added");

        // Try to add ship too close to first one
        IShip ship2 = new Barge(Compass.NORTH, new Position(2, 3));
        assertFalse(fleet.addShip(ship2), "Error: Should not add ship with collision risk");
        assertEquals(1, fleet.getShips().size(), "Error: Fleet size should remain 1");
    }

    @Test
    void testAddShip5_NullShip() {
        assertThrows(AssertionError.class, () -> fleet.addShip(null),
                "Error: Should throw AssertionError when ship is null");
    }

    // =============================================
    // getShips() Tests
    // =============================================

    @Test
    void testGetShips_Empty() {
        assertTrue(fleet.getShips().isEmpty(), "Error: Initial ships list should be empty");
        assertEquals(0, fleet.getShips().size(), "Error: Initial fleet size should be 0");
    }

    @Test
    void testGetShips_AfterAddition() {
        IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
        IShip ship2 = new Caravel(Compass.SOUTH, new Position(5, 5));

        fleet.addShip(ship1);
        fleet.addShip(ship2);

        List<IShip> ships = fleet.getShips();
        assertEquals(2, ships.size(), "Error: Fleet should contain 2 ships");
        assertTrue(ships.contains(ship1), "Error: Fleet should contain first ship");
        assertTrue(ships.contains(ship2), "Error: Fleet should contain second ship");
    }

    // =============================================
    // getShipsLike() Tests
    // =============================================

    @Test
    void testGetShipsLike1_MatchFound() {
        IShip barge1 = new Barge(Compass.NORTH, new Position(0, 0));
        IShip barge2 = new Barge(Compass.SOUTH, new Position(3, 3));
        IShip caravel = new Caravel(Compass.EAST, new Position(6, 6));

        fleet.addShip(barge1);
        fleet.addShip(barge2);
        fleet.addShip(caravel);

        List<IShip> barges = fleet.getShipsLike("Barca");
        assertEquals(2, barges.size(), "Error: Should find 2 Barca ships");
        assertTrue(barges.contains(barge1), "Error: First barge should be in result");
        assertTrue(barges.contains(barge2), "Error: Second barge should be in result");
        assertFalse(barges.contains(caravel), "Error: Caravel should not be in result");
    }

    @Test
    void testGetShipsLike2_NoMatch() {
        IShip barge = new Barge(Compass.NORTH, new Position(0, 0));
        fleet.addShip(barge);

        List<IShip> galleons = fleet.getShipsLike("Galeao");
        assertTrue(galleons.isEmpty(), "Error: Should find no Galeao ships");
    }

    @Test
    void testGetShipsLike3_NullCategory() {
        assertThrows(AssertionError.class, () -> fleet.getShipsLike(null),
                "Error: Should throw AssertionError when category is null");
    }

    @Test
    void testGetShipsLike4_EmptyFleet() {
        List<IShip> result = fleet.getShipsLike("Barca");
        assertTrue(result.isEmpty(), "Error: Should return empty list when fleet is empty");
    }

    // =============================================
    // getFloatingShips() Tests
    // =============================================

    @Test
    void testGetFloatingShips1_AllFloating() {
        IShip ship1 = new Barge(Compass.NORTH, new Position(0, 0));
        IShip ship2 = new Caravel(Compass.SOUTH, new Position(4, 4));

        fleet.addShip(ship1);
        fleet.addShip(ship2);

        List<IShip> floating = fleet.getFloatingShips();
        assertEquals(2, floating.size(), "Error: All ships should be floating initially");
        assertTrue(floating.contains(ship1), "Error: First ship should be floating");
        assertTrue(floating.contains(ship2), "Error: Second ship should be floating");
    }

    @Test
    void testGetFloatingShips2_SomeSunk() {
        IShip ship1 = new Barge(Compass.NORTH, new Position(0, 0));
        IShip ship2 = new Caravel(Compass.SOUTH, new Position(4, 4));

        fleet.addShip(ship1);
        fleet.addShip(ship2);

        // Sink ship1 completely
        ship1.sink();

        List<IShip> floating = fleet.getFloatingShips();
        assertEquals(1, floating.size(), "Error: Only ship2 should be floating after sinking ship1");
        assertFalse(floating.contains(ship1), "Error: Sunk ship1 should not be floating");
        assertTrue(floating.contains(ship2), "Error: Ship2 should still be floating");
    }

    @Test
    void testGetFloatingShips3_EmptyFleet() {
        assertTrue(fleet.getFloatingShips().isEmpty(), "Error: Should return empty list when fleet is empty");
    }

    // =============================================
    // getSunkShips() Tests
    // =============================================

    @Test
    void testGetSunkShips1_NoneSunk() {
        IShip ship1 = new Barge(Compass.NORTH, new Position(0, 0));
        IShip ship2 = new Caravel(Compass.SOUTH, new Position(4, 4));

        fleet.addShip(ship1);
        fleet.addShip(ship2);

        List<IShip> sunk = fleet.getSunkShips();
        assertTrue(sunk.isEmpty(), "Error: No ships should be sunk initially");
    }

    @Test
    void testGetSunkShips2_SomeSunk() {
        IShip ship1 = new Barge(Compass.NORTH, new Position(0, 0));
        IShip ship2 = new Caravel(Compass.SOUTH, new Position(4, 4));

        fleet.addShip(ship1);
        fleet.addShip(ship2);

        // Sink ship1
        ship1.sink();

        List<IShip> sunk = fleet.getSunkShips();
        assertEquals(1, sunk.size(), "Error: One ship should be sunk");
        assertTrue(sunk.contains(ship1), "Error: Ship1 should be in sunk list");
    }

    @Test
    void testGetSunkShips3_EmptyFleet() {
        assertTrue(fleet.getSunkShips().isEmpty(), "Error: Should return empty list when fleet is empty");
    }

    // =============================================
    // shipAt() Tests
    // =============================================

    @Test
    void testShipAt1_Found() {
        IShip ship = new Barge(Compass.NORTH, new Position(2, 2));
        fleet.addShip(ship);

        IPosition shipPosition = new Position(2, 2);
        IShip foundShip = fleet.shipAt(shipPosition);
        assertNotNull(foundShip, "Error: Should find ship at occupied position");
        assertEquals(ship, foundShip, "Error: Found ship should match added ship");
    }

    @Test
    void testShipAt2_NotFound() {
        IShip ship = new Barge(Compass.NORTH, new Position(2, 2));
        fleet.addShip(ship);

        IPosition emptyPosition = new Position(7, 7);
        IShip foundShip = fleet.shipAt(emptyPosition);
        assertNull(foundShip, "Error: Should return null for empty position");
    }

    @Test
    void testShipAt3_NullPosition() {
        assertThrows(AssertionError.class, () -> fleet.shipAt(null),
                "Error: Should throw AssertionError when position is null");
    }

    @Test
    void testShipAt4_EmptyFleet() {
        assertNull(fleet.shipAt(new Position(1, 1)), "Error: Should return null when fleet is empty");
    }

    // =============================================
    // isInsideBoard() Tests (Private Method)
    // =============================================

    @Test
    void testIsInsideBoardPath1_Valid() throws Exception {
        Method method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
        method.setAccessible(true);

        IShip insideShip = new Barge(Compass.NORTH, new Position(2, 2));
        assertTrue((Boolean) method.invoke(fleet, insideShip), "Error: Ship inside board should return true");
    }

    @Test
    void testIsInsideBoardPath2_OutsideLeft() throws Exception {
        Method method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
        method.setAccessible(true);

        IShip shipOutsideLeft = new Barge(Compass.EAST, new Position(0, -5));
        assertFalse((Boolean) method.invoke(fleet, shipOutsideLeft), "Error: Ship outside left boundary should return false");
    }

    @Test
    void testIsInsideBoardPath3_OutsideRight() throws Exception {
        Method method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
        method.setAccessible(true);

        IShip shipOutsideRight = new Barge(Compass.WEST, new Position(0, Game.BOARD_SIZE + 5));
        assertFalse((Boolean) method.invoke(fleet, shipOutsideRight), "Error: Ship outside right boundary should return false");
    }

    @Test
    void testIsInsideBoardPath4_OutsideTop() throws Exception {
        Method method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
        method.setAccessible(true);

        IShip shipOutsideTop = new Barge(Compass.SOUTH, new Position(-5, 2));
        assertFalse((Boolean) method.invoke(fleet, shipOutsideTop), "Error: Ship outside top boundary should return false");
    }

    @Test
    void testIsInsideBoardPath5_OutsideBottom() throws Exception {
        Method method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
        method.setAccessible(true);

        IShip shipOutsideBottom = new Barge(Compass.NORTH, new Position(Game.BOARD_SIZE + 5, 2));
        assertFalse((Boolean) method.invoke(fleet, shipOutsideBottom), "Error: Ship outside bottom boundary should return false");
    }

    @Test
    void testIsInsideBoardNull() throws Exception {
        Method method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
        method.setAccessible(true);

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> method.invoke(fleet, (IShip) null)
        );
        assertTrue(ex.getCause() instanceof AssertionError, "Error: Cause should be an AssertionError");
    }

    // =============================================
    // colisionRisk() Tests (Private Method)
    // =============================================

    @Test
    void testColisionRisk1_NoCollision() throws Exception {
        Method method = Fleet.class.getDeclaredMethod("colisionRisk", IShip.class);
        method.setAccessible(true);

        IShip ship = new Barge(Compass.NORTH, new Position(5, 5));
        assertFalse((Boolean) method.invoke(fleet, ship), "Error: No collision in empty fleet");
    }

    @Test
    void testColisionRisk2_CollisionDetected() throws Exception {
        Method method = Fleet.class.getDeclaredMethod("colisionRisk", IShip.class);
        method.setAccessible(true);

        IShip ship1 = new Barge(Compass.NORTH, new Position(2, 2));
        fleet.addShip(ship1);

        // Ship too close to ship1
        IShip ship2 = new Barge(Compass.NORTH, new Position(2, 3));
        assertTrue((Boolean) method.invoke(fleet, ship2), "Error: Collision should be detected for adjacent ship");
    }

    @Test
    void testColisionRisk3_PopulatedNoCollision() throws Exception {
        Method method = Fleet.class.getDeclaredMethod("colisionRisk", IShip.class);
        method.setAccessible(true);

        // Frota com 1 barco
        fleet.addShip(new Barge(Compass.NORTH, new Position(0, 0)));
        // Novo barco bem longe, não há colisão
        IShip newShip = new Barge(Compass.NORTH, new Position(8, 8));

        assertFalse((Boolean) method.invoke(fleet, newShip), "Error: Should return false when ships don't collide");
    }

    @Test
    void testColisionRiskNull() throws Exception {
        Method method = Fleet.class.getDeclaredMethod("colisionRisk", IShip.class);
        method.setAccessible(true);

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> method.invoke(fleet, (IShip) null)
        );
        assertTrue(ex.getCause() instanceof AssertionError, "Error: Cause should be an AssertionError");
    }

    // =============================================
    // printShips() Tests
    // =============================================

    @Test
    void testPrintShips1_Empty() {
        List<IShip> ships = new ArrayList<>();
        assertDoesNotThrow(() -> fleet.printShips(ships), "Error: Should not throw exception for empty list");
    }

    @Test
    void testPrintShips2_WithShips() {
        List<IShip> ships = new ArrayList<>();
        ships.add(new Barge(Compass.NORTH, new Position(1, 1)));
        ships.add(new Caravel(Compass.SOUTH, new Position(4, 4)));

        assertDoesNotThrow(() -> fleet.printShips(ships), "Error: Should not throw exception for ships list");
    }

    @Test
    void testPrintShipsNull() {
        assertThrows(AssertionError.class, () -> fleet.printShips(null),
                "Error: Should throw AssertionError when list is null");
    }

    // =============================================
    // printStatus() Tests
    // =============================================

    @Test
    void testPrintStatus() {
        IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
        fleet.addShip(ship);
        assertDoesNotThrow(fleet::printStatus, "Error: printStatus should not throw any exceptions");
    }

    // =============================================
    // printShipsByCategory() Tests
    // =============================================

    @Test
    void testPrintShipsByCategory_Valid() {
        IShip barge = new Barge(Compass.NORTH, new Position(1, 1));
        fleet.addShip(barge);
        assertDoesNotThrow(() -> fleet.printShipsByCategory("Barca"), "Error: Should not throw exception");
    }

    @Test
    void testPrintShipsByCategoryNull() {
        assertThrows(AssertionError.class, () -> fleet.printShipsByCategory(null),
                "Error: Should throw AssertionError when category is null");
    }

    // =============================================
    // printFloatingShips() Tests
    // =============================================

    @Test
    void testPrintFloatingShips() {
        IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
        fleet.addShip(ship);
        assertDoesNotThrow(fleet::printFloatingShips, "Error: Should not throw exception");
    }

    // =============================================
    // printAllShips() Tests
    // =============================================

    @Test
    void testPrintAllShips() {
        IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
        IShip ship2 = new Caravel(Compass.SOUTH, new Position(4, 4));
        fleet.addShip(ship1);
        fleet.addShip(ship2);
        assertDoesNotThrow(fleet::printAllShips, "Error: Should not throw exception");
    }

    // =============================================
    // createRandom() Tests
    // =============================================

    @Test
    void testCreateRandom1_NotEmpty() {
        IFleet randomFleet = Fleet.createRandom();
        assertNotNull(randomFleet, "Error: Random fleet should not be null");
        assertFalse(randomFleet.getShips().isEmpty(), "Error: Random fleet should contain ships");
    }

    @Test
    void testCreateRandom2_Composition() {
        IFleet randomFleet = Fleet.createRandom();
        assertEquals(IFleet.FLEET_SIZE, randomFleet.getShips().size(), "Error: Random fleet should have FLEET_SIZE ships");

        // Verify we have various ship types
        List<IShip> galleons = randomFleet.getShipsLike("Galeao");
        List<IShip> frigates = randomFleet.getShipsLike("Fragata");
        List<IShip> carracks = randomFleet.getShipsLike("Nau");
        List<IShip> caravels = randomFleet.getShipsLike("Caravela");
        List<IShip> barges = randomFleet.getShipsLike("Barca");

        assertEquals(1, galleons.size(), "Error: Should have 1 Galleon");
        assertEquals(1, frigates.size(), "Error: Should have 1 Frigate");
        assertEquals(2, carracks.size(), "Error: Should have 2 Carracks");
        assertEquals(3, caravels.size(), "Error: Should have 3 Caravels");
        assertEquals(4, barges.size(), "Error: Should have 4 Barges");
    }

    // =============================================
    // Integration Tests
    // =============================================

    @Test
    void testIntegrationMultipleShips() {
        IShip barge = new Barge(Compass.NORTH, new Position(0, 0));
        IShip caravel = new Caravel(Compass.SOUTH, new Position(3, 3));
        IShip carrack = new Carrack(Compass.EAST, new Position(6, 6));

        assertAll("Adding multiple ships",
                () -> assertTrue(fleet.addShip(barge), "Error: Barge should be added"),
                () -> assertTrue(fleet.addShip(caravel), "Error: Caravel should be added"),
                () -> assertTrue(fleet.addShip(carrack), "Error: Carrack should be added"),
                () -> assertEquals(3, fleet.getShips().size(), "Error: Fleet should contain 3 ships"),
                () -> assertEquals(3, fleet.getFloatingShips().size(), "Error: All 3 ships should be floating"),
                () -> assertEquals(0, fleet.getSunkShips().size(), "Error: No ships should be sunk")
        );
    }

    @Test
    void testIntegrationSinkShips() {
        IShip barge = new Barge(Compass.NORTH, new Position(0, 0));
        IShip caravel = new Caravel(Compass.SOUTH, new Position(3, 3));

        fleet.addShip(barge);
        fleet.addShip(caravel);

        // Sink first ship
        barge.sink();

        assertAll("After sinking one ship",
                () -> assertEquals(1, fleet.getFloatingShips().size(), "Error: One ship should still be floating"),
                () -> assertEquals(1, fleet.getSunkShips().size(), "Error: One ship should be sunk"),
                () -> assertTrue(fleet.getSunkShips().contains(barge), "Error: Barge should be in sunk list"),
                () -> assertTrue(fleet.getFloatingShips().contains(caravel), "Error: Caravel should be in floating list")
        );
    }
}