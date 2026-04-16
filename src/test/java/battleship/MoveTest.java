package battleship;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Move.
 * Author: tiagobexiga
 * Date: 2026-04-16
 * Cyclomatic Complexity for each method:
 * - Move(): 1
 * - toString(): 1
 * - getNumber(): 1
 * - getShots(): 1
 * - getShotResults(): 1
 * - processEnemyFire(boolean): 7
 */
public class MoveTest {

    private Move move;
    private List<IPosition> shots;
    private List<IGame.ShotResult> shotResults;

    @BeforeEach
    void setUp() {
        shots = new ArrayList<>();
        shotResults = new ArrayList<>();
        move = new Move(1, shots, shotResults);
    }

    @AfterEach
    void tearDown() {
        move = null;
        shots = null;
        shotResults = null;
    }

    // =============================================
    // Move Constructor Tests - CC: 1
    // =============================================

    @Test
    void testConstructor() {
        assertAll("Constructor should initialize Move properly",
                () -> assertNotNull(move, "Error: Move instance should not be null"),
                () -> assertEquals(1, move.getNumber(), "Error: Move number should be 1"),
                () -> assertNotNull(move.getShots(), "Error: Shots list should be initialized"),
                () -> assertNotNull(move.getShotResults(), "Error: Shot results list should be initialized"),
                () -> assertTrue(move.getShots().isEmpty(), "Error: Shots list should be empty initially"),
                () -> assertTrue(move.getShotResults().isEmpty(), "Error: Shot results list should be empty initially")
        );
    }

    // =============================================
    // toString() Tests - CC: 1
    // =============================================

    @Test
    void testToString() {
        String result = move.toString();
        assertAll("toString() should return correct format",
                () -> assertNotNull(result, "Error: toString() should not return null"),
                () -> assertTrue(result.contains("Move{"), "Error: toString() should contain 'Move{'"),
                () -> assertTrue(result.contains("number=1"), "Error: toString() should contain move number"),
                () -> assertTrue(result.contains("shots=0"), "Error: toString() should contain shots count"),
                () -> assertTrue(result.contains("results=0"), "Error: toString() should contain results count")
        );
    }

    @Test
    void testToStringWithData() {
        shots.add(new Position(0, 0));
        shots.add(new Position(1, 1));
        shotResults.add(new IGame.ShotResult(true, false, null, false));

        Move moveWithData = new Move(2, shots, shotResults);
        String result = moveWithData.toString();

        assertAll("toString() should reflect list contents",
                () -> assertTrue(result.contains("shots=2"), "Error: toString() should show 2 shots"),
                () -> assertTrue(result.contains("results=1"), "Error: toString() should show 1 result")
        );
    }

    // =============================================
    // getNumber() Tests - CC: 1
    // =============================================

    @Test
    void testGetNumber() {
        assertEquals(1, move.getNumber(), "Error: getNumber() should return 1");
    }

    @Test
    void testGetNumberDifferentValues() {
        for (int i = 1; i <= 10; i++) {
            Move testMove = new Move(i, new ArrayList<>(), new ArrayList<>());
            assertEquals(i, testMove.getNumber(), "Error: getNumber() should return " + i);
        }
    }

    // =============================================
    // getShots() Tests - CC: 1
    // =============================================

    @Test
    void testGetShots() {
        List<IPosition> result = move.getShots();
        assertAll("getShots() should return correct list",
                () -> assertNotNull(result, "Error: getShots() should not return null"),
                () -> assertTrue(result.isEmpty(), "Error: getShots() should return empty list initially"),
                () -> assertSame(shots, result, "Error: getShots() should return same list reference")
        );
    }

    @Test
    void testGetShotsWithData() {
        IPosition pos1 = new Position(0, 0);
        IPosition pos2 = new Position(3, 4);
        shots.add(pos1);
        shots.add(pos2);

        List<IPosition> result = move.getShots();
        assertAll("getShots() should return populated list",
                () -> assertEquals(2, result.size(), "Error: getShots() should return 2 shots"),
                () -> assertTrue(result.contains(pos1), "Error: getShots() should contain first position"),
                () -> assertTrue(result.contains(pos2), "Error: getShots() should contain second position")
        );
    }

    // =============================================
    // getShotResults() Tests - CC: 1
    // =============================================

