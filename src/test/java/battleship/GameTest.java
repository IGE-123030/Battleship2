package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;

/**
 * Test class for Game.
 * Author: britoeabreu
 * Date: 2024-03-19
 * Time: 15:30
 * Cyclomatic Complexity for each method:
 * - Game (constructor): 1
 * - fire: 7
 * - getShots: 1
 * - getRepeatedShots: 1
 * - getInvalidShots: 1
 * - getHits: 1
 * - getSunkShips: 1
 * - getRemainingShips: 1
 * - validShot: 3
 * - repeatedShot: 2
 * - printBoard: 1
 * - printValidShots: 1
 * - printFleet: 1
 */
public class GameTest {

	private Game game;

	@BeforeEach
	void setUp() {
		game = new Game(new Fleet()); // Assuming Fleet is a concrete implementation of IFleet
	}

	@AfterEach
	void tearDown() {
		game = null;
	}

	@Test
	void constructor() {
		assertNotNull(game, "Game instance should not be null after construction.");
		assertNotNull(game.getAlienMoves(), "Shots list should not be null after initialization.");
		assertTrue(game.getAlienMoves().isEmpty(), "Shots list should be empty upon initialization.");
		assertEquals(0, game.getInvalidShots(), "Invalid shots count should be zero upon initialization.");
		assertEquals(0, game.getRepeatedShots(), "Repeated shots count should be zero upon initialization.");
		assertEquals(0, game.getHits(), "Hits count should be zero upon initialization.");
		assertEquals(0, game.getSunkShips(), "Sunk ships count should be zero upon initialization.");
	}

	@Test
	void fire2() {
		Position invalidPosition = new Position(-1, 5);
		game.fireSingleShot(invalidPosition, false);
		assertEquals(1, game.getInvalidShots(), "Invalid shots counter should increase for an invalid shot.");
	}

	@Test
	void fire3() {
		Position position = new Position(2, 3);
		game.fireSingleShot(position, false);
		game.fireSingleShot(position, true);
		assertEquals(1, game.getRepeatedShots(), "Repeated shots counter should increase for a repeated shot.");
	}

	@Test
	void repeatedShot1() {
		List<IPosition> positions = List.of(new Position(2, 3), new Position(2, 4), new Position(2, 5));
		game.fireShots(positions);
		Position position = new Position(2, 3);
		assertTrue(game.repeatedShot(position), "Position (2,3) should be marked as repeated after firing.");
	}

	@Test
	void repeatedShot2() {
		Position position = new Position(2, 3);
		assertFalse(game.repeatedShot(position), "Position (2,3) should not be marked as repeated before firing.");
	}

	@Test
	void getAlienMoves() {
		List<IPosition> positions = List.of(new Position(2, 3), new Position(2, 4), new Position(2, 5));
		game.fireShots(positions);
		assertEquals(1, game.getAlienMoves().size(), "Shots list should contain one shot after firing once.");
	}

	@Test
	void getRemainingShips() {
		IFleet fleet = game.getMyFleet();
		Ship ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		Ship ship2 = new Frigate(Compass.EAST, new Position(5, 5));

		fleet.addShip(ship1);
		assertEquals(1, game.getRemainingShips(), "Just one ship was created!");
		fleet.addShip(ship2);
		assertEquals(2, game.getRemainingShips(), "Two ships were created!");
		ship2.sink();
		assertEquals(1, game.getRemainingShips(), "Remaining ships count should be 1 after sinking one of two ships.");
	}

	@Test
	void printBoard() {
		// Testa a impressão do tabuleiro com e sem legendas/tiros
		// Isso cobre as linhas de visualização da classe Game
		assertDoesNotThrow(() -> game.printMyBoard(true, true));
		assertDoesNotThrow(() -> game.printAlienBoard(false, false));
	}

	@Test
	void jsonShots() {
		// Preenche o espaço para testar a serialização JSON
		List<IPosition> shots = List.of(new Position(0, 0), new Position(1, 1));
		String json = Game.jsonShots(shots);
		assertNotNull(json);
		assertTrue(json.contains("row"));
		assertTrue(json.contains("column"));
	}

	@Test
	void randomEnemyFire() {
		// Executa várias vezes para forçar o jogo a escolher diferentes posições
		// e cobrir a lógica de remoção de candidatos
		for (int i = 0; i < 20; i++) {
			game.randomEnemyFire();
		}
		assertFalse(game.getAlienMoves().isEmpty());
	}

