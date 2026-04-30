package battleship;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang3.time.StopWatch;

import util.I18n;

public class Tasks {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String GOODBYE_MESSAGE = "msg.goodbye";

    private static final String AJUDA = "cmd.help";
    private static final String GERAFROTA = "cmd.genfleet";
    private static final String LEFROTA = "cmd.loadfleet";
    private static final String DESISTIR = "cmd.surrender";
    private static final String RAJADA = "cmd.volley";
    private static final String TIROS = "cmd.shots";
    private static final String MAPA = "cmd.map";
    private static final String STATUS = "cmd.status";
    private static final String SIMULA = "cmd.simulate";

    public static void menu() {

        // Inicialização da Base de Dados (Código do Mauro)
        DatabaseManager.setupDatabase();

        IFleet myFleet = null;
        IGame game = null;

        Scanner in = new Scanner(System.in);

        menuHelp();

        String command = readCommand(in);


        while (!command.equalsIgnoreCase(I18n.get(DESISTIR))) {
            String normalized = normalizeCommand(command);
            switch (normalized) {

                case "GENFLEET" -> {
                    myFleet = Fleet.createRandom();
                    game = initializeGame(myFleet, "msg.fleet.random");
                }

                case "LOADFLEET" -> {
                    myFleet = buildFleet(in);
                    game = initializeGame(myFleet, "msg.fleet.custom");
                }

                case "STATUS" -> printStatus(myFleet);

                case "MAP" -> printMap(game);

                case "VOLLEY" -> {
                    if (existsGame(game)) {
                        if (captureAndProcessFire(in, game, myFleet)) {
                            return;
                        }
                    }
                }

                case "SIMULATE" -> simulateGame(game, myFleet);

                case "SHOTS" -> printShots(game);

                case "HELP" -> menuHelp();

                case "UNKNOWN" ->
                        System.out.println(I18n.get("msg.error.unknown_cmd"));
            }

            command = readCommand(in);
        }

        System.out.println(I18n.get(GOODBYE_MESSAGE));
    }

    private static void printNeedFleetError() {
        System.out.println(I18n.get("msg.error.need_fleet"));
    }

    private static String normalizeCommand(String command) {
        if (command.equalsIgnoreCase(I18n.get(GERAFROTA))) return "GENFLEET";
        if (command.equalsIgnoreCase(I18n.get(LEFROTA))) return "LOADFLEET";
        if (command.equalsIgnoreCase(I18n.get(STATUS))) return "STATUS";
        if (command.equalsIgnoreCase(I18n.get(MAPA))) return "MAP";
        if (command.equalsIgnoreCase(I18n.get(RAJADA))) return "VOLLEY";
        if (command.equalsIgnoreCase(I18n.get(SIMULA))) return "SIMULATE";
        if (command.equalsIgnoreCase(I18n.get(TIROS))) return "SHOTS";
        if (command.equalsIgnoreCase(I18n.get(AJUDA))) return "HELP";
        return "UNKNOWN";
    }

    private static void printStatus(IFleet myFleet) {
        if (myFleet != null)
            myFleet.printStatus();
        else
            printNeedFleetError();
    }

    private static void printShots(IGame game) {
        if (existsGame(game))
            game.printMyBoard(true, true);
    }

