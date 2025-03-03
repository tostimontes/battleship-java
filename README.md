# Battleship Game - Hyperskill Project

## Overview

This is a Battleship game implemented in Java as part of my learning path on [Hyperskill](https://hyperskill.org/). This project showcases my ability to work with object-oriented programming principles, data structures, and game logic. The game allows two players to place their ships on a grid and take turns firing at the opponent's ships until one player's entire fleet is sunk.

## Skills Demonstrated  

- **Object-Oriented Programming (OOP)**: The project follows OOP principles by defining `Ship` and `Fleet` classes to encapsulate ship properties and game mechanics.
- **Data Structures & Collections**: Utilizes `List`, `ArrayList`, and `String[][]` arrays to manage game data, such as ship locations and battlefield representations.
- **Game State Management**: Implements methods to track ship placement, validate ship positioning, and determine when a fleet is completely sunk.
- **Input Handling & Validation**: Ensures players provide valid coordinates for ship placement and firing, checking for errors and providing meaningful feedback.
- **Algorithmic Thinking**: Implements logic for checking ship placement constraints and validating game rules.

## Features

- Two-player turn-based gameplay.
- Ship placement validation (ensuring ships are placed correctly and do not overlap or touch diagonally).
- Fog of war for hidden enemy ships.
- Game loop that continues until one player sinks all enemy ships.

## How to Play

1. Player 1 places ships on their game field.
2. Player 2 places ships on their game field.
3. Players take turns guessing coordinates to fire at enemy ships.
4. The game continues until all ships in one fleet are sunk.
5. The player who sinks all opponent ships wins!

## Potential Improvements

- Implement an AI opponent for single-player mode.
- Improve the UI with a graphical representation of the board.
- Add error handling for unexpected inputs.
- Optimize ship placement validation to reduce redundant calculations.
- Introduce different difficulty levels.