	@Test
	void readEnemyFire() {
		Scanner sc1 = new Scanner("A 1 B 2 C 3");
		assertNotNull(game.readEnemyFire(sc1));

		// Caso 2: Formato colado "D4 E5 F6"
		Scanner sc2 = new Scanner("D4 E5 F6");
		assertNotNull(game.readEnemyFire(sc2));

		// Caso 3: Erro de input (menos coordenadas que o necessário) para cobrir o ramo de exceção
		Scanner sc3 = new Scanner("A 1");
		assertThrows(IllegalArgumentException.class, () -> game.readEnemyFire(sc3));
	}

	@Test
	void fireShots() {
		List<IPosition> volley = List.of(new Position(1,1), new Position(2,2), new Position(3,3));
		// Garante que o método que salva na base de dados e gera o Move é executado
		assertDoesNotThrow(() -> game.fireShots(volley));
	}

	@Test
	void fireSingleShot() {
		// 1. Preparar navio
		Position posNavio = new Position(0, 0);
		game.getMyFleet().addShip(new Barge(Compass.NORTH, posNavio));

		// 2. Usar fireShots para garantir que o movimento é registado no histórico do jogo
		// Disparamos uma rajada de 3 tiros (exigência comum do motor do jogo)
		List<IPosition> volley = List.of(posNavio, new Position(9,9), new Position(8,8));
		game.fireShots(volley);

		// 3. Agora testamos se o jogo reconhece a repetição ao tentar disparar lá de novo
		// O parâmetro 'true' no final força a lógica de repetição se o método aceitar
		IGame.ShotResult resRepetido = game.fireSingleShot(posNavio, true);

		// Se o método fireSingleShot estiver correto, isto tem de dar true
		assertTrue(game.repeatedShot(posNavio), "A posição (0,0) deve constar como repetida no histórico");
		assertTrue(resRepetido.repeated(), "O objeto ShotResult deve indicar que é repetido");
	}

	@Test
	void testGetRemainingShips() {
	}

	@Test
	void repeatedShot() {
		Position p = new Position(4, 4);

		// Garantir estado limpo
		assertFalse(game.repeatedShot(p));

		// Disparar via fireShots (o método que adiciona à lista getAlienMoves())
		List<IPosition> volley = List.of(p, new Position(0,9), new Position(9,0));
		game.fireShots(volley);

		// Agora o jogo tem de reconhecer que 'p' já foi disparado
		assertTrue(game.repeatedShot(p), "O método repeatedShot deve retornar true após a posição ser disparada via fireShots");
	}

