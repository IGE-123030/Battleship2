package battleship;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import util.I18n;

class TasksTest {

    private void setInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    // =========================
    // MENU TESTS
    // =========================

    @Test
    @DisplayName("Menu - Todos comandos com game ativo")
    void testMenuAllCommandsWithGame() {
        String cGera = I18n.get("cmd.genfleet");
        String cStatus = I18n.get("cmd.status");
        String cMapa = I18n.get("cmd.map");
        String cAjuda = I18n.get("cmd.help");
        String cSair = I18n.get("cmd.surrender");

        setInput(
                cGera + "\n" +
                        cStatus + "\n" +
                        cMapa + "\n" +
                        cAjuda + "\n" +
                        cSair + "\n"
        );

        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - comandos sem frota (branches null)")
    void testMenuWithoutFleet() {
        String cStatus = I18n.get("cmd.status");
        String cMapa = I18n.get("cmd.map");
        String cSimula = I18n.get("cmd.simulate");
        String cSair = I18n.get("cmd.surrender");

        setInput(
                cStatus + "\n" +
                        cMapa + "\n" +
                        cSimula + "\n" +
                        cSair + "\n"
        );

        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - comando inválido")
    void testInvalidCommand() {
        String cSair = I18n.get("cmd.surrender");
        setInput("COMANDO_INVALIDO\n" + cSair + "\n");
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - TIROS sem game")
    void testShotsWithoutGame() {
        String cTiros = I18n.get("cmd.shots");
        String cSair = I18n.get("cmd.surrender");
        setInput(cTiros + "\n" + cSair + "\n");
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - TIROS com game")
    void testShotsWithGame() {
        String cGera = I18n.get("cmd.genfleet");
        String cTiros = I18n.get("cmd.shots");
        String cSair = I18n.get("cmd.surrender");
        setInput(cGera + "\n" + cTiros + "\n" + cSair + "\n");
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - GERAFROTA")
    void testMenuGenFleet() {
        setInput(I18n.get("cmd.genfleet") + "\n" + I18n.get("cmd.surrender") + "\n");
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - LEFROTA")
    void testMenuLoadFleet() {
        String navio = "BATTLESHIP";
        StringBuilder sb = new StringBuilder();
        sb.append(I18n.get("cmd.loadfleet")).append("\n");
        for (int i = 0; i < Fleet.FLEET_SIZE; i++) {
            sb.append(navio).append(" ").append(i).append(" ").append(i).append(" N\n");
        }
        sb.append(I18n.get("cmd.surrender")).append("\n");
        setInput(sb.toString());
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - STATUS com frota")
    void testMenuStatusWithFleet() {
        setInput(I18n.get("cmd.genfleet") + "\n" + I18n.get("cmd.status") + "\n" + I18n.get("cmd.surrender") + "\n");
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - STATUS sem frota")
    void testMenuStatusWithoutFleet() {
        setInput(I18n.get("cmd.status") + "\n" + I18n.get("cmd.surrender") + "\n");
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - MAPA com game")
    void testMenuMapWithGame() {
        setInput(I18n.get("cmd.genfleet") + "\n" + I18n.get("cmd.map") + "\n" + I18n.get("cmd.surrender") + "\n");
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - MAPA sem game")
    void testMenuMapWithoutGame() {
        setInput(I18n.get("cmd.map") + "\n" + I18n.get("cmd.surrender") + "\n");
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - AJUDA")
    void testMenuHelp1() {
        setInput(I18n.get("cmd.help") + "\n" + I18n.get("cmd.surrender") + "\n");
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - RAJADA sem game")
    void testVolleyWithoutGame() {
        String cRajada = I18n.get("cmd.volley");
        String cSair = I18n.get("cmd.surrender");
        setInput(cRajada + "\n" + cSair + "\n");
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - RAJADA com game")
    void testVolleyWithGame() {
        String cGera = I18n.get("cmd.genfleet");
        String cRajada = I18n.get("cmd.volley");
        String cSair = I18n.get("cmd.surrender");
        setInput(cGera + "\n" + cRajada + "\n" + "A1 B1 C1\n" + cSair + "\n");
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - RAJADA game over (true branch)")
    void testVolleyGameOver() {
        String cGera = I18n.get("cmd.genfleet");
        String cRajada = I18n.get("cmd.volley");
        String cSair = I18n.get("cmd.surrender");

        StringBuilder sb = new StringBuilder();
        sb.append(cGera).append("\n");
        for (int i = 0; i < 15; i++) {
            sb.append(cRajada).append("\n\nA1 B1 C1\n");
        }
        sb.append(cSair).append("\n");

        setInput(sb.toString());
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - SIMULA com game")
    void testSimulateWithGame() {
        String cGera = I18n.get("cmd.genfleet");
        String cSimula = I18n.get("cmd.simulate");
        String cSair = I18n.get("cmd.surrender");
        setInput(cGera + "\n" + cSimula + "\n" + cSair + "\n");
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    @Test
    @DisplayName("Menu - SIMULA sem game")
    void testSimulateWithoutGame() {
        String cSimula = I18n.get("cmd.simulate");
        String cSair = I18n.get("cmd.surrender");
        setInput(cSimula + "\n" + cSair + "\n");
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }

    // =========================
    // BUILD FLEET TESTS
    // =========================

    @Test
    @DisplayName("buildFleet - sucesso completo (all ships valid)")
    void testBuildFleetSuccess() {
        String navio = "BATTLESHIP";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Fleet.FLEET_SIZE; i++) {
            sb.append(navio).append(" ").append(i).append(" ").append(i).append(" N\n");
        }
        Scanner sc = new Scanner(new ByteArrayInputStream(sb.toString().getBytes()));
        assertDoesNotThrow(() -> {
            Fleet f = Tasks.buildFleet(sc);
            assertNotNull(f, "Fleet should not be null");
        });
    }

    @Test
    @DisplayName("buildFleet - s != null, success == true branch")
    void testBuildFleetShipNotNullSuccess() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Fleet.FLEET_SIZE; i++) {
            sb.append("BATTLESHIP").append(" ").append(i).append(" ").append(i).append(" N\n");
        }
        Scanner sc = new Scanner(new ByteArrayInputStream(sb.toString().getBytes()));
        Fleet f = Tasks.buildFleet(sc);
        assertNotNull(f);
    }

    @Test
    @DisplayName("buildFleet - s != null, success == false branch (collision)")
    void testBuildFleetShipCollision() {
        StringBuilder sb = new StringBuilder();
        sb.append("BATTLESHIP 0 0 N\n");
        sb.append("BATTLESHIP 0 0 N\n"); // Collision - same position
        for (int i = 2; i < Fleet.FLEET_SIZE + 2; i++) {
            sb.append("BATTLESHIP ").append(i).append(" ").append(i).append(" N\n");
        }
        Scanner sc = new Scanner(new ByteArrayInputStream(sb.toString().getBytes()));
        assertDoesNotThrow(() -> Tasks.buildFleet(sc));
    }

    @Test
    @DisplayName("buildFleet - s == null branch (invalid ship)")
    void testBuildFleetInvalidShip() {
        StringBuilder sb = new StringBuilder();
        sb.append("INVALID_SHIP 0 0 N\n");
        for (int i = 0; i < Fleet.FLEET_SIZE; i++) {
            sb.append("BATTLESHIP ").append(i).append(" ").append(i).append(" N\n");
        }
        Scanner sc = new Scanner(new ByteArrayInputStream(sb.toString().getBytes()));
        assertDoesNotThrow(() -> Tasks.buildFleet(sc));
    }

    // =========================
    // READ SHIP TESTS
    // =========================

    @Test
    @DisplayName("readShip - valid ship")
    void testReadShipValid() {
        Scanner sc = new Scanner("BATTLESHIP 0 0 N");
        assertDoesNotThrow(() -> {
            Ship s = Tasks.readShip(sc);
            assertNotNull(s);
        });
    }

    @Test
    @DisplayName("readShip - invalid bearing")
    void testReadShipInvalidBearing() {
        Scanner sc = new Scanner("BATTLESHIP 0 0 X");
        assertThrows(Exception.class, () -> Tasks.readShip(sc));
    }

    @Test
    @DisplayName("readShip - invalid ship type")
    void testReadShipInvalidType() {
        Scanner sc = new Scanner("INVALIDSHIP 0 0 N");
        assertDoesNotThrow(() -> {
            Ship s = Tasks.readShip(sc);
            assertNull(s);
        });
    }

    // =========================
    // READ POSITION TESTS
    // =========================

    @Test
    @DisplayName("readPosition - valid position")
    void testReadPositionValid() {
        Scanner sc = new Scanner("3 4");
        assertDoesNotThrow(() -> {
            Position p = Tasks.readPosition(sc);
            assertEquals(3, p.getRow());
            assertEquals(4, p.getColumn());
        });
    }

    @Test
    @DisplayName("readPosition - invalid input (non-integer)")
    void testReadPositionInvalid() {
        Scanner sc = new Scanner("A B");
        assertThrows(Exception.class, () -> Tasks.readPosition(sc));
    }

    @Test
    @DisplayName("readPosition - different coordinates")
    void testReadPositionDifferent() {
        Scanner sc = new Scanner("5 7");
        assertDoesNotThrow(() -> {
            Position p = Tasks.readPosition(sc);
            assertEquals(5, p.getRow());
            assertEquals(7, p.getColumn());
        });
    }

    // =========================
    // READ CLASSIC POSITION TESTS
    // =========================

    @Test
    @DisplayName("readClassicPosition - no input (throws)")
    void testReadClassicPositionNoInput() {
        Scanner sc = new Scanner("");
        assertThrows(IllegalArgumentException.class, () -> Tasks.readClassicPosition(sc));
    }

    @Test
    @DisplayName("readClassicPosition - format A1 (combined)")
    void testReadClassicPositionCombined() {
        Scanner sc = new Scanner("A1");
        assertDoesNotThrow(() -> {
            IPosition pos = Tasks.readClassicPosition(sc);
            assertNotNull(pos);
            assertEquals('A', pos.getClassicRow());
            assertEquals(1, pos.getClassicColumn());
        });
    }

    @Test
    @DisplayName("readClassicPosition - format A 1 (separated)")
    void testReadClassicPositionSeparated() {
        Scanner sc = new Scanner("A 1");
        assertDoesNotThrow(() -> {
            IPosition pos = Tasks.readClassicPosition(sc);
            assertNotNull(pos);
            assertEquals('A', pos.getClassicRow());
            assertEquals(1, pos.getClassicColumn());
        });
    }

    @Test
    @DisplayName("readClassicPosition - format B 5 (separated)")
    void testReadClassicPositionB5() {
        Scanner sc = new Scanner("B 5");
        assertDoesNotThrow(() -> {
            IPosition pos = Tasks.readClassicPosition(sc);
            assertNotNull(pos);
            assertEquals('B', pos.getClassicRow());
            assertEquals(5, pos.getClassicColumn());
        });
    }

    @Test
    @DisplayName("readClassicPosition - format C10 (combined, double digit)")
    void testReadClassicPositionC10() {
        Scanner sc = new Scanner("C10");
        assertDoesNotThrow(() -> {
            IPosition pos = Tasks.readClassicPosition(sc);
            assertNotNull(pos);
            assertEquals('C', pos.getClassicRow());
            assertEquals(10, pos.getClassicColumn());
        });
    }

    @Test
    @DisplayName("readClassicPosition - invalid format 11A (throws)")
    void testReadClassicPositionInvalid11A() {
        Scanner sc = new Scanner("11A");
        assertThrows(IllegalArgumentException.class, () -> Tasks.readClassicPosition(sc));
    }

    @Test
    @DisplayName("readClassicPosition - invalid format A B (throws)")
    void testReadClassicPositionInvalidAB() {
        Scanner sc = new Scanner("A B");
        assertThrows(IllegalArgumentException.class, () -> Tasks.readClassicPosition(sc));
    }

    @Test
    @DisplayName("readClassicPosition - lowercase format a1 (converted to A1)")
    void testReadClassicPositionLowercase() {
        Scanner sc = new Scanner("a1");
        assertDoesNotThrow(() -> {
            IPosition pos = Tasks.readClassicPosition(sc);
            assertNotNull(pos);
            assertEquals('A', pos.getClassicRow());
            assertEquals(1, pos.getClassicColumn());
        });
    }

    @Test
    @DisplayName("readClassicPosition - part2 null, part1 matches [A-Z]\\d+ (if branch)")
    void testReadClassicPositionPart2Null() {
        Scanner sc = new Scanner("D7");
        assertDoesNotThrow(() -> {
            IPosition pos = Tasks.readClassicPosition(sc);
            assertNotNull(pos);
            assertEquals('D', pos.getClassicRow());
            assertEquals(7, pos.getClassicColumn());
        });
    }

    @Test
    @DisplayName("readClassicPosition - part2 not null, both parts match (else if branch)")
    void testReadClassicPositionPart2NotNull() {
        Scanner sc = new Scanner("E 8");
        assertDoesNotThrow(() -> {
            IPosition pos = Tasks.readClassicPosition(sc);
            assertNotNull(pos);
            assertEquals('E', pos.getClassicRow());
            assertEquals(8, pos.getClassicColumn());
        });
    }

    @Test
    @DisplayName("readClassicPosition - invalid: part1 digit, part2 letter (else throws)")
    void testReadClassicPositionInvalidOrder() {
        Scanner sc = new Scanner("1 A");
        assertThrows(IllegalArgumentException.class, () -> Tasks.readClassicPosition(sc));
    }

    @Test
    @DisplayName("readClassicPosition - hasNextInt false (no second integer)")
    void testReadClassicPositionNoSecondInt() {
        Scanner sc = new Scanner("J X");
        assertThrows(IllegalArgumentException.class, () -> Tasks.readClassicPosition(sc));
    }

    // =========================
    // MENU HELP TEST
    // =========================

    @Test
    @DisplayName("menuHelp - execution test")
    void testMenuHelp() {
        assertDoesNotThrow(() -> Tasks.menuHelp());
    }

    // =========================
    // LOAD FLEET COMMAND TEST
    // =========================

    @Test
    @DisplayName("Menu - LEFROTA command full execution")
    void testLoadFleetCommand() {
        String cLe = I18n.get("cmd.loadfleet");
        String cSair = I18n.get("cmd.surrender");
        String navio = "BATTLESHIP";
        String[] rows = {"A","B","C","D","E","F","G","H","I","J"};
        StringBuilder sb = new StringBuilder();
        sb.append(cLe).append("\n");
        for (int i = 0; i < Fleet.FLEET_SIZE; i++) {
            sb.append(navio).append(" ").append(rows[i]).append(" ").append(i + 1).append(" N\n");
        }
        sb.append(cSair).append("\n");
        setInput(sb.toString());
        try {
            Tasks.menu();
        } catch (Exception ignored) {}
    }
}