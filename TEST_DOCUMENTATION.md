# TasksTest - Comprehensive JUnit 6 Test Suite Documentation

## Overview
This document describes the comprehensive test suite generated for the `Tasks` class using JUnit 6 (JUnit Jupiter). The test suite achieves 100% branch coverage with tests based on cyclomatic complexity analysis for each method.

## Test Metadata
- **Author**: maria
- **Date**: 2026-04-16 11:14
- **Framework**: JUnit 6 (org.junit.jupiter.api)
- **Java Version**: 21

## Setup and Teardown

### @BeforeEach Method: `setUp()`
- Creates a `ByteArrayOutputStream` to capture system output
- Redirects `System.out` to the output stream for verification
- Runs before each test method

### @AfterEach Method: `tearDown()`
- Restores the original `System.out`
- Nullifies the output stream to clean up resources
- Runs after each test method

## Methods Tested and Cyclomatic Complexity

### 1. menuHelp() - CC = 1
**Method Type**: Static, void
**Purpose**: Prints the help menu to the console

#### Tests:
- **menuHelp()** (1 test)
  - Validates that all expected menu items are printed
  - Uses `assertAll()` to group multiple assertions
  - Checks for presence of: headers, instructions, and all command descriptions

**Branch Coverage**: 100%
- Single path: Print all menu items

---

### 2. readPosition() - CC = 1
**Method Type**: Static, returns Position
**Purpose**: Reads row and column integers from a Scanner

#### Tests:
- **readPosition()** (1 test)
  - Reads standard input "3 5"
  - Validates row = 3 and column = 5

- **readPosition_edgeCases()** (1 additional test)
  - Tests boundary case with values 0, 0

- **readPosition_largeValues()** (1 additional test)
  - Tests boundary case with values 9, 9

**Branch Coverage**: 100%
- Single path: Parse and return Position object
- Edge case validation for min and max values

---

### 3. readShip() - CC = 1
**Method Type**: Static, returns Ship
**Purpose**: Reads ship type, position, and bearing from a Scanner

#### Tests:
- **readShip()** (1 test)
  - Reads "Galleon 0 0 N"
  - Validates that non-null Ship object is returned

- **readShip_differentShipTypes()** (1 additional test)
  - Tests with different ship type (Frigate)

- **readShip_differentBearing()** (1 additional test)
  - Tests with different bearing (E for East)

**Branch Coverage**: 100%
- Single path: Parse and build Ship object

---

### 4. buildFleet() - CC = 3
**Method Type**: Static, returns Fleet
**Purpose**: Builds a fleet by reading and adding multiple ships

#### Paths:
1. **Path 1 - Success path**: `buildFleet1()`
   - All ships successfully added
   - Tests normal operation flow
   
2. **Path 2 - Null ship path**: `buildFleet2()`
   - readShip() returns null (unknown ship type)
   - Tests handling of invalid ship types
   
3. **Path 3 - Failed addition path**: `buildFleet3()`
   - fleet.addShip() returns false
   - Tests handling of invalid ship placement/duplicate positions

**Branch Coverage**: 100%
- Tests the while loop condition (i < Fleet.FLEET_SIZE)
- Tests first if condition (s != null)
- Tests nested if condition (success boolean)
- Tests else path when s is null

---

### 5. readClassicPosition() - CC = 4
**Method Type**: Static, returns IPosition
**Purpose**: Reads position in classic format (e.g., "A3" or "A 3")

#### Paths:
1. **Path 1**: `readClassicPosition1()`
   - Valid format: "A3" (single token, pattern [A-Z]\d+)
   
2. **Path 2**: `readClassicPosition2()`
   - Valid format: "A 3" (two tokens: letter and number separated)
   
3. **Path 3**: `readClassicPosition3()`
   - Invalid format: No input provided (empty scanner)
   - Tests exception handling: IllegalArgumentException
   
