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

        IFleet myFleet = null;
        IGame game = null;

        Scanner in = new Scanner(System.in);

        menuHelp();

        System.out.print("> ");
        String command = in.next();

        while (!command.equalsIgnoreCase(I18n.get(DESISTIR))) {

            if (command.equalsIgnoreCase(I18n.get(GERAFROTA))) {

                myFleet = Fleet.createRandom();
                game = new Game(myFleet);

                game.printMyBoard(false, true);
                System.out.println(I18n.get("msg.fleet.random"));

                IGame finalGame = game;
                javax.swing.SwingUtilities.invokeLater(() -> BoardGUI.showBoard(finalGame));

            } else if (command.equalsIgnoreCase(I18n.get(LEFROTA))) {

                myFleet = buildFleet(in);
                game = new Game(myFleet);

                game.printMyBoard(false, true);
                System.out.println(I18n.get("msg.fleet.custom"));

                IGame finalGame1 = game;
                javax.swing.SwingUtilities.invokeLater(() -> BoardGUI.showBoard(finalGame1));

            } else if (command.equalsIgnoreCase(I18n.get(STATUS))) {

                if (myFleet != null)
                    myFleet.printStatus();
                else
                    System.out.println(I18n.get("msg.error.need_fleet"));

            } else if (command.equalsIgnoreCase(I18n.get(MAPA))) {

                if (game != null)
                    game.printMyBoard(false, true);
                else
                    System.out.println(I18n.get("msg.error.need_fleet"));

            } else if (command.equalsIgnoreCase(I18n.get(RAJADA))) {

                if (game != null) {

                    System.out.println(I18n.get("msg.prompt.volley"));

                    StopWatch relogio = new StopWatch();
                    relogio.start();

                    in.nextLine(); // limpar buffer
                    String coords = in.nextLine();

                    relogio.stop();

                    long tempo = relogio.getTime() / 1000;
                    System.out.println(I18n.get("msg.time_spent", tempo));

                    Scanner coordsScanner = new Scanner(coords);
                    game.readEnemyFire(coordsScanner);

                    myFleet.printStatus();
                    game.printMyBoard(true, false);

                    BoardGUI.refresh();

                    if (game.getRemainingShips() == 0) {
                        game.over();
                        break;
                    }

                } else {
                    System.out.println(I18n.get("msg.error.need_fleet"));
                }

            } else if (command.equalsIgnoreCase(I18n.get(SIMULA))) {

                if (game != null) {

                    while (game.getRemainingShips() > 0) {

                        game.randomEnemyFire();
                        myFleet.printStatus();
                        game.printMyBoard(true, false);

                        BoardGUI.refresh();

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    game.over();

                } else {
                    System.out.println(I18n.get("msg.error.need_fleet"));
                }

            } else if (command.equalsIgnoreCase(I18n.get(TIROS))) {

                if (game != null)
                    game.printMyBoard(true, true);

            } else if (command.equalsIgnoreCase(I18n.get(AJUDA))) {

                menuHelp();

            } else {

                System.out.println(I18n.get("msg.error.unknown_cmd"));
            }

            System.out.print("> ");
            command = in.next();
        }

        System.out.println(I18n.get(GOODBYE_MESSAGE));
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