    @Test
    void testGetShotResults() {
        List<IGame.ShotResult> result = move.getShotResults();
        assertAll("getShotResults() should return correct list",
                () -> assertNotNull(result, "Error: getShotResults() should not return null"),
                () -> assertTrue(result.isEmpty(), "Error: getShotResults() should return empty list initially"),
                () -> assertSame(shotResults, result, "Error: getShotResults() should return same list reference")
        );
    }

    @Test
    void testGetShotResultsWithData() {
        IGame.ShotResult result1 = new IGame.ShotResult(true, false, null, false);
        IGame.ShotResult result2 = new IGame.ShotResult(true, false, null, true);
        shotResults.add(result1);
        shotResults.add(result2);

        List<IGame.ShotResult> results = move.getShotResults();
        assertAll("getShotResults() should return populated list",
                () -> assertEquals(2, results.size(), "Error: getShotResults() should return 2 results"),
                () -> assertTrue(results.contains(result1), "Error: getShotResults() should contain first result"),
                () -> assertTrue(results.contains(result2), "Error: getShotResults() should contain second result")
        );
    }

    // =============================================
    // processEnemyFire(boolean verbose) Tests - CC: 7
    // =============================================

    @Test
    void testProcessEnemyFire1() {
        // Empty results
        String result = move.processEnemyFire(false);
        assertNotNull(result, "Error: processEnemyFire() should return non-null JSON");
        assertTrue(result.contains("\"validShots\": 0"), "Error: JSON should show 0 valid shots");
        assertTrue(result.contains("\"repeatedShots\": 0"), "Error: JSON should show 0 repeated shots");
    }

    @Test
    void testProcessEnemyFire2() {
        shotResults.add(new IGame.ShotResult(true, false, null, false));
        shotResults.add(new IGame.ShotResult(true, false, null, false));

        String result = move.processEnemyFire(false);
        assertAll("processEnemyFire() should process valid missed shots",
                () -> assertNotNull(result, "Error: Should return JSON"),
                () -> assertTrue(result.contains("\"validShots\": 2"), "Error: Should show 2 valid shots"),
                () -> assertTrue(result.contains("\"missedShots\": 2"), "Error: Should show 2 missed shots")
        );
    }

    @Test
    void testProcessEnemyFire3() {
        shotResults.add(new IGame.ShotResult(true, true, null, false));
        shotResults.add(new IGame.ShotResult(true, true, null, false));

        String result = move.processEnemyFire(false);
        assertAll("processEnemyFire() should process repeated shots",
                () -> assertTrue(result.contains("\"validShots\": 0"), "Error: Should show 0 valid shots"),
                () -> assertTrue(result.contains("\"repeatedShots\": 2"), "Error: Should show 2 repeated shots")
        );
    }

    @Test
    void testProcessEnemyFire4() {
        shotResults.add(new IGame.ShotResult(false, false, null, false));

        String result = move.processEnemyFire(false);
        assertAll("processEnemyFire() should skip invalid shots",
                () -> assertTrue(result.contains("\"validShots\": 0"), "Error: Should show 0 valid shots"),
                () -> assertTrue(result.contains("\"outsideShots\": 3"), "Error: Should show 3 outside shots")
        );
    }

    @Test
    void testProcessEnemyFire5() {
        IShip ship = new Barge(Compass.NORTH, new Position(0, 0));
        shotResults.add(new IGame.ShotResult(true, false, ship, false));

        String result = move.processEnemyFire(false);
        assertAll("processEnemyFire() should process hits on ships",
                () -> assertTrue(result.contains("\"validShots\": 1"), "Error: Should show 1 valid shot"),
                () -> assertTrue(result.contains("\"hitsOnBoats\""), "Error: Should contain hitsOnBoats")
        );
    }

    @Test
    void testProcessEnemyFire6() {
        IShip ship = new Barge(Compass.NORTH, new Position(0, 0));
        ship.sink();
        shotResults.add(new IGame.ShotResult(true, false, ship, true));

        String result = move.processEnemyFire(false);
        assertAll("processEnemyFire() should process sunk ships",
                () -> assertTrue(result.contains("\"validShots\": 1"), "Error: Should show 1 valid shot"),
                () -> assertTrue(result.contains("\"sunkBoats\""), "Error: Should contain sunkBoats")
        );
    }

