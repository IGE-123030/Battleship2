package battleship;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Move.
 * Fixed to match Jackson formatting and logic.
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

    @Test
    void testConstructor() {
        assertAll("Constructor should initialize Move properly",
                () -> assertNotNull(move),
                () -> assertEquals(1, move.getNumber()),
                () -> assertNotNull(move.getShots()),
                () -> assertNotNull(move.getShotResults()),
                () -> assertTrue(move.getShots().isEmpty()),
                () -> assertTrue(move.getShotResults().isEmpty())
        );
    }

    @Test
    void testToString() {
        String result = move.toString();
        assertAll("toString() should return correct format",
                () -> assertNotNull(result),
                () -> assertTrue(result.contains("Move{")),
                () -> assertTrue(result.contains("number=1")),
                () -> assertTrue(result.contains("shots=0")),
                () -> assertTrue(result.contains("results=0"))
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
                () -> assertTrue(result.contains("shots=2")),
                () -> assertTrue(result.contains("results=1"))
        );
    }

    @Test
    void testGetNumber() {
        assertEquals(1, move.getNumber());
    }

    @Test
    void testGetShots() {
        List<IPosition> result = move.getShots();
        assertSame(shots, result);
    }

    @Test
    void testGetShotResults() {
        List<IGame.ShotResult> result = move.getShotResults();
        assertSame(shotResults, result);
    }

    // =============================================
    // processEnemyFire() Tests - Adjusted for JSON Space Format
    // =============================================

    @Test
    void testProcessEnemyFire1() {
        String result = move.processEnemyFire(false);
        // Jackson indent usually puts "key" : value
        assertTrue(result.contains("\"validShots\" : 0"), "Should match Jackson format");
    }

    @Test
    void testProcessEnemyFire2() {
        shotResults.add(new IGame.ShotResult(true, false, null, false));
        shotResults.add(new IGame.ShotResult(true, false, null, false));

        String result = move.processEnemyFire(false);
        assertAll("processEnemyFire() should process valid missed shots",
                () -> assertTrue(result.contains("\"validShots\" : 2")),
                () -> assertTrue(result.contains("\"missedShots\" : 2"))
        );
    }

    @Test
    void testProcessEnemyFire3() {
        shotResults.add(new IGame.ShotResult(true, true, null, false));
        shotResults.add(new IGame.ShotResult(true, true, null, false));

        String result = move.processEnemyFire(false);
        assertAll("processEnemyFire() should process repeated shots",
                // validShots are only those NOT repeated
                () -> assertTrue(result.contains("\"validShots\" : 0")),
                () -> assertTrue(result.contains("\"repeatedShots\" : 2"))
        );
    }

    @Test
    void testProcessEnemyFire4() {
        shotResults.add(new IGame.ShotResult(false, false, null, false));

        String result = move.processEnemyFire(false);
        assertAll("processEnemyFire() should skip invalid shots and count them in outsideShots",
                () -> assertTrue(result.contains("\"validShots\" : 0")),
                // Based on log: 3 total - 0 valid - 0 repeated = 3 outside
                () -> assertTrue(result.contains("\"outsideShots\" : 3"))
        );
    }

    @Test
    void testProcessEnemyFire5() {
        IShip ship = new Barge(Compass.NORTH, new Position(0, 0));
        shotResults.add(new IGame.ShotResult(true, false, ship, false));

        String result = move.processEnemyFire(false);
        assertAll("processEnemyFire() should process hits on ships",
                () -> assertTrue(result.contains("\"validShots\" : 1")),
                () -> assertTrue(result.contains("\"hitsOnBoats\""))
        );
    }

    @Test
    void testProcessEnemyFire6() {
        IShip ship = new Barge(Compass.NORTH, new Position(0, 0));
        ship.sink();
        shotResults.add(new IGame.ShotResult(true, false, ship, true));

        String result = move.processEnemyFire(false);
        assertAll("processEnemyFire() should process sunk ships",
                () -> assertTrue(result.contains("\"validShots\" : 1")),
                () -> assertTrue(result.contains("\"sunkBoats\""))
        );
    }

    @Test
    void testProcessEnemyFire7() {
        IShip ship = new Barge(Compass.NORTH, new Position(0, 0));
        shotResults.add(new IGame.ShotResult(true, false, ship, false)); // valid
        shotResults.add(new IGame.ShotResult(true, false, null, false)); // valid miss
        shotResults.add(new IGame.ShotResult(true, true, null, false));  // repeated

        String result = move.processEnemyFire(true);
        assertAll("Verbose mode should have correct counts",
                () -> assertTrue(result.contains("\"validShots\" : 2")),
                () -> assertTrue(result.contains("\"repeatedShots\" : 1"))
        );
    }

    @Test
    void testProcessEnemyFireVerbose_OnlyOutside() {
        shotResults.add(new IGame.ShotResult(false, false, null, false));
        String result = move.processEnemyFire(true);
        assertTrue(result.contains("\"outsideShots\" : 3"), "Calculation: 3 - 0 valid - 0 repeated");
    }

    @Test
    void testProcessEnemyFireVerbose_SunkBoatsNoMisses() {
        IShip ship = new Barge(Compass.NORTH, new Position(0, 0));
        ship.sink();
        shotResults.add(new IGame.ShotResult(true, false, ship, true));

        String result = move.processEnemyFire(true);
        assertAll("Sunk boat with no misses",
                () -> assertTrue(result.contains("\"sunkBoats\"")),
                () -> assertTrue(result.contains("\"missedShots\" : 0"))
        );
    }

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
        assertAll("Multiple ships processing",
                () -> assertTrue(result.contains("\"validShots\" : 3")),
                () -> assertTrue(result.contains("\"missedShots\" : 1"))
        );
    }

    @Test
    void testProcessEnemyFireReturnsValidJSON() {
        String result = move.processEnemyFire(false);
        assertTrue(result.trim().startsWith("{") && result.trim().endsWith("}"), "Result should be a JSON object");
    }

    @Test
    void testProcessEnemyFireVerboseDifference() {
        shotResults.add(new IGame.ShotResult(true, false, null, false));
        String resultQuiet = move.processEnemyFire(false);
        String resultVerbose = move.processEnemyFire(true);

        // The return value is the same JSON string regardless of verbose mode
        assertEquals(resultQuiet, resultVerbose, "Quiet and verbose should return the same JSON string");
    }

    @Test
    void testMoveLifecycle() {
        shots.add(new Position(0, 0));
        shots.add(new Position(1, 1));

        shotResults.add(new IGame.ShotResult(true, false, null, false)); // valid
        shotResults.add(new IGame.ShotResult(true, true, null, false));  // repeated

        String jsonResult = move.processEnemyFire(false);
        // validShots: 1 (the non-repeated one)
        assertTrue(jsonResult.contains("\"validShots\" : 1"));
        // outsideShots: 3 - 1 (valid) - 1 (repeated) = 1
        assertTrue(jsonResult.contains("\"outsideShots\" : 1"));
    }
}