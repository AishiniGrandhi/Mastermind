# Mastermind
Mastermind game implemented in Java with an AI solver based on graph traversal , greedy heuristics, and extensible architecture for Dynamic Programming and Backtracking optimizations, inspired by ideas from Simon Tatham.
<img width="683" height="655" alt="image" src="https://github.com/user-attachments/assets/ef91857d-69f5-4fdb-ab6e-28974324eb68" />


## Overview
This project is an advanced Java-based implementation of the **Mastermind game**
featuring an **AI solver using Graph-based Breadth First Search (BFS) combined with
a Greedy strategy**.  
The game includes a fully interactive **Java Swing GUI** allowing both human and
AI turns.

---

## Core Concepts Used
- Graph Modeling of Game States
- Breadth First Search (BFS)
- Greedy Decision Making
- Constraint Pruning
- Object-Oriented Programming (OOP)

---

## Technologies
- Java
- Java Swing (GUI)
- BFS Graph Traversal
- Greedy Algorithms
- Event Handling

---

## Features
- Interactive drag-and-drop color selection
- BFS-based AI solver with pruning
- Greedy move selection for deterministic AI behavior
- Visual feedback with black & white pegs
- Restartable gameplay
- Human vs AI turn system

---

## Architecture
- **MastermindEngine**  
  Handles secret generation, guess evaluation, and graph vertex generation.

- **GraphBFSNavigator**  
  Implements BFS traversal, pruning of invalid states, and greedy selection.

- **MastermindBoardGUI**  
  Java Swing-based graphical interface integrating human and AI gameplay.

---

## How to Run
1. Clone the repository
```bash
git clone https://github.com/yourusername/Mastermind

2. Compile all files
```
javac *.java

3. Run the game
```
java MastermindBoardGUI

