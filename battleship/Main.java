package battleship;

import javax.swing.*;
import java.util.*;
import java.util.function.ToDoubleBiFunction;

class Ship {
    String name;
    int length;
    String[] parts;
    int hitCount;
    boolean isSunk;

    public Ship(String name, int length) {
        this.name = name;
        this.length = length;
        this.parts = new String[length];
        this.hitCount = 0;
        this.isSunk = false;
    }
}

class Fleet {
    private List<Ship> ships;
    private int sunkCount;

    public Fleet() {
        this.ships = new ArrayList<>();
        this.sunkCount = 0;

        // Initialize the fleet with five ships of different lengths
        ships.add(new Ship("Aircraft Carrier", 5));
        ships.add(new Ship("Battleship", 4));
        ships.add(new Ship("Submarine", 3));
        ships.add(new Ship("Cruiser", 3));
        ships.add(new Ship("Destroyer", 2));
    }

    public boolean allShipsSunk() {
        return sunkCount == ships.size();
    }

    public void updateSunkCount() {
        sunkCount = 0;
        for (Ship ship : ships) {
            if (ship.isSunk) {
                sunkCount++;
            }
        }
    }

    // Getters
    public List<Ship> getShips() {
        return ships;
    }

    public int getSunkCount() {
        return sunkCount;
    }
}


public class Main {

    static List<Character> conversor = Arrays.asList(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'
    );

