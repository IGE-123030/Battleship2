package battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test class for Position.
 * Author: maria
 * Date: 2026-04-16 11:14
 * Cyclomatic Complexity:
 * - Position(char, int): 1
 * - Position(int, int): 1
 * - randomPosition(): 1
 * - getRow(): 1
 * - getColumn(): 1
 * - getClassicRow(): 1
 * - getClassicColumn(): 1
 * - isInside(): 2
 * - isAdjacentTo(IPosition): 1
 * - adjacentPositions(): 2
 * - isOccupied(): 1
 * - isHit(): 1
 * - occupy(): 1
 * - shoot(): 1
 * - equals(Object): 3
 * - hashCode(): 1
 * - toString(): 1
 */
public class PositionTest {
	private Position position;

	@BeforeEach
	void setUp() {
		position = new Position(2, 3);
	}

	@AfterEach
	void tearDown() {
		position = null;
	}

	// ==================== Position(int, int) Constructor - CC = 1 ====================
	@Test
	void constructorWithIntegers() {
		Position pos = new Position(5, 7);
		assertAll("Constructor with integers",
			() -> assertNotNull(pos, "Error: Position object should not be null"),
			() -> assertEquals(5, pos.getRow(), "Error: expected row 5 but got " + pos.getRow()),
			() -> assertEquals(7, pos.getColumn(), "Error: expected column 7 but got " + pos.getColumn()),
			() -> assertFalse(pos.isOccupied(), "Error: new position should not be occupied"),
			() -> assertFalse(pos.isHit(), "Error: new position should not be hit")
		);
	}

	// ==================== Position(char, int) Constructor - CC = 1 ====================
	@Test
	void constructorWithCharacter() {
		Position pos = new Position('D', 5);
		assertAll("Constructor with character",
			() -> assertNotNull(pos, "Error: Position object should not be null"),
			() -> assertEquals(3, pos.getRow(), "Error: expected row 3 (D-A) but got " + pos.getRow()),
			() -> assertEquals(4, pos.getColumn(), "Error: expected column 4 (5-1) but got " + pos.getColumn()),
			() -> assertFalse(pos.isOccupied(), "Error: new position should not be occupied"),
			() -> assertFalse(pos.isHit(), "Error: new position should not be hit")
		);
	}

	@Test
	void constructorWithLowercaseCharacter() {
		Position pos = new Position('a', 1);
		assertAll("Constructor with lowercase character converts to uppercase",
			() -> assertEquals(0, pos.getRow(), "Error: lowercase 'a' should convert to uppercase 'A' (row 0)"),
			() -> assertEquals(0, pos.getColumn(), "Error: column 1 should become 0 after -1")
		);
	}

	// ==================== randomPosition() - CC = 1 ====================
	@Test
	void randomPosition() {
		Position randomPos = Position.randomPosition();
		assertAll("randomPosition validation",
			() -> assertNotNull(randomPos, "Error: randomPosition should not return null"),
			() -> assertTrue(randomPos.isInside(), "Error: random position should be inside board"),
			() -> assertTrue(randomPos.getRow() >= 0 && randomPos.getRow() < Game.BOARD_SIZE, 
				"Error: random row should be within bounds"),
			() -> assertTrue(randomPos.getColumn() >= 0 && randomPos.getColumn() < Game.BOARD_SIZE,
				"Error: random column should be within bounds")
		);
	}

	// ==================== getRow() - CC = 1 ====================
	@Test
	void getRow() {
		assertEquals(2, position.getRow(), "Error: expected row 2 but got " + position.getRow());
	}

	// ==================== getColumn() - CC = 1 ====================
	@Test
	void getColumn() {
		assertEquals(3, position.getColumn(), "Error: expected column 3 but got " + position.getColumn());
	}

	// ==================== getClassicRow() - CC = 1 ====================
	@Test
	void getClassicRow() {
		assertEquals('C', position.getClassicRow(), "Error: expected classic row 'C' but got '" + position.getClassicRow() + "'");
	}