    @Test
    void testProcessEnemyFire7() {
        IShip ship = new Barge(Compass.NORTH, new Position(0, 0));
        shotResults.add(new IGame.ShotResult(true, false, ship, false));
        shotResults.add(new IGame.ShotResult(true, false, null, false));
        shotResults.add(new IGame.ShotResult(true, true, null, false));

        String result = move.processEnemyFire(true);
        assertAll("processEnemyFire() with verbose=true should process all results",
                () -> assertNotNull(result, "Error: Should return JSON"),
                () -> assertTrue(result.contains("\"validShots\": 2"), "Error: Should show 2 valid shots"),
                () -> assertTrue(result.contains("\"repeatedShots\": 1"), "Error: Should show 1 repeated shot"),
                () -> assertTrue(result.contains("\"missedShots\": 1"), "Error: Should show 1 missed shot")
        );
    }

    // --- NOVOS TESTES PARA GARANTIR 100% BRANCH COVERAGE NO VERBOSE ---

    /**
     * Test processEnemyFire() - Path 8: Verbose mode with ONLY repeated shots
     * Conditions: verbose = true, validShots == 0 && repeatedShots > 0
     */
    @Test
    void testProcessEnemyFireVerbose_OnlyRepeated() {
        shotResults.add(new IGame.ShotResult(true, true, null, false));
        String result = move.processEnemyFire(true);
        assertTrue(result.contains("\"repeatedShots\": 1"), "Error: Should show 1 repeated shot");
    }

    /**
     * Test processEnemyFire() - Path 9: Verbose mode with ONLY outside/invalid shots
     * Conditions: verbose = true, validShots == 0, repeatedShots == 0, outsideShots > 0
     */
    @Test
    void testProcessEnemyFireVerbose_OnlyOutside() {
        shotResults.add(new IGame.ShotResult(false, false, null, false));
        String result = move.processEnemyFire(true);
        assertTrue(result.contains("\"validShots\": 0"), "Error: Should show 0 valid shots");
        assertTrue(result.contains("\"outsideShots\""), "Error: Should register outside shots in JSON");
    }

    /**
     * Test processEnemyFire() - Path 10: Verbose mode with sunk boats and NO missed/repeated
     * Conditions: verbose = true, validShots > 0, missedShots == 0, repeatedShots == 0
     */
    @Test
    void testProcessEnemyFireVerbose_SunkBoatsNoMisses() {
        IShip ship = new Barge(Compass.NORTH, new Position(0, 0));
        ship.sink();
        shotResults.add(new IGame.ShotResult(true, false, ship, true));

        String result = move.processEnemyFire(true);
        assertTrue(result.contains("\"sunkBoats\""), "Error: Should process sunk boats");
        assertTrue(result.contains("\"missedShots\": 0"), "Error: Should show 0 missed shots");
    }

    // =============================================
    // Complex Scenario Tests
    // =============================================

    @Test
    void testProcessEnemyFireMultipleShips() {
        IShip barge = new Barge(Compass.NORTH, new Position(0, 0));
        IShip caravel = new Caravel(Compass.SOUTH, new Position(3, 3));

        barge.sink();
        caravel.sink();

        shotResults.add(new IGame.ShotResult(true, false, barge, true));
        shotResults.add(new IGame.ShotResult(true, false, caravel, true));
        shotResults.add(new IGame.ShotResult(true, false, null, false));

        String result = move.processEnemyFire(false);
        assertAll("processEnemyFire() should handle multiple ship types",
                () -> assertTrue(result.contains("\"sunkBoats\""), "Error: Should contain sunkBoats"),
                () -> assertTrue(result.contains("\"validShots\": 3"), "Error: Should show 3 valid shots"),
                () -> assertTrue(result.contains("\"missedShots\": 1"), "Error: Should show 1 missed shot")
        );
    }

    @Test
    void testProcessEnemyFireOutsideShots() {
        // NUMBER_SHOTS = 3, so if we have 1 valid, outsideShots = 3 - 1 - 0 = 2
        shotResults.add(new IGame.ShotResult(true, false, null, false));
        shotResults.add(new IGame.ShotResult(false, false, null, false));
        shotResults.add(new IGame.ShotResult(false, false, null, false));

        String result = move.processEnemyFire(false);
        assertTrue(result.contains("\"outsideShots\": 2"), "Error: Should show 2 outside shots");
    }

