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