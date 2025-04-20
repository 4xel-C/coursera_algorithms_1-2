
# 8-Puzzle Solver

This project implements the A* algorithm to solve the 8-puzzle problem. It includes classes to represent the puzzle board and a solver that finds the shortest path to the solution using the Manhattan priority function.

## 📌 Overview

The 8-puzzle is a sliding puzzle having 8 numbered square tiles and one empty space. The objective is to place the tiles in order by making sliding moves that use the empty space.

## 📁 Structure

- `Board.java` - represents the game board, handles state, moves, and heuristics.
- `Solver.java` - uses the A* algorithm to solve the puzzle using `Board` objects.

## ▶️ How It Works

- **Initialization**: The solver takes an initial board and its "twin" (a variation with two tiles swapped).
- **A\* Algorithm**: It maintains two priority queues—one for the initial board and one for the twin.
- If the twin reaches the goal state first, the puzzle is unsolvable.
- Otherwise, it explores the minimum-priority node's neighbors until the goal is reached.

## 📐 Heuristics

- **Hamming**: Number of tiles out of place.
- **Manhattan**: Sum of the Manhattan distances (sum of vertical and horizontal distances) from the tiles to their goal positions.

## 🧠 A* Algorithm

Uses the formula:

```
priority = number of moves made + manhattan distance
```

## 📦 API Definitions

### `Board`

- `Board(int[][] tiles)`: Constructor that deep copies the board.
- `int dimension()`: Returns board dimension (n).
- `int hamming()`: Returns the Hamming distance.
- `int manhattan()`: Returns the Manhattan distance.
- `boolean isGoal()`: Checks if the board is in the goal state.
- `Iterable<Board> neighbors()`: Returns all neighboring boards.
- `Board twin()`: Returns a board by swapping any two non-zero tiles.
- `boolean equals(Object y)`: Checks if two boards are equal.
- `String toString()`: Returns the string representation of the board.

### `Solver`

- `Solver(Board initial)`: Solves the puzzle using A* algorithm.
- `boolean isSolvable()`: Returns whether the puzzle is solvable.
- `int moves()`: Returns the minimum number of moves to solve the puzzle, -1 if unsolvable.
- `Iterable<Board> solution()`: Returns the sequence of boards in the shortest solution path.

## ✅ Example Output

```
3
 3  4  2
 1  5  6
 8  7  0

Manhattan: 10
Hamming: 5
```

## 🧪 How to Test

You can test the program by creating a `main` method in `Board` or `Solver` and initializing with a test board. Then call methods like `solver.isSolvable()` and `solver.solution()`.

---