	@Test
	void getClassicRowBoundary() {
		Position posA = new Position(0, 0);
		Position posJ = new Position(9, 9);
		assertAll("Classic row boundary cases",
			() -> assertEquals('A', posA.getClassicRow(), "Error: row 0 should be 'A'"),
			() -> assertEquals('J', posJ.getClassicRow(), "Error: row 9 should be 'J'")
		);
	}

	// ==================== getClassicColumn() - CC = 1 ====================
	@Test
	void getClassicColumn() {
		assertEquals(4, position.getClassicColumn(), "Error: expected classic column 4 but got " + position.getClassicColumn());
	}

	@Test
	void getClassicColumnBoundary() {
		Position col1 = new Position(0, 0);
		Position col10 = new Position(0, 9);
		assertAll("Classic column boundary cases",
			() -> assertEquals(1, col1.getClassicColumn(), "Error: column 0 should return 1"),
			() -> assertEquals(10, col10.getClassicColumn(), "Error: column 9 should return 10")
		);
	}

	// ==================== isInside() - CC = 2 ====================
	// Path 1: All conditions true (inside board)
	@Test
	void isInside1() {
		position = new Position(5, 5);
		assertTrue(position.isInside(), "Error: position (5,5) should be inside board");
	}

	// Path 2: Negative row (outside board)
	@Test
	void isInside2() {
		position = new Position(-1, 5);
		assertFalse(position.isInside(), "Error: position with negative row should be outside board");
	}

	// Path 3: Negative column (outside board)
	@Test
	void isInside3() {
		position = new Position(5, -1);
		assertFalse(position.isInside(), "Error: position with negative column should be outside board");
	}

	// Path 4: Row >= BOARD_SIZE (outside board)
	@Test
	void isInside4() {
		position = new Position(Game.BOARD_SIZE, 5);
		assertFalse(position.isInside(), "Error: position with row >= BOARD_SIZE should be outside board");
	}

	// Path 5: Column >= BOARD_SIZE (outside board)
	@Test
	void isInside5() {
		position = new Position(5, Game.BOARD_SIZE);
		assertFalse(position.isInside(), "Error: position with column >= BOARD_SIZE should be outside board");
	}

	// Boundary cases
	@Test
	void isInsideBoundary1() {
		position = new Position(0, 0);
		assertTrue(position.isInside(), "Error: position (0,0) should be inside board");
	}

	@Test
	void isInsideBoundary2() {
		position = new Position(Game.BOARD_SIZE - 1, Game.BOARD_SIZE - 1);
		assertTrue(position.isInside(), "Error: position (" + (Game.BOARD_SIZE - 1) + "," + (Game.BOARD_SIZE - 1) + ") should be inside board");
	}

	// ==================== isAdjacentTo(IPosition) - CC = 1 ====================
	@Test
	void isAdjacentTo1() {
		Position other = new Position(2, 4);
		assertTrue(position.isAdjacentTo(other), "Error: horizontally adjacent position (2,4) should be detected");
	}

	@Test
	void isAdjacentTo2() {
		Position other = new Position(3, 3);
		assertTrue(position.isAdjacentTo(other), "Error: vertically adjacent position (3,3) should be detected");
	}

	@Test
	void isAdjacentTo3() {
		Position other = new Position(3, 4);
		assertTrue(position.isAdjacentTo(other), "Error: diagonally adjacent position (3,4) should be detected");
	}

	@Test
	void isAdjacentTo4() {
		Position other = new Position(4, 5);
		assertFalse(position.isAdjacentTo(other), "Error: non-adjacent position (4,5) should not be detected as adjacent");
	}

	@Test
	void isAdjacentToSelf() {
		assertTrue(position.isAdjacentTo(position), "Error: position should be adjacent to itself");
	}

	@Test
	void isAdjacentToWithNull() {
		assertThrows(NullPointerException.class, () -> position.isAdjacentTo(null),
			"Error: isAdjacentTo should throw NullPointerException for null input");
	}