    private static void simulateGame(IGame game, IFleet myFleet) {
        if (existsGame(game)) {

            while (game.getRemainingShips() > 0) {

                game.randomEnemyFire();
                myFleet.printStatus();
                game.printMyBoard(true, false);

                BoardGUI.refresh();

                try {
                    //noinspection BusyWait
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            game.over();

        } else {
            printNeedFleetError();
        }
    }

    private static boolean existsGame(IGame game) {
        if (game == null) {
            printNeedFleetError();
            return false;
        }
        return true;
    }

    private static void printMap(IGame game) {
        if (existsGame(game))
            game.printMyBoard(false, true);
    }

    private static String readCommand(Scanner in) {
        String command;
        System.out.print("> ");
        command = in.next();
        return command;
    }

    private static @NotNull IGame initializeGame( IFleet myFleet, String key) {
        Game game = new Game(myFleet);

        game.printMyBoard(false, true);
        System.out.println(I18n.get(key));

        IGame finalGame = game;
        javax.swing.SwingUtilities.invokeLater(() -> BoardGUI.showBoard(finalGame));
        return game;
    }

    private static boolean captureAndProcessFire(Scanner in, IGame game, IFleet myFleet) {
        System.out.println(I18n.get("msg.prompt.volley"));

        StopWatch relogio = new StopWatch();
        relogio.start();

        in.nextLine(); // limpar buffer
        String coords = in.nextLine();

        relogio.stop();

        long tempo = relogio.getTime() / 1000;
        printTimeSpent(tempo);

        processFire(game, myFleet, coords);

        return gameOver(game);
    }

    private static void printTimeSpent(long tempo) {
        System.out.println(I18n.get("msg.time_spent", tempo));
    }

    private static boolean gameOver(IGame game) {
        if (game.getRemainingShips() == 0) {
            game.over();
            return true;
        }
        return false;
    }

    private static void processFire(IGame game, IFleet myFleet, String coords) {
        Scanner coordsScanner = new Scanner(coords);
        game.readEnemyFire(coordsScanner);

        myFleet.printStatus();
        game.printMyBoard(true, false);

        BoardGUI.refresh();
    }

    public static void menuHelp() {
        System.out.println("=== " + I18n.get("desc.help").toUpperCase() + " ===");
        System.out.println(I18n.get("desc.instruction"));
        System.out.println("- " + I18n.get(GERAFROTA) + ": " + I18n.get("desc.genfleet.cmd"));
        System.out.println("- " + I18n.get(LEFROTA) + ": " + I18n.get("desc.loadfleet.cmd"));
        System.out.println("- " + I18n.get(STATUS) + ": " + I18n.get("desc.status.cmd"));
        System.out.println("- " + I18n.get(MAPA) + ": " + I18n.get("desc.map.cmd"));
        System.out.println("- " + I18n.get(RAJADA) + ": " + I18n.get("desc.volley.cmd"));
        System.out.println("- " + I18n.get(SIMULA) + ": " + I18n.get("desc.simulate.cmd"));
        System.out.println("- " + I18n.get(TIROS) + ": " + I18n.get("desc.shots.cmd"));
        System.out.println("- " + I18n.get(DESISTIR) + ": " + I18n.get("desc.surrender.cmd"));
        System.out.println("===============================================================");
    }

    public static Fleet buildFleet(Scanner in) {

        Fleet fleet = new Fleet();
        int i = 0;

        while (i < Fleet.FLEET_SIZE) {

            IShip s = readShip(in);

            if (s != null) {
                boolean success = fleet.addShip(s);

                if (success)
                    i++;
                else
                    LOGGER.info("Falha na criacao de {} {} {}", s.getCategory(), s.getBearing(), s.getPosition());

            } else {
                LOGGER.info("Navio desconhecido!");
            }
        }

        LOGGER.info("{} navios adicionados com sucesso!", i);
        return fleet;
    }

    public static Ship readShip(Scanner in) {

        String shipKind = in.next();
        Position pos = readPosition(in);
        char c = in.next().charAt(0);

        Compass bearing = Compass.charToCompass(c);

        return Ship.buildShip(shipKind, bearing, pos);
    }

    public static Position readPosition(Scanner in) {

        int row = in.nextInt();
        int column = in.nextInt();

        return new Position(row, column);
    }

    public static IPosition readClassicPosition(@NotNull Scanner in) {

        if (!in.hasNext()) {
            throw new IllegalArgumentException("Nenhuma posição válida encontrada!");
        }

        String part1 = in.next();
        String part2 = null;

        if (in.hasNextInt()) {
            part2 = in.next();
        }

        String input = (part2 != null) ? part1 + part2 : part1;

        input = input.toUpperCase();

        return parsePositionString(input, part2, part1);

    }

    private static @NotNull Position parsePositionString(String input, String part2, String part1) {
        if (input.matches("[A-Z]\\d+")) {

            char column = input.charAt(0);
            int row = Integer.parseInt(input.substring(1));

            return new Position(column, row);

        } else if (part2 != null && part1.matches("[A-Z]") && part2.matches("\\d+")) {

            char column = part1.charAt(0);
            int row = Integer.parseInt(part2);

            return new Position(column, row);

        } else {
            throw new IllegalArgumentException("Formato inválido. Use 'A3' ou 'A 3'.");
        }
    }

}