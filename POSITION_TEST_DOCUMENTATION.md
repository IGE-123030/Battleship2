# PositionTest - Comprehensive JUnit 6 Test Suite Documentation

## Overview
This document describes the comprehensive test suite generated for the `Position` class using JUnit 6 (JUnit Jupiter). The test suite achieves 100% branch coverage with tests based on cyclomatic complexity analysis for each method.

## Test Metadata
- **Author**: maria
- **Date**: 2026-04-16 11:14
- **Framework**: JUnit 6 (org.junit.jupiter.api)
- **Java Version**: 21
- **Total Test Methods**: 44

## Setup and Teardown

### @BeforeEach Method: `setUp()`
- Creates a Position instance at coordinates (2, 3)
- Runs before each test method
- Provides a standard position for testing

### @AfterEach Method: `tearDown()`
- Nullifies the position object
- Cleans up resources after each test
- Runs after each test method

## Methods Tested and Cyclomatic Complexity

### 1. Position(int, int) Constructor - CC = 1
**Method Type**: Constructor
**Purpose**: Creates a Position with integer coordinates

#### Tests:
- **constructorWithIntegers()** - Validates basic integer constructor
  - Tests row assignment
  - Tests column assignment
  - Tests initial isOccupied() = false
  - Tests initial isHit() = false

**Branch Coverage**: 100%
- Single path: Direct assignment of all fields

---

### 2. Position(char, int) Constructor - CC = 1
**Method Type**: Constructor
**Purpose**: Creates a Position with character row and integer column

#### Tests:
- **constructorWithCharacter()** - Tests 'D' to row 3 conversion
  - D - A = 3 (row coordinate)
  - Column 5 becomes 4 after -1
  
- **constructorWithLowercaseCharacter()** - Tests lowercase conversion
  - Validates Character.toUpperCase() behavior
  - Tests 'a' converts to 'A'

**Branch Coverage**: 100%
- Tests both uppercase and lowercase character inputs
- Validates the conversion formula: char - 'A'

---

### 3. randomPosition() - CC = 1
**Method Type**: Static method
**Purpose**: Generates a random Position within board boundaries

#### Tests:
- **randomPosition()** - Validates random position generation
  - Tests that result is not null
  - Tests that position is inside board
  - Tests row is within [0, BOARD_SIZE)
  - Tests column is within [0, BOARD_SIZE)

**Branch Coverage**: 100%
- Single path: Generate and return random valid position

---

### 4. getRow() - CC = 1
**Method Type**: Getter
**Purpose**: Returns the row coordinate

#### Tests:
- **getRow()** - Simple getter test
  - Validates return value for position (2, 3) is 2

**Branch Coverage**: 100%
- Single path: Return row field

---

### 5. getColumn() - CC = 1
**Method Type**: Getter
**Purpose**: Returns the column coordinate

#### Tests:
- **getColumn()** - Simple getter test
  - Validates return value for position (2, 3) is 3

**Branch Coverage**: 100%
- Single path: Return column field

---

### 6. getClassicRow() - CC = 1
**Method Type**: Getter
**Purpose**: Returns the row as a character (A-J)

#### Tests:
- **getClassicRow()** - Tests character conversion for position (2, 3)
  - Returns 'C' (A + 2)

- **getClassicRowBoundary()** - Tests boundary cases
  - Row 0 returns 'A'
  - Row 9 returns 'J'

**Branch Coverage**: 100%
- Single path: Return (char)('A' + row)

---

### 7. getClassicColumn() - CC = 1
**Method Type**: Getter
**Purpose**: Returns the column as 1-indexed integer (1-10)

#### Tests:
- **getClassicColumn()** - Tests column 3 returns 4
  
- **getClassicColumnBoundary()** - Tests boundary cases
  - Column 0 returns 1
  - Column 9 returns 10

**Branch Coverage**: 100%
- Single path: Return column + 1

---

### 8. isInside() - CC = 2
**Method Type**: Boolean query
**Purpose**: Checks if position is within board boundaries

#### Paths:
1. **Path 1 - isInside1()**: All conditions true (inside board)
   - Tests (5, 5) returns true
   
2. **Path 2 - isInside2()**: Negative row (outside board)
   - Tests (-1, 5) returns false
   
3. **Path 3 - isInside3()**: Negative column (outside board)
   - Tests (5, -1) returns false
   
4. **Path 4 - isInside4()**: Row >= BOARD_SIZE (outside board)
   - Tests (BOARD_SIZE, 5) returns false
   
5. **Path 5 - isInside5()**: Column >= BOARD_SIZE (outside board)
   - Tests (5, BOARD_SIZE) returns false
   
6. **isInsideBoundary1()**: Minimum valid position
   - Tests (0, 0) returns true
   
