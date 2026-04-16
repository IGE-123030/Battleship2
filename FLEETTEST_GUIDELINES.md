# FleetTest Guidelines and Examples

## Quick Start Guide

### File Location
```
/Users/tiagobexiga/IdeaProjects/Battleship2/src/test/java/battleship/FleetTest.java
```

### Running Tests

#### Run All Fleet Tests
```bash
mvn clean test -Dtest=FleetTest
```

#### Run Single Test
```bash
mvn clean test -Dtest=FleetTest#testAddShip1
```

#### Run with Coverage Report
```bash
mvn clean test -Dtest=FleetTest jacoco:report
```

## Test Execution Examples

### Example 1: Adding a Single Valid Ship

```java
@Test
void testAddShip1() {
    IShip validShip = new Barge(Compass.NORTH, new Position(1, 1));
    assertTrue(fleet.addShip(validShip), "Error: Valid ship should be added successfully");
    assertEquals(1, fleet.getShips().size(), "Error: Fleet size should be 1 after adding ship");
    assertTrue(fleet.getShips().contains(validShip), "Error: Fleet should contain the added ship");
}
```

**What This Tests:**
- Basic ship addition when all conditions are satisfied
- Verifies return value is true
- Confirms fleet size increased
- Verifies ship is stored correctly

**Expected Result:** ✓ PASS

---

### Example 2: Exceeding Fleet Size Limit

```java
@Test
void testAddShip2() {
    // Fill fleet to its maximum capacity
    for (int i = 0; i < IFleet.FLEET_SIZE; i++) {
        Position pos = new Position(i % 5, i / 2);
        IShip ship = new Barge(Compass.NORTH, pos);
        fleet.addShip(ship);
    }
    assertEquals(IFleet.FLEET_SIZE, fleet.getShips().size(), "Error: Fleet should be full");
    
    // Try to add one more ship - should fail due to size limit
    IShip extraShip = new Barge(Compass.SOUTH, new Position(9, 9));
    assertFalse(fleet.addShip(extraShip), "Error: Should not add ship when fleet is at FLEET_SIZE limit");
    assertEquals(IFleet.FLEET_SIZE, fleet.getShips().size(), "Error: Fleet size should remain unchanged");
}
```

**What This Tests:**
- Fleet respects maximum size limit (FLEET_SIZE = 11)
- Rejects ships when fleet is full
- Size doesn't increase on rejection

**Expected Result:** ✓ PASS

---

### Example 3: Testing Private Method via Reflection

```java
@Test
void testIsInsideBoardPath1() throws Exception {
    var method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
    method.setAccessible(true);
    
    IShip insideShip = new Barge(Compass.NORTH, new Position(2, 2));
    assertTrue((Boolean) method.invoke(fleet, insideShip), 
        "Error: Ship inside board should return true");
}
```

**What This Tests:**
- Private method can be tested via reflection
- Ship at valid position returns true
- Method boundary validation works correctly

**Expected Result:** ✓ PASS

---

### Example 4: Integration Test - Multiple Operations

```java
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
```

**What This Tests:**
- Multiple ships can be added successfully
- Fleet maintains correct size
- All ships initially floating
- No ships sunk initially

**Expected Result:** ✓ PASS

---

## Extending the Tests

### Adding a New Test for Existing Method

To add a new test for `addShip()` with a specific scenario:

```java
/**
 * Test addShip() - Path 5: (New scenario description)
 */
@Test
void testAddShip5() {
    // Setup
    IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
    fleet.addShip(ship1);
    
    // Action
    IShip ship2 = new Barge(Compass.SOUTH, new Position(3, 4));
    boolean result = fleet.addShip(ship2);
    
    // Assert
    assertTrue(result, "Error: description of expected behavior");
    assertEquals(2, fleet.getShips().size(), "Error: Fleet should contain 2 ships");
}
```

### Adding Tests for a New Method

If a new method is added to Fleet, follow this pattern:

```java
// =============================================
// newMethod() Tests - CC: N
// =============================================

/**
 * Test newMethod() - Path 1: (First scenario)
 */
@Test
void testNewMethod1() {
    // Setup phase
    // ... create test data ...
    
    // Action phase
    // ... call method ...
    
    // Assert phase
    // ... verify results ...
}

/**
 * Test newMethod() - Path 2: (Second scenario)
 */
@Test
void testNewMethod2() {
    // ... implementation ...
}
```

## Common Testing Patterns

### Pattern 1: State Verification
```java
@Test
void testStateChange() {
    // Verify initial state
    assertEquals(0, fleet.getShips().size(), "Initial state should be empty");
    
    // Change state
    IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
    fleet.addShip(ship);
    
    // Verify new state
    assertEquals(1, fleet.getShips().size(), "State should reflect added ship");
}
```