    // List to store invalid positions
    static List<String> invalidCells = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[][] field = new String[11][11];
        String[][] fogOfWar = new String[11][11];

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j == 0) {
                    field[i][j] = "";
                    fogOfWar[i][j] = "";
                    continue;
                }

                if (i == 0) {
                    field[i][j] = String.valueOf(j);
                    fogOfWar[i][j] = String.valueOf(j);
                    continue;
                }

                if (j == 0) {
                    field[i][j] = String.valueOf(conversor.get(i - 1));
                    fogOfWar[i][j] = String.valueOf(conversor.get(i - 1));
                    continue;
                }

                field[i][j] = "~";
                fogOfWar[i][j] = "~";
            }
        }

        printField(field);

        Fleet fleet = new Fleet();

        // Ship placement loop
        for (Ship ship : fleet.getShips()) {
            System.out.printf("Enter the coordinates of the %s (%d cells):", ship.name, ship.length);

            boolean shipPlaced = false;

            do {
                String coords = scanner.nextLine();
                String firstCoord = coords.split(" ")[0];
                String secondCoord = coords.split(" ")[1];

                char firstRow = firstCoord.substring(0, 1).charAt(0);
                int firstColumn = Integer.parseInt(firstCoord.substring(1));
                char secondRow = secondCoord.substring(0, 1).charAt(0);
                int secondColumn = Integer.parseInt(secondCoord.substring(1));

                Optional<String> placementError = checkShipPlacement(firstRow, firstColumn, secondRow, secondColumn,
                        ship.length, field, ship.name);

                if (placementError.isPresent()) {
                    System.out.println(placementError.get());
                    continue;
                } else {
                    boolean isAscending;
                    boolean isVertical = firstColumn == secondColumn;
                    if (isVertical) {
                        isAscending = firstRow < secondRow;
                    } else {
                        isAscending = firstColumn < secondColumn;
                    }

                    int max = isVertical ? Math.max(firstRow, secondRow) : Math.max(firstColumn, secondColumn);
                    int min = isVertical ? Math.min(firstRow, secondRow) : Math.min(firstColumn, secondColumn);

                    int length = max - min + 1;

                    if (isVertical) {
                        if (isAscending) {
                            for (int i = 0; i < length; i++) {
                                ship.parts[i] = String.valueOf((char) (firstRow + i)) + (firstColumn);
                            }
                        } else {
                            for (int i = 0; i < length; i++) {
                                ship.parts[i] = String.valueOf((char) (firstRow - i)) + (firstColumn);
                            }
                        }
                    } else {
                        if (isAscending) {
                            for (int i = 0; i < length; i++) {
                                ship.parts[i] = String.valueOf(firstRow) + (firstColumn + i);
                            }
                        } else {
                            for (int i = 0; i < length; i++) {
                                ship.parts[i] = String.valueOf(firstRow) + (firstColumn - i);
                            }
                        }
                    }

                    for (String part : ship.parts) {
                        int row = part.substring(0, 1).charAt(0) - 'A';
                        int col = Integer.parseInt(part.substring(1)) - 1;
                        field[row + 1][col + 1] = "O";
                    }

                    // Define directions to check (vertically, horizontally, and diagonally)
                    int[] dRow = {-1, -1, -1, 0, 1, 1, 1, 0};
                    int[] dCol = {-1, 0, 1, 1, 1, 0, -1, -1};

                    // Iterate through each part of the ship
                    for (String part : ship.parts) {
                        int row = part.charAt(0) - 'A' + 1; // Convert letter to row index
                        int col = Integer.parseInt(part.substring(1)); // Extract column number

                        // Check all 8 surrounding directions
                        for (int i = 0; i < 8; i++) {
                            int newRow = row + dRow[i];
                            int newCol = col + dCol[i];

                            // Ensure the cell is within bounds before adding it to the invalid list
                            if (newRow > 0 && newRow <= field.length - 1 && newCol > 0 && newCol <= field[0].length - 1) {
                                String invalidCoord = String.valueOf((char) ('A' + newRow - 1)) + newCol;

                                // Avoid duplicates
                                if (!invalidCells.contains(invalidCoord)) {
                                    invalidCells.add(invalidCoord);
                                }
                            }
                        }
                    }
                    shipPlaced = true;

                    for (String[] row : field) {
                        for (int i = 0; i < row.length; i++) {
                            System.out.print(row[i] + " ");
                        }
                        System.out.println();
                    }
                }
            } while (!shipPlaced);
        }

        System.out.println("The game starts!");

        printField(fogOfWar);

        // Shooting loop
        System.out.println("Take a shot!");
        String message = "";

        do {
            String shotCoords = scanner.nextLine();
            char row = shotCoords.charAt(0);
            int col = Integer.parseInt(shotCoords.substring(1));

            message = checkShot(row, col, field, fogOfWar, fleet);

            System.out.println(message);
        } while (!fleet.allShipsSunk());
    }

    public static Optional<String> checkShipPlacement(char firstRow, int firstColumn, char secondRow, int secondColumn,
                                                      int length, String[][] field, String shipName) {
        if (isOutOfBounds(firstRow, firstColumn, secondRow, secondColumn) || isDiagonal(firstRow, firstColumn, secondRow, secondColumn)) {
            return Optional.of("Error! Wrong ship location! Try again:\n");
        }

        boolean isAscending;
        boolean isVertical = firstColumn == secondColumn;
        if (isVertical) {
            isAscending = firstRow < secondRow;
        } else {
            isAscending = firstColumn < secondColumn;
        }

        int max = isVertical ? Math.max(firstRow, secondRow) : Math.max(firstColumn, secondColumn);
        int min = isVertical ? Math.min(firstRow, secondRow) : Math.min(firstColumn, secondColumn);

        int inputLength = max - min + 1;

        String[] parts = new String[length];

        if (isVertical) {
            if (isAscending) {
                for (int i = 0; i < length; i++) {
                    parts[i] = String.valueOf((char) (firstRow + i)) + (firstColumn);
                }
            } else {
                for (int i = 0; i < length; i++) {
                    parts[i] = String.valueOf((char) (firstRow - i)) + (firstColumn);
                }
            }
        } else {
            if (isAscending) {
                for (int i = 0; i < length; i++) {
                    parts[i] = String.valueOf(firstRow) + (firstColumn + i);
                }
            } else {
                for (int i = 0; i < length; i++) {
                    parts[i] = String.valueOf(firstRow) + (firstColumn - i);
                }
            }
        }


        if (inputLength != length) {
            return Optional.of("Error! Wrong length of the " + shipName + "! Try again:\n");
        } else if (!arePositionsValid(firstRow, firstColumn, secondRow, secondColumn, invalidCells, parts)) {
            return Optional.of("Error! You placed it too close to another one. Try again:\n");
        }

        return Optional.empty();
    }

    public static void printField(String[][] field) {
        for (String[] row : field) {
            for (int i = 0; i < row.length; i++) {
                System.out.print(row[i] + " ");
            }
            System.out.println();
        }
    }

    public static boolean isOutOfBounds(char row, int col) {
        return row < 'A' || row > 'J' || col < 1 || col > 10;
    }

    public static boolean isOutOfBounds(char firstRow, int firstColumn, char secondRow, int secondColumn) {
        return firstRow < 'A' || firstRow > 'J' || secondRow < 'A' || secondRow > 'J' || firstColumn < 1 || firstColumn > 10 || secondColumn < 1 || secondColumn > 10;
    }

    public static boolean arePositionsValid(char firstRow, int firstColumn, char secondRow, int secondColumn,
                                            List<String> invalidCells, String[] parts) {
        for (String part : parts) {
            if (invalidCells.contains(part)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDiagonal(char firstRow, int firstColumn, char secondRow, int secondColumn) {
        if (firstColumn != secondColumn && firstRow != secondRow) {
            return true;
        }
        return false;
    }

    public static String checkShot(char row, int col, String[][] field, String[][] fogOfWar, Fleet fleet) {
        int rowIndex = row - 'A';
        int colIndex = col - 1;

        String coords = row + String.valueOf(col);

        if (isOutOfBounds(row, col)) {
            return "Error! You entered the wrong coordinates! Try again:\n";
        }

        // Check if the cell was already hit or missed
        if (field[rowIndex + 1][colIndex + 1].equals("X")) {
            printField(fogOfWar);
            return "You hit a ship! Try again:";
        } else if (field[rowIndex + 1][colIndex + 1].equals("M")) {
            printField(fogOfWar);
            return "You missed! Try again:";
        }

        for (Ship ship : fleet.getShips()) {
            if (ship.isSunk) {
                continue;
            }
            
            // TODO: D8 is hit but player aims again and has to hit again and not change field

            // TODO: add a Set so that no same coord can be shot twice

            for (int i = 0; i < ship.parts.length; i++) {
                if (coords.equals(ship.parts[i])) {
                    ship.parts[i] = "X";
                    field[rowIndex + 1][colIndex + 1] = "X";
                    fogOfWar[rowIndex + 1][colIndex + 1] = "X";
                    ship.hitCount++;
                    ship.isSunk = isShipSunk(ship.length, ship.hitCount);

                    fleet.updateSunkCount();

                    printField(fogOfWar);

                    if (fleet.allShipsSunk()) {
                        return "You sank the last ship. You won. Congratulations!";
                    }

                    return isShipSunk(ship.length, ship.hitCount) ? "You sank a ship! Specify a new target:" : "You " +
                            "hit a ship! Try again:";
                }
            }
        }
        field[rowIndex + 1][colIndex + 1] = "M";
        fogOfWar[rowIndex + 1][colIndex + 1] = "M";
        printField(fogOfWar);
        return "You missed! Try again:";
    }

    public static boolean isShipSunk(int length, int hitCount) {
        return length == hitCount;
    }

}