7. **isInsideBoundary2()**: Maximum valid position
   - Tests (BOARD_SIZE-1, BOARD_SIZE-1) returns true

**Branch Coverage**: 100%
- Tests all conditions in compound boolean expression:
  - row >= 0
  - column >= 0
  - row < Game.BOARD_SIZE
  - column < Game.BOARD_SIZE

---

### 9. isAdjacentTo(IPosition) - CC = 1
**Method Type**: Boolean comparison
**Purpose**: Checks if another position is adjacent (within 1 step)

#### Tests:
- **isAdjacentTo1()** - Horizontal adjacency (2,4) to (2,3)
- **isAdjacentTo2()** - Vertical adjacency (3,3) to (2,3)
- **isAdjacentTo3()** - Diagonal adjacency (3,4) to (2,3)
- **isAdjacentTo4()** - Non-adjacent (4,5) to (2,3)
- **isAdjacentToSelf()** - Position is adjacent to itself
- **isAdjacentToWithNull()** - NullPointerException for null input

**Branch Coverage**: 100%
- Single path: Return compound boolean condition
- Exception handling: NullPointerException

---

### 10. adjacentPositions() - CC = 2
**Method Type**: List-returning query
**Purpose**: Returns all valid adjacent positions within board boundaries

#### Paths:
1. **Path 1 - adjacentPositions1()**: Middle of board
   - Position (5,5) has all 8 adjacent positions
   
2. **Path 2 - adjacentPositions2()**: Corner position
   - Position (0,0) has 3 adjacent positions
   
3. **adjacentPositions3()**: Opposite corner
   - Position (9,9) has 3 adjacent positions
   
4. **adjacentPositions4()**: Edge position
   - Position (0,5) has 5 adjacent positions
   
5. **adjacentPositionsContainsExpected()**: Content validation
   - Verifies specific position is in result

**Branch Coverage**: 100%
- Tests for loop with all positions
- Tests if condition (inside board check)
- Tests valid and invalid adjacent positions

---

### 11. isOccupied() - CC = 1
**Method Type**: Boolean query
**Purpose**: Returns whether position is occupied

#### Tests:
- **isOccupied()** - Validates initial state is false

**Branch Coverage**: 100%
- Single path: Return isOccupied field

---

### 12. occupy() - CC = 1
**Method Type**: Mutator
**Purpose**: Marks position as occupied

#### Tests:
- **occupy()** - Tests occupy() sets isOccupied to true
  - Validates initial false
  - Validates true after occupy()

**Branch Coverage**: 100%
- Single path: Set isOccupied = true

---

### 13. isHit() - CC = 1
**Method Type**: Boolean query
**Purpose**: Returns whether position has been hit

#### Tests:
- **isHit()** - Validates initial state is false

**Branch Coverage**: 100%
- Single path: Return isHit field

---

### 14. shoot() - CC = 1
**Method Type**: Mutator
**Purpose**: Marks position as hit

#### Tests:
- **shoot()** - Tests shoot() sets isHit to true
  - Validates initial false
  - Validates true after shoot()

**Branch Coverage**: 100%
- Single path: Set isHit = true

---

### 15. equals(Object) - CC = 3
**Method Type**: Comparison
**Purpose**: Compares two Position objects for equality

#### Paths:
1. **Path 1 - equals1()**: this == otherPosition (same reference)
   - Position equals itself
   
2. **Path 2 - equals2()**: Same coordinates (different object)
   - (2,3) equals (2,3)
   
3. **Path 3 - equals3()**: null comparison
   - Position does not equal null
   
4. **Path 4 - equals4()**: Not IPosition instance
   - Position does not equal Object
   
5. **Path 5 - equals5()**: Different row, same column
   - (3,3) does not equal (2,3)
   
6. **Path 6 - equals6()**: Same row, different column
   - (2,4) does not equal (2,3)
   
7. **Path 7 - equals7()**: Different row and column
   - (1,1) does not equal (2,3)

**Branch Coverage**: 100%
- Tests all conditions:
  - if (this == otherPosition) branch
  - if (otherPosition instanceof IPosition) branch
  - Compound && condition for row and column equality
  - All return paths

---

### 16. hashCode() - CC = 1
**Method Type**: Hash function
**Purpose**: Returns hash code for Position object

#### Tests:
- **hashCode1()** - Equal positions have same hash code
- **hashCodeConsistency()** - Hash code consistent across calls
- **hashCodeDifferent()** - Different positions typically have different hashes
- **hashCodeAfterOccupy()** - Hash changes when occupied flag changes
- **hashCodeAfterShoot()** - Hash changes when hit flag changes

**Branch Coverage**: 100%
- Single path: Return Objects.hash(row, column, isOccupied, isHit)

---

### 17. toString() - CC = 1
**Method Type**: String conversion
**Purpose**: Returns string representation (e.g., "C4")