    @Test
    void testProcessEnemyFireReturnsValidJSON() {
        IShip ship = new Barge(Compass.NORTH, new Position(0, 0));
        shotResults.add(new IGame.ShotResult(true, false, ship, false));
        shotResults.add(new IGame.ShotResult(true, false, null, false));

        String result = move.processEnemyFire(false);
        assertAll("processEnemyFire() should return valid JSON",
                () -> assertTrue(result.startsWith("{"), "Error: JSON should start with '{'"),
                () -> assertTrue(result.endsWith("}"), "Error: JSON should end with '}'"),
                () -> assertTrue(result.contains("validShots"), "Error: JSON should contain validShots"),
                () -> assertTrue(result.contains("repeatedShots"), "Error: JSON should contain repeatedShots"),
                () -> assertTrue(result.contains("missedShots"), "Error: JSON should contain missedShots"),
                () -> assertTrue(result.contains("outsideShots"), "Error: JSON should contain outsideShots"),
                () -> assertTrue(result.contains("sunkBoats"), "Error: JSON should contain sunkBoats"),
                () -> assertTrue(result.contains("hitsOnBoats"), "Error: JSON should contain hitsOnBoats")
        );
    }

    @Test
    void testProcessEnemyFireVerboseConsistency() {
        IShip ship = new Barge(Compass.NORTH, new Position(0, 0));
        shotResults.add(new IGame.ShotResult(true, false, ship, false));

        String resultQuiet = move.processEnemyFire(false);

        // Create new move with same data
        Move move2 = new Move(1, shots, shotResults);
        String resultVerbose = move2.processEnemyFire(true);

        assertEquals(resultQuiet, resultVerbose, "Error: JSON output should be same regardless of verbose flag");
    }

    @Test
    void testProcessEnemyFireAllCombinations() {
        IShip ship1 = new Barge(Compass.NORTH, new Position(0, 0));
        IShip ship2 = new Caravel(Compass.SOUTH, new Position(2, 2));

        // Valid shot - missed
        shotResults.add(new IGame.ShotResult(true, false, null, false));
        // Valid shot - hit but not sunk
        shotResults.add(new IGame.ShotResult(true, false, ship1, false));
        // Valid shot - hit and sunk (on second call we'll have sunk ship)
        ship2.sink();
        shotResults.add(new IGame.ShotResult(true, false, ship2, true));

        String result = move.processEnemyFire(false);
        assertAll("processEnemyFire() should handle all combinations",
                () -> assertTrue(result.contains("\"validShots\": 3"), "Error: Should show 3 valid shots"),
                () -> assertTrue(result.contains("\"missedShots\": 1"), "Error: Should show 1 missed shot"),
                () -> assertTrue(result.contains("\"sunkBoats\""), "Error: Should show sunk boats"),
                () -> assertTrue(result.contains("\"hitsOnBoats\""), "Error: Should show hits on boats")
        );
    }

    @Test
    void testMoveLifecycle() {
        // Create move with initial data
        assertAll("Move creation",
                () -> assertEquals(1, move.getNumber(), "Error: Initial move number should be 1"),
                () -> assertTrue(move.getShots().isEmpty(), "Error: Initial shots should be empty"),
                () -> assertTrue(move.getShotResults().isEmpty(), "Error: Initial results should be empty")
        );

        // Add shots
        shots.add(new Position(0, 0));
        shots.add(new Position(1, 1));
        assertEquals(2, move.getShots().size(), "Error: Move should have 2 shots after addition");

        // Add results
        shotResults.add(new IGame.ShotResult(true, false, null, false));
        shotResults.add(new IGame.ShotResult(true, true, null, false));
        assertEquals(2, move.getShotResults().size(), "Error: Move should have 2 results after addition");

        // Process results
        String jsonResult = move.processEnemyFire(false);
        assertAll("Move processing",
                () -> assertNotNull(jsonResult, "Error: JSON result should not be null"),
                () -> assertTrue(jsonResult.contains("\"validShots\": 1"), "Error: Should have 1 valid shot")
        );
    }
}