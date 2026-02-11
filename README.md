🐍 Snake Battle: Pathfinding Algorithms Showdown
A Java-based interactive game that combines classic Snake gameplay with real-time visualization of pathfinding algorithms and an exciting battle mode where different algorithms compete!

https://via.placeholder.com/800x450/2d3748/ffffff?text=Snake+Battle+Screenshot

🎮 Features
🐍 Classic Snake Game
Traditional snake gameplay with arrow key controls

Score tracking and collision detection

Smooth animations and intuitive controls

🔍 Pathfinding Algorithm Visualization
Depth-First Search (DFS): Blue cells show exploration with backtracking

Breadth-First Search (BFS): Yellow cells show level-by-level exploration

*A Search (A-Star)**: Purple cells show heuristic-based optimal pathfinding

Real-time visualization of algorithm exploration

Smart path reconstruction without backtracking

⚔️ Snake Battle Arena
Three algorithm-controlled snakes compete for food:

DFS Snake (Blue)

BFS Snake (Yellow)

A* Snake (Purple)

Real-time battle with collision detection

Score tracking and winner determination

Professional battle results screen

Intelligent AI with trap detection

🚀 Getting Started
Prerequisites
Java JDK 8 or higher

Git (for cloning the repository)

Installation
Clone the repository

bash
git clone https://github.com/yourusername/SnakeBattle.git
cd SnakeBattle
Compile the project

bash
javac snake-battle/src/main/java/com/ndivhuwo/snakebattle/*.java
Run the game

bash
java -cp snake-battle/src/main/java com.ndivhuwo.snakebattle.SnakeGameLauncher
Or simply:

bash
java com.ndivhuwo.snakebattle.SnakeGameLauncher
🎯 Game Modes
1. Human Play 🎮
Classic snake gameplay

Arrow keys for movement

Eat red apples to grow

Avoid walls and yourself

2. DFS Mode 🔍
Watch Depth-First Search algorithm in action

Blue cells = visited nodes

Green path = current exploration

Demonstrates backtracking behavior

3. BFS Mode 📊
Breadth-First Search visualization

Yellow cells = visited nodes

Finds shortest path to food

Explores level by level

4. A* Mode ⭐
A-Star algorithm visualization

Purple cells = visited (closed set)

Orange cells = nodes to evaluate (open set)

Uses heuristic + cost for optimal paths

5. Snake Battle ⚔️
Watch all three algorithms compete!

Real-time battle arena

Score tracking and statistics

Last snake standing wins

🎨 Controls
Common Controls (All Modes)
SPACE: Restart game/battle

ESC: Return to main menu

Close Window: Exit game

Human Mode Controls
↑ Arrow: Move up

↓ Arrow: Move down

← Arrow: Move left

→ Arrow: Move right

📊 Algorithm Comparison
Algorithm	Strategy	Strengths	Weaknesses	Color
DFS	Depth-first exploration	Simple implementation, low memory	May find longer paths, can get stuck	Blue
BFS	Level-by-level exploration	Finds shortest path	Explores many cells, higher memory	Yellow
A*	Cost + heuristic	Most efficient, optimal paths	More complex implementation	Purple
🏗️ Project Structure
text
src/
└── main/
    └── java/
        └── com/
            └── ndivhuwo/
                └── snakebattle/
                    ├── SnakeGame.java              # Main window with CardLayout
                    ├── GamePanel.java              # Main game logic and rendering
                    ├── GameMenu.java               # Start menu with mode selection
                    ├── DFSVisualizer.java          # DFS algorithm implementation
                    ├── BFSVisualizer.java          # BFS algorithm implementation
                    ├── AStarVisualizer.java        # A* algorithm implementation
                    ├── BattleManager.java          # Snake battle arena controller
                    ├── BattleSnake.java            # Individual battle snake
                    └── SnakeGameLauncher.java      # Application entry point
🧠 Technical Details
Key Features Implemented
State Management: Multiple game modes with clean state transitions

Collision Detection: Snake-to-snake, snake-to-wall, and head-on collisions

Pathfinding Algorithms: Three different search strategies with visualization

Real-time Animation: Smooth frame updates using Swing Timer

Intelligent AI: Algorithm snakes with trap detection and pathfinding

Professional UI: Clean menus, color-coded visuals, and informative HUD

Design Patterns Used
Model-View-Controller (MVC): Separation of game logic and UI

Strategy Pattern: Different algorithms for different snakes

Observer Pattern: Event-driven game loop

Factory Pattern: Creation of different snake types

📈 Educational Value
This project serves as an excellent educational tool for:

Understanding pathfinding algorithms visually

Learning game development in Java

Studying algorithm efficiency and trade-offs

Exploring object-oriented design patterns

Practicing collision detection and game physics

🛠️ Development
Building from Source
bash
# Compile all Java files
find . -name "*.java" -exec javac {} \;

# Run the game
java com.ndivhuwo.snakebattle.SnakeGameLauncher
Dependencies
Pure Java with Swing (no external dependencies)

Works on any platform with Java 8+

🤝 Contributing
Contributions are welcome! Here's how you can help:

Report Bugs: Open an issue with detailed reproduction steps

Suggest Features: Share your ideas for new modes or improvements

Submit Pull Requests:

Fork the repository

Create a feature branch

Make your changes

Submit a pull request

Areas for Improvement
Add more pathfinding algorithms (Dijkstra, Greedy Best-First)

Implement maze generation with obstacles

Add sound effects and background music

Create network multiplayer mode

Add difficulty levels and speed controls

Implement save/load game functionality

📝 License
This project is licensed under the MIT License - see the LICENSE file for details.

🙏 Acknowledgments
Inspired by classic Snake game

Educational resources on pathfinding algorithms

Java Swing documentation and tutorials

The open-source community for inspiration and guidance

📧 Contact
Ndivhuwo Neswiswi

GitHub: @NdivhuwoNeswiswi

Project Link: https://github.com/NdivhuwoNeswiswi/SnakeBattle

⭐ Show Your Support
If you find this project useful or educational, please give it a ⭐ on GitHub!
