# Oportunidades de Refabricação (Refactoring Opportunities)

| Local (classe ou classe::método) | Nome do cheiro no código | Nome da refabricação | Número d@ alun@ |
| :--- | :--- | :--- | :--- |
| `battleship.Ship` | Mutable Field Exposure (Field 'size' may be 'final') | Make Final | 123030 |
| `battleship.Ship` | Switch Statements (Verbose/Legacy switch) | Replace with Enhanced Switch | 123030 |
| `battleship.Ship` | Legacy Control Flow ('while' can be replaced with 'for') | Replace with foreach | 123030 |
| `battleship.Ship` | Verbose Type Declaration (Explicit type replaced with '<>') | Type Migration (Diamond Operator) | 123030 |
| `battleship.Ship` | Redundant Assignment (Variable is already assigned to value) | Remove Redundant Assignment | 123030 |
| `battleship.Ship` | Insecure Control Flow (Missing braces/Suspicious indentation) | Add Braces | 123030 |
| `battleship.Ship` | Legacy Collection Access (SequencedCollection method) | Use getFirst() | 123030 |
| Game::printBoard | Long Method | Extract Method | 94255 |
| Game::jsonString | Redundant assignment | Inline Variable | 94255 |
| Game::objectMapper | Mutable field exposure | Make Final | 94255 |
| Game::readEnemyFire | Long Method | Extract Method | 94255 |
| Game | Large Class | Extract Class | 94255 |
| Game | Inappropriate Intimacy | Move Method | 94255 |
| Game::fireSingleShot | Overly Complex Method | Decompose Conditional | 94255 |
| :--- | :--- | :--- | :--- |
| `battleship.Tasks::menu` | Long Method | Extract Method | 122983|
| `battleship.Tasks::menu` | Switch Statements | Replace with Enhanced Switch | 122983 |
| `battleship.Tasks::readClassicPosition` | Long Method | Extract Method | 122983 |
| `battleship.Tasks` | Duplicate Code | Extract Method | 122983 |
| `battleship.Tasks` | Speculative Generality (Código morto/comentado) | Remove Dead Code | 122983 |
| `battleship.Tasks` | Information Exposure (System.out em lógica) | Extract Method | 122983 |
| `battleship.Position::randomPosition` | Feature Envy (Dependência excessiva de `Game.BOARD_SIZE`) | Move Method / Parameterize Method | 122983 |
| `battleship.Position` | Duplicate Code (Lógica de conversão 'A' + row repetida) | Extract Method (Ex: `rowToChar`) | 122983 |
| `battleship.Position::adjacentPositions` | Long Method (Inicialização manual de matriz de direções) | Extract Method / Replace Temp with Query | 122983 |
| :--- | :--- | :--- | :--- |
| ⁠ battleship.Move::processEnemyFire ⁠ | Long Method (Método com múltiplas responsabilidades: lógica, I18n e JSON) | Extract Method (Ex: ⁠ generateJSONResponse ⁠) | 122991 |
| ⁠ battleship.Move::processEnemyFire ⁠ | Information Exposure (Uso de ⁠ System.out ⁠ misturado com lógica de serialização) | Extract Method (Separar lógica de visualização) | 122991 |
| ⁠ battleship.Move ⁠ | Information Exposure (Devolve referências diretas de Listas mutáveis) | Encapsulate Collection (Usar ⁠ unmodifiableList ⁠) | 122991 |
| ⁠ battleship.Move::processEnemyFire ⁠ | Complex Conditional (Ninhos de ⁠ if/else ⁠ para construir a String de log) | Replace Conditional with Guard Clause | 122991 |
| ⁠ battleship.Fleet::createRandom ⁠ | Magic Number (Nomes de barcos e quantidades "hardcoded") | Extract Constant / Variable | 122991 |
| ⁠ battleship.Fleet::addShip ⁠ | Complex Conditional (Múltiplas validações numa única linha) | Extract Method (Ex: ⁠ isValidAddition ⁠) | 122991 |
| ⁠ battleship.Fleet::getFloatingShips ⁠ | Duplicate Code (Lógica de filtragem muito semelhante a ⁠ getSunkShips ⁠) | Substitute Algorithm (Usar Java Streams) | 122991 |
| ⁠ battleship.Fleet ⁠ | Speculative Generality (Métodos comentados e prints não utilizados como ⁠ printAllShips ⁠) | Remove Dead Code | 122991 |
| ⁠ battleship.Fleet::colisionRisk ⁠ | Legacy Control Flow (Uso de ⁠ for ⁠ indexado para percorrer coleção) | Replace with foreach | 122991 |
| ⁠ battleship.Fleet ⁠ | Verbose Type Declaration (Ex: ⁠ new ArrayList<IShip>() ⁠) | Type Migration (Diamond Operator ⁠ <> ⁠) | 122991 |
