package battleship;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang3.time.StopWatch;
import util.I18n;

/**
 * The type Tasks.
 */
public class Tasks {
    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * The constant GOODBYE_MESSAGE.
     */
    //Anterior -> private static final String GOODBYE_MESSAGE = "Bons ventos!";
    private static final String GOODBYE_MESSAGE = "msg.goodbye";

    /**
     * Strings to be used by the user
     */
    private static final String AJUDA = "cmd.help";
    private static final String GERAFROTA = "cmd.genfleet";
    private static final String LEFROTA = "cmd.loadfleet";
    private static final String DESISTIR = "cmd.surrender";
    private static final String RAJADA = "cmd.volley";
    private static final String TIROS = "cmd.shots";
    private static final String MAPA = "cmd.map";
    private static final String STATUS = "cmd.status";
    private static final String SIMULA = "cmd.simulate";

    /**
     * This task also tests the fighting element of a round of three shots
     */
    public static void menu() {

        IFleet myFleet = null;
        IGame game = null;
        menuHelp();

        System.out.print("> ");
        Scanner in = new Scanner(System.in);
        String command = in.next();
        while (!command.equals(I18n.get(DESISTIR))) {

            //Tive de trocar de switch-case para if-else devido às traduções.
            if (command.equals(I18n.get(GERAFROTA))) {
                myFleet = Fleet.createRandom();
                game = new Game(myFleet);
                game.printMyBoard(false, true);
            } else if (command.equals(I18n.get(LEFROTA))) {
                myFleet = buildFleet(in);
                game = new Game(myFleet);
                game.printMyBoard(false, true);
            } else if (command.equals(I18n.get(STATUS))) {
                if (myFleet != null) myFleet.printStatus();
            } else if (command.equals(I18n.get(MAPA))) {
                if (myFleet != null) game.printMyBoard(false, true);
            } else if (command.equals(I18n.get(RAJADA))) {
                if (game != null) {
                    // 1. OBRIGA O JOGO A PEDIR OS TIROS NUMA NOVA LINHA
                    System.out.println(I18n.get("msg.prompt.volley"));

                    // 2. INICIA O CRONÓMETRO AQUI!
                    StopWatch relogio = new StopWatch();
                    relogio.start();

                    // Limpa qualquer lixo que tenha ficado no buffer antes de pedir nova linha
                    in.nextLine();

                    // 3. O JOGO FICA AQUI PARADO À TUA ESPERA (O TEMPO ESTÁ A CONTAR)
                    String coords = in.nextLine();

                    // 4. PARA O CRONÓMETRO ASSIM QUE DÁS ENTER
                    relogio.stop();

                    long tempoEmSegundos = relogio.getTime() / 1000;
                    System.out.println(I18n.get("msg.time_spent", tempoEmSegundos));

                    // 5. AGORA SIM, MANDA AS COORDENADAS PARA O JOGO PROCESSAR

                    Scanner coordsScanner = new Scanner(coords);
                    game.readEnemyFire(coordsScanner);

                    myFleet.printStatus();
                    game.printMyBoard(true, false);

                    if (game.getRemainingShips() == 0) {
                        game.over();
                        System.exit(0);
                    }
                } else {
                    System.out.println(I18n.get("msg.error.need_fleet"));
                }
            } else if (command.equals(I18n.get(SIMULA))) {
                if (game != null) {
                    while (game.getRemainingShips() > 0) {
                        game.randomEnemyFire();
                        myFleet.printStatus();
                        game.printMyBoard(true, false);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt(); // Best practice: restore interrupt status
                        }
                    }

                    if (game.getRemainingShips() == 0) {
                        game.over();
                        System.exit(0);
                    }
                }
            } else if (command.equals(I18n.get(TIROS))) {
                if (game != null)
                    game.printMyBoard(true, true);
            } else if (command.equals(I18n.get(AJUDA))) {
                menuHelp();
            } else {
                System.out.println(I18n.get("msg.error.unknown_cmd"));
            }
            System.out.print("> ");
            command = in.next();
        }
        System.out.println(I18n.get(GOODBYE_MESSAGE));
    }

    /**
     * This function provides help information about the menu commands.
     */
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

    /**
     * This operation allows the build up of a fleet, given user data
     *
     * @param in The scanner to read from
     * @return The fleet that has been built
     */
    public static Fleet buildFleet(Scanner in) {

        assert in != null;

        Fleet fleet = new Fleet();
        int i = 0; // i represents the total of successfully created ships
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

    /**
     * This operation reads data about a ship, build it and returns it
     *
     * @param in The scanner to read from
     * @return The created ship based on the data that has been read
     */
    public static Ship readShip(Scanner in) {

        assert in != null;

        String shipKind = in.next();
        Position pos = readPosition(in);
        char c = in.next().charAt(0);
        Compass bearing = Compass.charToCompass(c);
        return Ship.buildShip(shipKind, bearing, pos);
    }

    /**
     * This operation allows reading a position in the map
     *
     * @param in The scanner to read from
     * @return The position that has been read
     */
    public static Position readPosition(Scanner in) {

        assert in != null;

        int row = in.nextInt();
        int column = in.nextInt();
        return new Position(row, column);
    }

    /**
     * This operation allows reading a position in the map
     *
     * @param in The scanner to read from
     * @return The classic position that has been read
     */
    public static IPosition readClassicPosition(@NotNull Scanner in) {
        // Verifica se ainda há tokens disponíveis
        if (!in.hasNext()) {
            throw new IllegalArgumentException("Nenhuma posição válida encontrada!");
        }

        String part1 = in.next(); // Primeiro token
        String part2 = null;

        if (in.hasNextInt()) {
            part2 = in.next(); // Segundo token, se disponível
        }

        String input = (part2 != null) ? part1 + part2 : part1;

        // Normalizar o input para tratar letras maiúsculas e minúsculas
        input = input.toUpperCase();

        // Verificar os dois formatos possíveis: compactos e com espaço
        if (input.matches("[A-Z]\\d+")) {
            char column = input.charAt(0); // Extrair a coluna
            int row = Integer.parseInt(input.substring(1)); // Extrair a linha
            return new Position(column, row);
        } else if (part2 != null && part1.matches("[A-Z]") && part2.matches("\\d+")) {
            char column = part1.charAt(0); // Extrair a coluna
            int row = Integer.parseInt(part2); // Extrair a linha
            return new Position(column, row);
        } else {
            throw new IllegalArgumentException("Formato inválido. Use 'A3', 'A 3' ou similar.");
        }
    }

}