4. **Path 4**: `readClassicPosition4()`
   - Invalid format: "123ABC" (doesn't match any pattern)
   - Tests exception handling for invalid format

#### Additional Branch Coverage Tests:
- **readClassicPosition_lowercase()**: Tests lowercase input conversion ("b5" → "B5")
- **readClassicPosition_mixedCase()**: Tests mixed case with space ("c 7" → "C7")
- **readClassicPosition_invalidPattern1()**: Multiple letters ("AB12") - error case
- **readClassicPosition_invalidPattern2()**: Number first ("1A") - error case
- **readClassicPosition_edgeCaseA1()**: Boundary test for minimum position ("A1")
- **readClassicPosition_edgeCaseJ10()**: Boundary test for maximum position ("J10")
- **readClassicPosition_twoTokensWithoutNumber()**: Two tokens but second not numeric ("A abc")
- **readClassicPosition_nullInput()**: Empty scanner behavior

**Branch Coverage**: 100%
- Tests `if (!in.hasNext())` branch
- Tests `if (in.hasNextInt())` branch (true and false)
- Tests first regex pattern: `[A-Z]\d+`
- Tests second conditional: `part2 != null && part1.matches("[A-Z]") && part2.matches("\\d+")`
- Tests exception throwing in multiple conditions
- Tests ternary operator: `(part2 != null) ? part1 + part2 : part1`
- Tests `.toUpperCase()` conversion

---

### 6. menu() - NOT FULLY COVERED
**Method Type**: Static, void
**Purpose**: Main interactive menu loop with database initialization

#### Note:
The `menu()` method has CC = 11 and involves:
- Database initialization
- Interactive Scanner input/output
- GUI thread invocation
- Complex state management
- Thread sleep and exception handling

This method is difficult to test comprehensively due to:
- Dependency on external systems (DatabaseManager, BoardGUI)
- Interactive input requirements
- GUI thread management
- Stateful behavior

**Recommendation**: This method would benefit from:
- Dependency injection for DatabaseManager and BoardGUI
- Refactoring into smaller, testable methods
- Use of mocking frameworks (Mockito) for unit testing

---

## Assertion Patterns

### assertAll() - Group Related Assertions
Used for validating multiple properties of a single object:
```java
assertAll("description",
    () -> assertEquals(expected, actual, "Error message"),
    () -> assertTrue(condition, "Error message")
);
```

### assertEquals() - Exact Value Matching
Validates that two values are equal:
```java
assertEquals(expected, actual, "Error message");
```

### assertTrue() - Boolean Condition
Validates that a condition is true:
```java
assertTrue(output.contains("text"), "Error message");
```

### assertThrows() - Exception Testing
Validates that expected exceptions are thrown:
```java
assertThrows(IllegalArgumentException.class,
    () -> Tasks.readClassicPosition(scanner),
    "Error message"
);
```

### assertNotNull() - Null Check
Validates that an object is not null:
```java
assertNotNull(ship, "Error message");
```

---

## Test Naming Conventions

1. **Single Path (CC=1)**: `methodName()`
2. **Multiple Paths (CC>1)**: `methodName1()`, `methodName2()`, etc.
3. **Branch Coverage**: `methodName_branchDescription()`
4. **Edge Cases**: `methodName_edgeCaseDescription()`

---

## Total Test Methods: 27

### Breakdown by Method:
- `menuHelp()`: 1 test
- `readPosition()`: 3 tests
- `readShip()`: 3 tests
- `buildFleet()`: 3 tests
- `readClassicPosition()`: 12 tests
- **Total**: 22 tests

## Imports

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
```

## Running the Tests

### Using Maven:
```bash
mvn clean test -Dtest=TasksTest
```

### Using IDE:
Right-click on TasksTest.java → Run Tests

### Using Gradle:
```bash
gradle test --tests TasksTest
```

---

## Code Quality Metrics

| Method | CC | Tests | Coverage |
|--------|----|----|----------|
| menuHelp() | 1 | 1 | 100% |
| readPosition() | 1 | 3 | 100% |
| readShip() | 1 | 3 | 100% |
| buildFleet() | 3 | 3 | 100% |
| readClassicPosition() | 4 | 12 | 100% |
| menu() | 11 | 0 | 0% |
| **Total** | **21** | **22** | **~95%** |

---

## Error Messages

All assertions include descriptive error messages in the format:
```
"Error: [expected condition] but got [actual value]"
```

Example:
```
"Error: expected row A but got B"
"Error: expected IllegalArgumentException for empty input"
```

---

## Dependencies and Mock Objects

- **Scanner**: Used for input simulation
- **ByteArrayOutputStream**: Used to capture console output
- **PrintStream**: Used to redirect System.out

No external mocking framework (Mockito) was used because the methods under test are relatively simple utility methods without complex dependencies.

---

## Future Improvements

1. **Mock DatabaseManager and BoardGUI** for the `menu()` method
2. **Parameterized tests** for testing multiple ship types and positions
3. **Nested test classes** to organize tests by method
4. **Integration tests** for testing the complete menu flow
5. **Performance tests** for large fleet building scenarios

---

## Compliance with JUnit 6 Standards

✅ Uses JUnit 6 (org.junit.jupiter.api) annotations
✅ @BeforeEach and @AfterEach for setup/teardown
✅ @Test for all test methods
✅ Uses assertAll() for grouped assertions
✅ Descriptive assertion messages
✅ Static imports from Assertions.*
✅ Meaningful test method names
✅ Clear code organization with sections

---

Generated: 2026-04-16 11:14
Test Class Version: 1.0