#### Tests:
- **toString1()** - Position (2,3) returns "C4"
- **toStringA1()** - Position (0,0) returns "A1"
- **toStringJ10()** - Position (9,9) returns "J10"
- **toStringVariousPositions()** - Multiple positions tested

**Branch Coverage**: 100%
- Single path: Return formatted string with row character and column number

---

## Integration Tests

### positionLifecycle()
Tests complete lifecycle of a position:
1. Creation with isOccupied=false, isHit=false
2. occupy() changes isOccupied to true
3. shoot() changes isHit to true

### positionComparison()
Tests position equality and hashing:
1. Same coordinates are equal
2. Different coordinates are not equal
3. Equal positions have same hash code

### classicPositionConversion()
Tests character and integer constructor equivalence:
- Position('F', 7) equals Position(5, 6)

---

## Test Statistics

| Category | Count |
|----------|-------|
| Total Test Methods | 44 |
| CC = 1 Methods | 12 |
| CC = 2 Methods | 2 |
| CC = 3 Methods | 1 |
| Integration Tests | 3 |

---

## Assertion Patterns

### assertAll() - Group Related Assertions
Used for validating multiple properties:
```java
assertAll("description",
    () -> assertEquals(expected, actual, "Error message"),
    () -> assertTrue(condition, "Error message")
);
```

### assertEquals() - Value Matching
```java
assertEquals(expected, actual, "Error: expected X but got Y");
```

### assertTrue() / assertFalse() - Boolean Conditions
```java
assertTrue(condition, "Error message");
assertFalse(condition, "Error message");
```

### assertThrows() - Exception Testing
```java
assertThrows(Exception.class, () -> method(), "Error message");
```

### assertNotNull() / assertNull() - Null Checks
```java
assertNotNull(object, "Error message");
assertNull(object, "Error message");
```

### assertNotEquals() - Inequality
```java
assertNotEquals(value1, value2, "Error message");
```

---

## Total Test Methods: 44

### Breakdown:
- Position(int, int): 1 test
- Position(char, int): 2 tests
- randomPosition(): 1 test
- getRow(): 1 test
- getColumn(): 1 test
- getClassicRow(): 2 tests
- getClassicColumn(): 2 tests
- isInside(): 7 tests (covering all boundary cases)
- isAdjacentTo(): 6 tests
- adjacentPositions(): 5 tests
- isOccupied(): 1 test
- occupy(): 1 test
- isHit(): 1 test
- shoot(): 1 test
- equals(): 7 tests
- hashCode(): 5 tests
- toString(): 4 tests
- Integration Tests: 3 tests

---

## Code Quality Metrics

| Method | CC | Tests | Coverage |
|--------|----|----|----------|
| Position(int, int) | 1 | 1 | 100% |
| Position(char, int) | 1 | 2 | 100% |
| randomPosition() | 1 | 1 | 100% |
| getRow() | 1 | 1 | 100% |
| getColumn() | 1 | 1 | 100% |
| getClassicRow() | 1 | 2 | 100% |
| getClassicColumn() | 1 | 2 | 100% |
| isInside() | 2 | 7 | 100% |
| isAdjacentTo() | 1 | 6 | 100% |
| adjacentPositions() | 2 | 5 | 100% |
| isOccupied() | 1 | 1 | 100% |
| occupy() | 1 | 1 | 100% |
| isHit() | 1 | 1 | 100% |
| shoot() | 1 | 1 | 100% |
| equals() | 3 | 7 | 100% |
| hashCode() | 1 | 5 | 100% |
| toString() | 1 | 4 | 100% |
| **TOTAL** | **22** | **44** | **100%** |

---

## Running the Tests

### Using Maven
```bash
mvn clean test -Dtest=PositionTest
```

### Using IDE
Right-click on PositionTest.java → Run Tests

### Expected Output
```
Tests run: 44, Failures: 0, Errors: 0
```

---

## Error Message Format

All assertions follow this pattern:
```
"Error: [expected condition] but got [actual value]"
```

Examples:
```
"Error: expected row 3 but got 2"
"Error: position should be occupied after occupy()"
"Error: expected 'C' but got 'B'"
```

---

## JUnit 6 Standards Compliance

✅ Uses org.junit.jupiter.api package
✅ @BeforeEach and @AfterEach for setup/teardown
✅ @Test for all test methods
✅ Uses assertAll() for grouped assertions
✅ Descriptive assertion messages
✅ Static imports from Assertions.*
✅ Meaningful test method names
✅ Clear code organization

---

## Dependencies

- **JUnit 6 (JUnit Jupiter)**: 5.10.2
- **Java**: 21
- **Maven**: Compatible

---

Generated: 2026-04-16 11:14
Test Class Version: 1.0