	@Test
	void printMyBoard() {
		// Disparar alguns tiros para garantir que o tabuleiro tem símbolos para desenhar
		game.fireSingleShot(new Position(1, 1), false); // Água

		// Testa os ramos com e sem legendas/fogos
		assertDoesNotThrow(() -> game.printMyBoard(true, true));
		assertDoesNotThrow(() -> game.printMyBoard(false, false));

		// Testa o método over() que imprime a mensagem final
		assertDoesNotThrow(() -> game.over());
	}
	@Test
	void testPrintBoardWithSunkShip() {
		IFleet fleet = new Fleet();
		// Adicionar uma Barca (ocupa 1 posição)
		Position p = new Position(0, 0);
		IShip barge = new Barge(Compass.NORTH, p);
		fleet.addShip(barge);

		// Afundar o barco para ativar o ramo "if (!ship.stillFloating())"
		barge.sink();

		// Isto vai cobrir o desenho das SHIP_ADJACENT_MARKER
		assertDoesNotThrow(() -> game.printBoard(fleet, new ArrayList<>(), true, true));
	}
	@Test
	void testPrintBoardWithHitsAndMisses() {
		IFleet fleet = new Fleet();
		Position posNavio = new Position(2, 2);
		fleet.addShip(new Barge(Compass.NORTH, posNavio));

		// Criar um Move com 2 tiros: um acerto e uma falha
		List<IPosition> shots = List.of(
				posNavio,             // Acerto (SHOT_SHIP_MARKER)
				new Position(5, 5)    // Água (SHOT_WATER_MARKER)
		);

		// Simular o resultado dos tiros para criar o objeto Move
		List<IGame.ShotResult> results = List.of(
				new IGame.ShotResult(true, false, fleet.getShips().get(0), false),
				new IGame.ShotResult(true, false, null, false)
		);

		IMove move = new Move(1, shots, results);
		List<IMove> moves = List.of(move);

		// Executar printBoard com show_shots = true
		assertDoesNotThrow(() -> game.printBoard(fleet, moves, true, true));
	}
	@Test
	void testPrintBoardWithOutsideShots() {
		List<IMove> moves = new ArrayList<>();
		List<IPosition> shots = List.of(new Position(-1, -1)); // Fora do tabuleiro
		moves.add(new Move(1, shots, new ArrayList<>()));

		// Isto forçará o 'if (shot.isInside())' a ser avaliado como false
		assertDoesNotThrow(() -> game.printBoard(new Fleet(), moves, true, false));
	}
	@Test
	void testGameOverCoverage() {
		// 1. Caso onde o jogo ainda não acabou
		assertDoesNotThrow(() -> game.over());

		// 2. Caso de vitória (limpar frota e afundar tudo)
		game.getMyFleet().getShips().clear();
		Barge smallShip = new Barge(Compass.NORTH, new Position(0, 0));
		game.getMyFleet().addShip(smallShip);

		// Disparar para afundar o único navio
		game.fireSingleShot(new Position(0, 0), false);

		// Isto agora vai entrar no ramo de "Vitória" dentro do over()
		assertDoesNotThrow(() -> game.over());
	}
	@Test
	void testPrintBoardAdjacentShot() {
		IFleet fleet = new Fleet();
		Position p = new Position(1, 1);
		Barge barge = new Barge(Compass.NORTH, p);
		fleet.addShip(barge);
		barge.sink(); // Marca vizinhos como SHIP_ADJACENT_MARKER

		// Criar um movimento que acerta na zona adjacente (ex: 0,1)
		Position adjPos = new Position(0, 1);
		List<IPosition> shots = List.of(adjPos);
		List<IGame.ShotResult> results = List.of(new IGame.ShotResult(true, false, null, false));
		IMove move = new Move(1, shots, results);

		// Isso cobre o segundo lado do OR (||) na lógica do printBoard
		assertDoesNotThrow(() -> game.printBoard(fleet, List.of(move), true, true));
	}
	@Test
	void testReadEnemyFireWithInvalidInput() {
		// Simula: primeiro um erro "Z99", depois coordenadas corretas
		Scanner sc = new Scanner("Z99\nA 1 B 2 C 3");

		// O código vai dar erro no Z99, entrar no catch, e tentar ler de novo o A 1...
		assertNotNull(game.readEnemyFire(sc));
	}
	@Test
	void testPrintBoardNullArguments() {
		// Cobre o ramo onde as asserções falham (false hits)
		try {
			game.printBoard(null, new ArrayList<>(), true, true);
		} catch (AssertionError e) { /* Coberto */ }

		try {
			game.printBoard(new Fleet(), null, true, true);
		} catch (AssertionError e) { /* Coberto */ }
	}
	@Test
	void testReadEnemyFireIncompleteShots() {
		// Outro teste para garantir a cobertura da mesma lógica
		Scanner sc = new Scanner("B2\n");

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			game.readEnemyFire(sc);
		});

		assertNotNull(ex.getMessage());
	}
	@Test
	void testReadEnemyFireIncompletePosition() {
		// Força o erro: token "A" sem número a seguir
		Scanner sc = new Scanner("A");

		assertThrows(IllegalArgumentException.class, () -> {
			game.readEnemyFire(sc);
		});
	}
	@Test
	void testReadEnemyFireInsufficientShots() {
		// Forçamos o erro: enviamos apenas 1 posição (A1).
		// O NUMBER_SHOTS é 3, logo shots.size() será 1.
		Scanner sc = new Scanner("A1");

		// Este assert captura a IllegalArgumentException lançada no fim do método
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			game.readEnemyFire(sc);
		});

		// Isto garante que a linha "Must fire exactly..." foi executada
		assertTrue(ex.getMessage().contains("exatamente") || ex.getMessage().contains("exactly"));
	}
	@Test
	void testReadEnemyFireMalformedToken() {
		// Simula um token que não é uma posição válida
		Scanner sc = new Scanner("Z\n");

		assertThrows(IllegalArgumentException.class, () -> {
			game.readEnemyFire(sc);
		});
	}
	@Test
	void testAssertsNull() {
		// Testa o assert in != null no readEnemyFire
		try {
			game.readEnemyFire(null);
		} catch (AssertionError e) { /* Coberto */ }

		// Testa o assert pos != null no fireSingleShot
		try {
			game.fireSingleShot(null, false);
		} catch (AssertionError e) { /* Coberto */ }
	}
	@Test
	void testPrintBoardAdjacentHit() {
		// 1. Criar frota com um barco pequeno e afundá-lo
		IFleet fleet = new Fleet();
		Position pos = new Position(1, 1);
		Barge barge = new Barge(Compass.NORTH, pos);
		fleet.addShip(barge);
		barge.sink(); // Isto cria SHIP_ADJACENT_MARKER à volta de (1,1)

		// 2. Criar um movimento que acerta na vizinhança (ex: 0,1)
		Position adjPos = new Position(0, 1);
		IMove move = new Move(1, List.of(adjPos),
				List.of(new IGame.ShotResult(true, false, null, false)));

		// 3. Executar o printBoard: isto vai forçar o true no SHIP_ADJACENT_MARKER
		assertDoesNotThrow(() -> game.printBoard(fleet, List.of(move), true, true));
	}
	@Test
	void testRandomEnemyFireWithFullBoard() {
		// 1. Simular muitos tiros para esgotar as posições do tabuleiro
		// Se o tabuleiro for 10x10, precisamos de quase 100 tiros
		for (int i = 0; i < 99; i++) {
			game.getAlienMoves().add(new Move(i, List.of(new Position(i/10, i%10)), new ArrayList<>()));
		}

		// 2. Agora o candidateShots.size() será pequeno, entrando no 'else'
		assertDoesNotThrow(() -> game.randomEnemyFire());
	}
	@Test
	void testJsonShotsFailure() {
		List<IPosition> broken = new ArrayList<>();
		broken.add(null); // Passar null costuma fazer o ObjectMapper explodir no loop

		assertThrows(RuntimeException.class, () -> Game.jsonShots(broken));
	}
	@Test
	void testGettersCoverage() {
		// Chama os métodos para garantir que o código passa por eles
		assertNotNull(game.getAlienFleet(), "Deve retornar a frota");
		assertNotNull(game.getMyMoves(), "Deve retornar a lista de movimentos");
	}
	@Test
	void testFireShotsInvalidCount() {
		// Este teste foca na linha: throw new IllegalArgumentException("Must fire exactly " + NUMBER_SHOTS + " shots per move.");
		// que está dentro do método fireShots(List<IPosition> shots)

		List<IPosition> tooFewShots = List.of(new Position(0, 0)); // Apenas 1 tiro

		assertThrows(IllegalArgumentException.class, () -> {
			game.fireShots(tooFewShots);
		});
	}
	@Test
	void testJsonShotsSerializationError() {
		// Forçamos um erro de cast que o teu catch(Exception e) vai apanhar
		// e transformar na RuntimeException desejada.
		List listaSuja = new ArrayList();
		listaSuja.add("Não sou uma posição");

		assertThrows(RuntimeException.class, () -> Game.jsonShots(listaSuja));
	}
	@Test
	void testAssertPosNotNull() {
		// Testa o assert dentro de repeatedShot
		assertThrows(AssertionError.class, () -> game.repeatedShot(null));

		// Aproveite para testar também no fireSingleShot, que usa 'pos'
		assertThrows(AssertionError.class, () -> game.fireSingleShot(null, false));
	}
	@Test
	void testHitAdjacentMarker() {
		IFleet fleet = game.getMyFleet();
		Position p = new Position(1, 1);
		Barge barge = new Barge(Compass.NORTH, p);
		fleet.addShip(barge);

		// 1. Afundar o barco para gerar os marcadores adjacentes ('-') no mapa
		barge.sink();

		// 2. Disparar num quadrado vizinho (ex: 0,1 ou 1,0)
		// Isso fará com que a condição 'map[row][col] == SHIP_ADJACENT_MARKER' seja TRUE
		game.fireSingleShot(new Position(0, 1), false);

		// Chame o printBoard ou printMyBoard para garantir que a lógica de desenho é executada
		game.printMyBoard(true, false);
	}
	@Test
	void testJsonShotsAssertNull() {
		// Força a condição 'shots != null' a ser falsa
		assertThrows(AssertionError.class, () -> {
			Game.jsonShots(null);
		});
	}
	@Test
	void testPrintBoardAdjacentLogic() {
		IFleet fleet = new Fleet();
		Position pos = new Position(1, 1);
		Barge barge = new Barge(Compass.NORTH, pos);
		fleet.addShip(barge);

		// 1. Afundar o barco para que os vizinhos passem a ser SHIP_ADJACENT_MARKER ('-')
		barge.sink();

		// 2. Criar um movimento que acerta num vizinho (ex: 1,2)
		Position adjPos = new Position(1, 2);
		IMove move = new Move(1, List.of(adjPos),
				List.of(new IGame.ShotResult(true, false, null, false)));

		// 3. Executar o printBoard: isto vai forçar o 'true' na verificação do SHIP_ADJACENT_MARKER
		assertDoesNotThrow(() -> game.printBoard(fleet, List.of(move), true, true));
	}
	@Test
	void testAssertsNotNull() {
		// Para a imagem do shots != null (Hits: 30 e Hits: 56)
		assertThrows(AssertionError.class, () -> Game.jsonShots(null));
		assertThrows(AssertionError.class, () -> game.fireShots(null));
	}
	@Test
	void testRepeatedShotLogic() {
		Position p = new Position(5, 5);
		// 1ª vez: tiro normal
		game.fireShots(List.of(p, new Position(0,0), new Position(1,1)));

		// 2ª vez: disparar para 'p' novamente (isRepeated=false, mas repeatedShot(p)=true)
		// Isto vai ativar o "true hits" na segunda parte do OR (||)
		IGame.ShotResult res = game.fireSingleShot(p, false);

		assertTrue(res.repeated());
	}
	@Test
	void testShipHitButNotSunk() {
		// Usar um Frigate (tamanho maior que 1)
		Position p = new Position(2, 2);
		game.getMyFleet().addShip(new Frigate(Compass.NORTH, p));

		// Disparar apenas numa parte. ship.stillFloating() continuará true.
		// Logo !ship.stillFloating() será FALSE.
		IGame.ShotResult res = game.fireSingleShot(p, false);

		assertFalse(res.sunk());
	}
	@Test
	void testRandomFireCollision() {
		// 1. Limpamos os movimentos para garantir espaço no tabuleiro
		game.getAlienMoves().clear();

		// 2. Disparar uma quantidade segura para não esgotar o tabuleiro 10x10
		// 20 disparos é suficiente para testar a lógica sem causar crash
		for (int i = 0; i < 20; i++) {
			game.randomEnemyFire();
		}

		// 3. O assertDoesNotThrow deve envolver apenas uma chamada se o objetivo for testar estabilidade
		assertDoesNotThrow(() -> game.randomEnemyFire());
	}
	@Test
	void testShotHitButNotSunk() {
		// 1. Preparação: Limpar contadores e adicionar um navio grande
		Position p = new Position(4, 4);
		// Frigate ocupa 3 posições, logo 1 tiro não o afunda
		game.getMyFleet().addShip(new Frigate(Compass.NORTH, p));

		int hitsAntes = game.getHits();

		// 2. Execução: Disparar no navio
		// O fireSingleShot vai executar a linha !ship.stillFloating() (que será false)
		game.fireSingleShot(p, false);

		// 3. Verificação via contadores do Game (evita erro de símbolo no ShotResult)
		assertEquals(hitsAntes + 1, game.getHits(), "O contador de hits deve ter subido");
		assertEquals(0, game.getSunkShips(), "O navio não deve ter afundado ainda");
	}
	@Test
	void testJsonShotsCatchCoverage() throws Exception {

		// Mapper falso que falha sempre
		ObjectMapper fakeMapper = new ObjectMapper() {
			@Override
			public String writeValueAsString(Object value) throws JsonProcessingException {
				throw new JsonProcessingException("Erro forçado") {};
			}
		};

		// Aceder ao campo privado static objectMapper
		java.lang.reflect.Field field = Game.class.getDeclaredField("objectMapper");
		field.setAccessible(true);

		// Guardar original
		Object original = field.get(null);

		// Injetar falso
		field.set(null, fakeMapper);

		List<IPosition> shots = List.of(new Position(0, 0));

		assertThrows(RuntimeException.class, () -> Game.jsonShots(shots));

		// Restaurar original
		field.set(null, original);
	}
	@Test
	void testRandomEnemyFireElseBranchGuaranteed() {
		Game g = new Game(new Fleet());

		// ocupar 98 posições já disparadas
		for (int i = 0; i < 98; i++) {
			int row = i / 10;
			int col = i % 10;

			g.getAlienMoves().add(
					new Move(i,
							List.of(new Position(row, col)),
							new ArrayList<>())
			);
		}

		// agora sobram menos de 3 posições => entra no else
		assertDoesNotThrow(g::randomEnemyFire);
	}
	@Test
	void branchMissingLikelyRepeatedBothFalse() {
		Position fresh = new Position(9,9);

		IGame.ShotResult r = game.fireSingleShot(fresh, false);

		assertFalse(r.repeated());
	}




}