	// ==================== adjacentPositions() - CC = 2 ====================
	// Path 1: Position in middle of board (all 8 adjacent positions inside)
	@Test
	void adjacentPositions1() {
		position = new Position(5, 5);
		List<IPosition> adjacents = position.adjacentPositions();
		assertAll("adjacentPositions for middle position",
			() -> assertNotNull(adjacents, "Error: adjacentPositions should not return null"),
			() -> assertEquals(8, adjacents.size(), "Error: position in middle should have 8 adjacent positions, got " + adjacents.size())
		);
	}

	// Path 2: Position at corner (only 3 adjacent positions inside)
	@Test
	void adjacentPositions2() {
		position = new Position(0, 0);
		List<IPosition> adjacents = position.adjacentPositions();
		assertAll("adjacentPositions for corner position",
			() -> assertNotNull(adjacents, "Error: adjacentPositions should not return null"),
			() -> assertEquals(3, adjacents.size(), "Error: corner position (0,0) should have 3 adjacent positions, got " + adjacents.size())
		);
	}

	@Test
	void adjacentPositions3() {
		position = new Position(Game.BOARD_SIZE - 1, Game.BOARD_SIZE - 1);
		List<IPosition> adjacents = position.adjacentPositions();
		assertEquals(3, adjacents.size(), "Error: corner position should have 3 adjacent positions, got " + adjacents.size());
	}

	@Test
	void adjacentPositions4() {
		position = new Position(0, 5);
		List<IPosition> adjacents = position.adjacentPositions();
		assertEquals(5, adjacents.size(), "Error: edge position (0,5) should have 5 adjacent positions, got " + adjacents.size());
	}

	@Test
	void adjacentPositionsContainsExpected() {
		position = new Position(5, 5);
		List<IPosition> adjacents = position.adjacentPositions();
		Position expectedAdjacent = new Position(4, 4);
		assertTrue(adjacents.contains(expectedAdjacent), "Error: adjacent list should contain position (4,4)");
	}

	// ==================== isOccupied() and occupy() - CC = 1 each ====================
	@Test
	void isOccupied() {
		assertFalse(position.isOccupied(), "Error: new position should not be occupied");
	}

	@Test
	void occupy() {
		assertFalse(position.isOccupied(), "Error: position should not be occupied initially");
		position.occupy();
		assertTrue(position.isOccupied(), "Error: position should be occupied after occupy()");
	}

	// ==================== isHit() and shoot() - CC = 1 each ====================
	@Test
	void isHit() {
		assertFalse(position.isHit(), "Error: new position should not be hit");
	}

	@Test
	void shoot() {
		assertFalse(position.isHit(), "Error: position should not be hit initially");
		position.shoot();
		assertTrue(position.isHit(), "Error: position should be hit after shoot()");
	}

	// ==================== equals(Object) - CC = 3 ====================
	// Path 1: this == otherPosition (same object reference)
	@Test
	void equals1() {
		assertTrue(position.equals(position), "Error: position should equal itself");
	}

	// Path 2: otherPosition instanceof IPosition && row and column match
	@Test
	void equals2() {
		Position same = new Position(2, 3);
		assertTrue(position.equals(same), "Error: positions with same coordinates should be equal");
	}

	// Path 3: otherPosition is null
	@Test
	void equals3() {
		assertFalse(position.equals(null), "Error: position should not equal null");
	}

	// Path 4: otherPosition is not IPosition instance
	@Test
	void equals4() {
		Object other = new Object();
		assertFalse(position.equals(other), "Error: position should not equal non-Position object");
	}

	// Path 5: Different row, same column
	@Test
	void equals5() {
		Position different = new Position(3, 3);
		assertFalse(position.equals(different), "Error: positions with different row should not be equal");
	}

	// Path 6: Same row, different column
	@Test
	void equals6() {
		Position different = new Position(2, 4);
		assertFalse(position.equals(different), "Error: positions with different column should not be equal");
	}

