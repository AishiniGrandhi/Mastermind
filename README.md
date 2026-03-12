<img width="683" height="655" alt="image" src="https://github.com/user-attachments/assets/ef91857d-69f5-4fdb-ab6e-28974324eb68" />
# Mastermind

Mastermind game implemented in Java with an AI solver based on **graph traversal, greedy heuristics, dynamic programming, and backtracking optimizations**, inspired by ideas from Simon Tatham.

<img width="683" height="655" alt="image" src="https://github.com/user-attachments/assets/ef91857d-69f5-4fdb-ab6e-28974324eb68" />

---

# Overview

This project is an advanced **Java-based implementation of the Mastermind game** featuring an intelligent AI solver.

The solver combines multiple algorithmic paradigms:

* **Graph-based Breadth First Search (BFS)** for state traversal
* **Greedy Minimax strategy** for selecting optimal guesses
* **Dynamic Programming (Tabulation)** for fast feedback lookup
* **Backtracking (DFS)** for constraint-based candidate generation
* **Divide & Conquer (Merge Sort)** for ordering candidate states

The game includes a fully interactive **Java Swing GUI** allowing both **human and AI turns**.

---

# Core Concepts Used

* Graph Modeling of Game States
* Breadth First Search (BFS)
* Greedy Algorithms (Minimax Strategy)
* Dynamic Programming (Tabulation)
* Backtracking with Constraint Pruning
* Divide & Conquer (Merge Sort)
* Object-Oriented Programming (OOP)

---

# Technologies

* Java
* Java Swing (GUI)
* Graph Traversal Algorithms
* Greedy Heuristics
* Dynamic Programming
* Backtracking Search
* Event Handling

---

# Features

* Interactive **drag-and-drop color selection**
* **Graph-based BFS solver** for game state management
* **Improved greedy move selection** minimizing worst-case search space
* **Dynamic Programming feedback table** for constant-time lookup
* **Backtracking candidate generation** with constraint pruning
* **Merge Sort ordering** of remaining possibilities
* Visual feedback using **black and white pegs**
* **Human vs AI cooperative gameplay**
* Restartable game session

---

# Architecture

### MastermindEngine

Handles core game logic:

* Secret code generation
* Guess evaluation (black & white peg feedback)
* Generation of all possible code vertices
* Dynamic Programming feedback table

---

### GraphBFSNavigator

Responsible for AI solving logic:

* BFS-based game state traversal
* Constraint pruning of candidate codes
* Backtracking-based candidate generation
* Greedy minimax move selection
* Merge Sort ordering of candidate states

---

### Backtracking

Implements recursive **DFS-based candidate generation**:

* Builds possible codes slot-by-slot
* Applies feedback constraints from previous guesses
* Prunes invalid branches early

---

### MastermindBoardGUI

Java Swing graphical interface:

* Drag-and-drop color input
* Displays guess history and feedback pegs
* Manages human and AI turns
* Integrates solver with the game board

---

# Algorithmic Pipeline

```
Human Guess
     ↓
Engine Feedback Evaluation
     ↓
Backtracking Candidate Generation (DFS)
     ↓
Merge Sort Ordering (Divide & Conquer)
     ↓
Greedy Minimax Move Selection
     ↓
BFS State Update
```

---

# How to Run

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/Mastermind
```

### 2. Compile the project

```bash
javac *.java
```

### 3. Run the game

```bash
java MastermindBoardGUI
```

---

If you want, I can also help you add a **very impressive “Algorithm Analysis” section** (time complexity + space complexity) to the README that would make the repository look much more professional.



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