### Pattern 2: Boundary Testing
```java
@Test
void testBoundaryCondition() {
    // Test at boundary value
    for (int i = 0; i < IFleet.FLEET_SIZE; i++) {
        fleet.addShip(new Barge(Compass.NORTH, new Position(i % 5, i / 2)));
    }
    assertEquals(IFleet.FLEET_SIZE, fleet.getShips().size(), "Boundary value reached");
    
    // Test beyond boundary
    IShip extraShip = new Barge(Compass.NORTH, new Position(9, 9));
    assertFalse(fleet.addShip(extraShip), "Beyond boundary should be rejected");
}
```

### Pattern 3: Collection Verification
```java
@Test
void testCollectionContents() {
    IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
    IShip ship2 = new Caravel(Compass.SOUTH, new Position(4, 4));
    
    fleet.addShip(ship1);
    fleet.addShip(ship2);
    
    List<IShip> ships = fleet.getShips();
    assertEquals(2, ships.size(), "Should have 2 ships");
    assertTrue(ships.contains(ship1), "Should contain ship1");
    assertTrue(ships.contains(ship2), "Should contain ship2");
}
```

### Pattern 4: Grouped Assertions
```java
@Test
void testMultipleAssertions() {
    IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
    fleet.addShip(ship);
    
    assertAll("Verify ship addition",
        () -> assertNotNull(fleet.getShips(), "Ships list should not be null"),
        () -> assertEquals(1, fleet.getShips().size(), "Size should be 1"),
        () -> assertTrue(fleet.getShips().contains(ship), "Should contain ship"),
        () -> assertEquals(1, fleet.getFloatingShips().size(), "Should have 1 floating")
    );
}
```

## Debugging Failed Tests

### Test Fails: Expected true but got false

**Step 1:** Check the error message
```
Error: Valid ship should be added successfully
```

**Step 2:** Review the test setup
- Are positions valid? (0-9 range)
- Is fleet empty initially?
- Are all parameters correct?

**Step 3:** Check the method implementation
- Are all conditions being evaluated?
- Is the return value set correctly?

**Step 4:** Add debugging
```java
IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
System.out.println("Ship: " + ship);
System.out.println("Fleet size before: " + fleet.getShips().size());
boolean result = fleet.addShip(ship);
System.out.println("Result: " + result);
System.out.println("Fleet size after: " + fleet.getShips().size());
```

### Test Fails: NullPointerException

**Likely causes:**
- Ship not added to fleet (returns false)
- Position not in ship's positions list
- fleet.getShips() returned null

**Solution:**
```java
@Test
void testWithNullCheck() {
    IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
    
    // Verify preconditions
    assertNotNull(fleet, "Fleet should not be null");
    assertNotNull(fleet.getShips(), "Ships list should be initialized");
    assertTrue(fleet.getShips().isEmpty(), "Fleet should be empty");
    
    // Perform action
    boolean added = fleet.addShip(ship);
    assertTrue(added, "Ship should be added");
    
    // Safe access
    assertNotNull(fleet.getShips(), "Ships list should not be null after adding");
    assertEquals(1, fleet.getShips().size(), "Size should be 1");
}
```

## Performance Considerations

### Test Execution Time
- All 35 tests should complete in < 1 second
- Reflection adds minimal overhead
- No I/O operations involved

### Memory Usage
- Each test creates small object graphs
- @AfterEach ensures cleanup
- No memory leaks expected

## Best Practices

### ✓ DO:
- Write clear, descriptive test names
- Use meaningful assertions with error messages
- Test one logical path per test method
- Keep tests independent
- Use assertAll() for grouped assertions
- Document complex test scenarios

### ✗ DON'T:
- Share state between tests
- Use test execution order dependencies
- Skip @AfterEach cleanup
- Write multiple assertions without grouping
- Suppress exceptions without reason
- Test multiple scenarios in one method

## CI/CD Integration

### GitHub Actions Example
```yaml
name: Run Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 21
        uses: actions/setup-java@v2
        with:
          java-version: '21'
      - name: Run Fleet Tests
        run: mvn clean test -Dtest=FleetTest
```

### Local Pre-Commit Hook
```bash
#!/bin/bash
mvn clean test -Dtest=FleetTest
if [ $? -ne 0 ]; then
    echo "Tests failed, commit aborted"
    exit 1
fi
```

## Related Documentation

- **TEST_GENERATION_SUMMARY.md** - High-level overview
- **TECHNICAL_DOCUMENTATION.md** - Detailed technical analysis
- **Fleet.java** - Source code being tested
- **IFleet.java** - Fleet interface contract

## Contact & Support

For questions about the test suite:
1. Review TECHNICAL_DOCUMENTATION.md for detailed analysis
2. Check the test method comments for specific scenarios
3. Run individual tests with `-Dtest=FleetTest#testMethodName`
4. Use IDE debugging to step through execution