	// Path 7: Different row and column
	@Test
	void equals7() {
		Position different = new Position(1, 1);
		assertFalse(position.equals(different), "Error: positions with different coordinates should not be equal");
	}

	// ==================== hashCode() - CC = 1 ====================
	@Test
	void hashCode1() {
		Position same = new Position(2, 3);
		assertEquals(position.hashCode(), same.hashCode(),
			"Error: equal positions should have same hash code");
	}

	@Test
	void hashCodeConsistency() {
		int hash1 = position.hashCode();
		int hash2 = position.hashCode();
		assertEquals(hash1, hash2, "Error: hash code should be consistent across calls");
	}

	@Test
	void hashCodeDifferent() {
		Position different = new Position(1, 1);
		// Note: Different objects can have same hash code, but typically they don't
		// This test documents the behavior
		assertNotEquals(position.hashCode(), different.hashCode(),
			"Error: positions with different coordinates typically have different hash codes");
	}

	@Test
	void hashCodeAfterOccupy() {
		int hashBefore = position.hashCode();
		position.occupy();
		int hashAfter = position.hashCode();
		assertNotEquals(hashBefore, hashAfter, "Error: hash code should change when occupied flag changes");
	}

	@Test
	void hashCodeAfterShoot() {
		int hashBefore = position.hashCode();
		position.shoot();
		int hashAfter = position.hashCode();
		assertNotEquals(hashBefore, hashAfter, "Error: hash code should change when hit flag changes");
	}

	// ==================== toString() - CC = 1 ====================
	@Test
	void toString1() {
		String expected = "C4";
		assertEquals(expected, position.toString(),
			"Error: expected '" + expected + "' but got '" + position.toString() + "'");
	}

	@Test
	void toStringA1() {
		position = new Position(0, 0);
		String expected = "A1";
		assertEquals(expected, position.toString(),
			"Error: position (0,0) should format as 'A1' but got '" + position.toString() + "'");
	}

	@Test
	void toStringJ10() {
		position = new Position(9, 9);
		String expected = "J10";
		assertEquals(expected, position.toString(),
			"Error: position (9,9) should format as 'J10' but got '" + position.toString() + "'");
	}

	@Test
	void toStringVariousPositions() {
		assertAll("toString for various positions",
			() -> assertEquals("A2", new Position(0, 1).toString(), "Error: position (0,1) should be 'A2'"),
			() -> assertEquals("B3", new Position(1, 2).toString(), "Error: position (1,2) should be 'B3'"),
			() -> assertEquals("E5", new Position(4, 4).toString(), "Error: position (4,4) should be 'E5'")
		);
	}

	// ==================== Integration Tests ====================
	@Test
	void positionLifecycle() {
		Position test = new Position(3, 3);
		assertAll("Complete position lifecycle",
			() -> assertFalse(test.isOccupied(), "Error: new position should not be occupied"),
			() -> assertFalse(test.isHit(), "Error: new position should not be hit"),
			() -> assertTrue(test.isInside(), "Error: position (3,3) should be inside board")
		);
		
		test.occupy();
		assertTrue(test.isOccupied(), "Error: position should be occupied after occupy()");
		
		test.shoot();
		assertTrue(test.isHit(), "Error: position should be hit after shoot()");
	}

	@Test
	void positionComparison() {
		Position pos1 = new Position(4, 4);
		Position pos2 = new Position(4, 4);
		Position pos3 = new Position(5, 5);
		
		assertAll("Position comparison",
			() -> assertEquals(pos1, pos2, "Error: same coordinates should be equal"),
			() -> assertNotEquals(pos1, pos3, "Error: different coordinates should not be equal"),
			() -> assertEquals(pos1.hashCode(), pos2.hashCode(), "Error: equal positions should have same hash")
		);
	}

	@Test
	void classicPositionConversion() {
		Position charPos = new Position('F', 7);
		Position intPos = new Position(5, 6);
		assertEquals(charPos, intPos, "Error: Position('F', 7) should equal Position(5, 6)");
	}
}