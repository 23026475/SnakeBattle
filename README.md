# 🐍 Snake Battle: Pathfinding Algorithms Showdown

A Java-based interactive Snake game that visualizes classical
pathfinding algorithms in real time --- and allows them to compete in an
AI battle arena.

This project combines: - 🎮 Classic Snake gameplay\
- 🔍 Algorithm visualization\
- ⚔️ AI vs AI competition\
- 🧠 Search algorithm comparison

Built using **Java + Swing**, with clean object-oriented architecture.

------------------------------------------------------------------------

## 🎮 Features

### 🐍 Classic Snake (Human Mode)

-   Arrow key movement
-   Real-time collision detection
-   Score tracking
-   Smooth frame updates
-   Restart and menu navigation

### 🔍 Algorithm Visualization Modes

#### 🟦 Depth-First Search (DFS)

-   Explores deeply before backtracking
-   Visualizes visited nodes
-   Demonstrates stack-based exploration

#### 🟨 Breadth-First Search (BFS)

-   Explores level-by-level
-   Guarantees shortest path in uniform grids
-   Clear exploration visualization

#### 🟪 A\* Search (A-Star)

-   Uses heuristic + path cost
-   Efficient optimal pathfinding
-   Open and closed set visualization

------------------------------------------------------------------------

## ⚔️ Snake Battle Arena

Three AI snakes compete simultaneously:

  Snake       Algorithm
  ----------- ----------------------
  DFS Snake   Depth-First Search
  BFS Snake   Breadth-First Search
  A\* Snake   A-Star

### Battle Features

-   Multi-snake real-time competition
-   Collision detection (wall, body, head-on)
-   Score tracking
-   Winner determination screen

------------------------------------------------------------------------

## 🚀 Getting Started

### Prerequisites

-   Java JDK 8 or higher

### Compile

``` bash
javac -d out $(find src -name "*.java")
```

### Run

``` bash
java -cp out com.ndivhuwo.snakebattle.SnakeGameLauncher
```

------------------------------------------------------------------------

## 🎮 Controls

### Common Controls

-   SPACE → Restart
-   ESC → Return to menu
-   Close window → Exit

### Human Mode Controls

-   ↑ Move Up
-   ↓ Move Down
-   ← Move Left
-   → Move Right

------------------------------------------------------------------------

## 📊 Algorithm Comparison

  Algorithm   Optimal Path   Strategy
  ----------- -------------- ------------------
  DFS         No             Deep exploration
  BFS         Yes            Level-by-level
  A\*         Yes            Cost + heuristic

------------------------------------------------------------------------

## 🏗️ Project Structure

src/main/java/com/ndivhuwo/snakebattle/

-   SnakeGame.java
-   GamePanel.java
-   GameMenu.java
-   DFSVisualizer.java
-   BFSVisualizer.java
-   AStarVisualizer.java
-   BattleManager.java
-   BattleSnake.java
-   SnakeGameLauncher.java

------------------------------------------------------------------------

## 🧠 Technical Highlights

-   Real-time grid rendering using Swing Timer
-   Strategy Pattern for algorithm selection
-   Clean separation of game state and UI
-   Collision management system
-   Heuristic-based pathfinding

------------------------------------------------------------------------

## 📝 License

MIT License

------------------------------------------------------------------------

## 👨‍💻 Author

Ndivhuwo Neswiswi\
GitHub: https://github.com/NdivhuwoNeswiswi
