import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Random;

/**
 * PA #2 -- Battleship
 *
 * Implement a single-player Battleship game on a 10x10 board.
 *
 * Startup input (one line from stdin):
 *   N MODE FILE_NAME
 *   - N        : number of bombs (positive integer)
 *   - MODE     : d/D (Debug) or r/R (Release)
 *   - FILE_NAME: board file path (may contain spaces)
 *
 * Submit this file as: Battleship.java
 * - Public class name must be exactly "Battleship"
 * - No Korean comments allowed
 * - Must compile cleanly: javac Battleship.java
 */
public class Battleship {

    private static final int  BOARD_SIZE  = 10;
    private static final long RANDOM_SEED =
        Long.parseLong(System.getProperty("seed", "2026"));

    // === Board State ===
    // TODO: declare fields for baseBoard, shot array, shipRef array, score
    //       Suggested types:
    //         char[][]  baseBoard  -- ship characters or ' '
    //         boolean[][] shot     -- true if this cell has been targeted
    //         Ship[][]  shipRef    -- reference to the Ship object at each cell
    //         int       score

    private char[][] baseBoard = new char[BOARD_SIZE][BOARD_SIZE];
    private boolean[][] shot = new boolean[BOARD_SIZE][BOARD_SIZE];
    private Ship[][] shipRef = new Ship[BOARD_SIZE][BOARD_SIZE];
    private int score = 0;
    
    // === Entry Point ===

    public static void main(String[] args) {
        // TODO:
        //   1. Create a BufferedReader from System.in
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	
        //   2. Call parseStartupLine() -- catch BombInputException / ModeInputException,
        //      print the exception class simple name, and return
    	try {
    		StartupConfig config = parseStartupLine(reader);
    	
    	
        //   3. Create a Battleship instance and call initializeBoard()
    		Battleship game = new Battleship();
    		game.initializeBoard(config.fileName);
    	
        //   4. Call play()
    		game.play(config.bombs, config.mode, reader);
    	} 
    	catch (BombInputException | ModeInputException e) {
            System.out.println(e.getClass().getSimpleName());
            return;
    	}
    	
        //   5. Catch IOException: print "IOException" to stdout and return
    	catch (IOException e) {
    		System.out.println("IOException");
            return;
    	}
    }

    // === Startup Parsing ===

    /**
     * Reads one non-empty line from reader and parses it as:
     *   N MODE FILE_NAME
     *
     * FILE_NAME is everything after MODE (may contain spaces).
     *
     * @throws BombInputException  if N is missing, not an integer, or <= 0
     * @throws ModeInputException  if MODE is not one of d, D, r, R,
     *                             or if MODE/FILE_NAME tokens are missing
     */
    private static StartupConfig parseStartupLine(BufferedReader reader)
            throws IOException, BombInputException, ModeInputException {
        // TODO
    	String inputLine = reader.readLine();
    	
    	if (inputLine == null || inputLine.trim().isEmpty()) {
    		throw new BombInputException();
    	}
    	
    	String[] tokens = inputLine.trim().split("\\s+", 3);
       
        int bombs;
        try 
        {
        	bombs = Integer.parseInt(tokens[0]);
        	if (bombs <= 0) 
        	{
        		throw new BombInputException();
        	}
        } 
        catch (NumberFormatException e) 
        {
        	throw new BombInputException();
        }
        
        if (tokens.length < 3) 
        {
            throw new ModeInputException();
        }
        
        String modeToken = tokens[1];
        Mode mode;
        if (modeToken.equals("d") || modeToken.equals("D")) 
        {
            mode = Mode.DEBUG;
        } 
        else if (modeToken.equals("r") || modeToken.equals("R")) 
        {
            mode = Mode.RELEASE;
        } 
        else 
        {
            throw new ModeInputException();
        }
        
        return new StartupConfig(bombs, mode, tokens[2]);
    }

    // === Board Initialisation ===

    /**
     * Initializes all board state to empty / false / null.
     * Resets score to 0.
     */
    private void clearBoard() {
    	for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                baseBoard[i][j] = ' ';
                shot[i][j] = false;
                shipRef[i][j] = null;
            }
        }
        score = 0;
    }

    /**
     * If the file at fileName exists, calls loadBoardFromFile().
     * Otherwise calls generateRandomBoard(new Random(RANDOM_SEED)).
     */
    private void initializeBoard(String fileName) throws IOException {
        // TODO
    	clearBoard();
    	
    	java.io.File file = new java.io.File(fileName);
        
        if (file.exists() && !file.isDirectory()) 
        {
            loadBoardFromFile(file.toPath());
        } 
        else 
        {
            generateRandomBoard(new Random(RANDOM_SEED));
        }
    }

    /**
     * Reads a 10-line board file. Each line is exactly 10 characters
     * (space-pad lines shorter than 10). Valid ship characters: A B S D P.
     * Populates baseBoard and shipRef.
     *
     * Ship segments are recognized:
     *   - Horizontal: consecutive same-type characters in the same row (length >= 2)
     *   - Vertical  : consecutive same-type characters in the same column
     *   - Isolated single cell: treated as its own ship object
     */
    private void loadBoardFromFile(java.nio.file.Path path) throws IOException {
        java.util.List<String> lines = java.nio.file.Files.readAllLines(path);

        for (int i = 0; i < BOARD_SIZE; i++) {
            String line = (i < lines.size()) ? lines.get(i) : "";

            for (int j = 0; j < BOARD_SIZE; j++) {
                char c = (j < line.length()) ? line.charAt(j) : ' ';
                baseBoard[i][j] = c;
            }
        }

        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {

                char c = baseBoard[i][j];

                if (c == ' ' || visited[i][j]) continue;

                Ship ship = createShipByType(c);

                // Horizontal
                if (j + 1 < BOARD_SIZE && baseBoard[i][j + 1] == c) {
                    int col = j;
                    while (col < BOARD_SIZE && baseBoard[i][col] == c) {
                        shipRef[i][col] = ship;
                        visited[i][col] = true;
                        col++;
                    }
                }
                // Vertical
                else if (i + 1 < BOARD_SIZE && baseBoard[i + 1][j] == c) {
                    int row = i;
                    while (row < BOARD_SIZE && baseBoard[row][j] == c) {
                        shipRef[row][j] = ship;
                        visited[row][j] = true;
                        row++;
                    }
                }
                // Isolated single cell
                else {
                    shipRef[i][j] = ship;
                    visited[i][j] = true;
                }
            }
        }
    }
    
    private Ship createShipByType(char c) {
        if (c == 'A') return new AircraftCarrier();
        if (c == 'B') return new BattleshipShip();
        if (c == 'S') return new Submarine();
        if (c == 'D') return new Destroyer();
        if (c == 'P') return new PatrolBoat();
        return null;
    }

    /**
     * Places all ships randomly using the provided Random instance.
     *
     * Ship placement order (MUST follow exactly for deterministic output):
     *   AircraftCarrier x1, BattleshipShip x2, Submarine x2,
     *   Destroyer x1, PatrolBoat x4
     *
     * Per attempt:
     *   boolean horizontal = rng.nextBoolean();
     *   int row = rng.nextInt(10);
     *   int col = rng.nextInt(10);
     *
     * Retry (call rng again in the same order) if the placement is invalid.
     */
    private void generateRandomBoard(Random rng) {
        Ship[] shipsToPlace = {
            new AircraftCarrier(),
            new BattleshipShip(), new BattleshipShip(),
            new Submarine(), new Submarine(),
            new Destroyer(),
            new PatrolBoat(), new PatrolBoat(), new PatrolBoat(), new PatrolBoat()
        };

        for (Ship ship : shipsToPlace) {
            boolean placed = false;
            while (!placed) {
                boolean horizontal = rng.nextBoolean();
                int row = rng.nextInt(10);
                int col = rng.nextInt(10);

                if (canPlace(row, col, ship.size, horizontal)) {
                    placeShip(ship, row, col, horizontal);
                    placed = true;
                }
            }
        }
    }

    
    /**
     * Returns true if a ship of the given size can be placed at (row, col)
     * in the given direction without overlapping or touching any existing ship.
     */
    private boolean canPlace(int row, int col, int size, boolean horizontal) {
        if (horizontal && col + size > BOARD_SIZE) return false;
        if (!horizontal && row + size > BOARD_SIZE) return false;

        int startRow = Math.max(0, row - 1);
        int endRow = Math.min(BOARD_SIZE - 1, horizontal ? row + 1 : row + size);
        int startCol = Math.max(0, col - 1);
        int endCol = Math.min(BOARD_SIZE - 1, horizontal ? col + size : col + 1);

        for (int r = startRow; r <= endRow; r++) {
            for (int c = startCol; c <= endCol; c++) {
                if (baseBoard[r][c] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    
    /**
     * Places the ship on the board starting at (row, col).
     * Updates baseBoard and shipRef for every cell the ship occupies.
     */
    private void placeShip(Ship ship, int row, int col, boolean horizontal) {
        for (int i = 0; i < ship.size; i++) {
            int r = horizontal ? row : row + i;
            int c = horizontal ? col + i : col;
            baseBoard[r][c] = ship.type;
            shipRef[r][c] = ship;
        }
    }

    
    
    // === Game Loop ===

    /**
     * Main game loop.
     *
     * Repeats until all bombs are used:
     *   - Debug mode  : print board, then read a coordinate
     *   - Release mode: read a coordinate (no board print)
     *
     * After processing each input:
     *   - Valid new coordinate : call shoot(), increment bomb counter
     *   - Invalid / repeated   : print "Try again", do NOT increment counter
     *
     * When all bombs are used: print final board, then "Score N".
     */
    private void play(int bombs, Mode mode, BufferedReader reader) throws IOException {
    	int remainingBombs = bombs;
    	
    	while (remainingBombs > 0) {
            if (mode == Mode.DEBUG) {
                printBoard();
            }

            String input = reader.readLine();
            if (input == null) break; 
            input = input.trim();

            try {
                int[] coords = parseCoordinate(input);
                shoot(coords[0], coords[1]);
                remainingBombs--;
            } catch (HitException e) {
                System.out.println("Try again");
            }
        }
    	
    	printBoard();
        System.out.println("Score " + score);
    }
    	

    /**
     * Parses a coordinate string (e.g., "A1", "j10").
     *
     * Rules:
     *   - First character must be a letter A-J (case-insensitive)
     *   - Remaining characters must form an integer 1-10
     *   - The cell must not have been shot before
     *
     * Throws HitException for any invalid or repeated input.
     * Returns int[]{row, col} (0-indexed) on success.
     */
    private int[] parseCoordinate(String token) throws HitException {
    	if (token == null || token.length() < 2) throw new HitException();

        char colChar = Character.toUpperCase(token.charAt(0));
        if (colChar < 'A' || colChar > 'J') throw new HitException();

        int col = colChar - 'A';

        String numPart = token.substring(1);

        if (!numPart.matches("^[1-9]$|^10$")) {
            throw new HitException();
        }

        int row = Integer.parseInt(numPart) - 1;
        

        if (shot[row][col]) throw new HitException();

        return new int[]{row, col};
    }

    
    
    /**
     * Marks (row, col) as shot.
     * Prints "Miss" or "Hit X" (X = uppercase ship character).
     * Updates score by adding ship.size for a hit.
     */
    private void shoot(int row, int col) {
    	shot[row][col] = true;
        char c = baseBoard[row][col];

        if (c == ' ') {
            System.out.println("Miss");
        } else {
            System.out.println("Hit " + Character.toUpperCase(c));
            Ship s = shipRef[row][col];
            if (s != null) {
                score += s.size;
            }
        }
    }

    // === Display ===

    /**
     * Prints the current board state to stdout.
     *
     * Format:
     *   "  A B C D E F G H I J"
     *   "  - - - - - - - - - -"
     *   "1 | <cells...>"
     *   ...
     *   "10 | <cells...>"
     *
     * Cell rendering:
     *   - Not shot, empty  : " "
     *   - Not shot, ship   : ship character (uppercase)
     *   - Shot, empty      : "X"
     *   - Shot, ship       : "X" + ship character (lowercase)   e.g. "Xp", "Xa"
     *
     * Trailing spaces must be stripped from every line.
     */
    private void printBoard() {
    	System.out.println("  A B C D E F G H I J");
        System.out.println("  - - - - - - - - - -");
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            StringBuilder sb = new StringBuilder();
            
            if (i < 9) {
                sb.append((i + 1)).append(" | ");
            } else {
                sb.append((i + 1)).append(" | ");
            }

            for (int j = 0; j < BOARD_SIZE; j++) {
                String cellStr;
                if (!shot[i][j]) {
                    cellStr = baseBoard[i][j] + " ";
                } else {
                    if (baseBoard[i][j] == ' ') {
                        cellStr = "X ";
                    } else {
                        cellStr = "X" + Character.toLowerCase(baseBoard[i][j]) + " ";
                    }
                }
                sb.append(cellStr);
            }
            
            String finalLine = sb.toString().replaceAll("\\s+$", "");
            System.out.println(finalLine);
        }
    }

    // === Inner Types ===

    /** Execution mode. */
    private enum Mode { DEBUG, RELEASE }

    /** Holds parsed startup parameters. */
    private static class StartupConfig {
        final int    bombs;
        final Mode   mode;
        final String fileName;

        StartupConfig(int bombs, Mode mode, String fileName) {
            this.bombs    = bombs;
            this.mode     = mode;
            this.fileName = fileName;
        }
    }

    // === Ship Hierarchy ===

    /**
     * Abstract base class for all ship types.
     * Fields:
     *   type -- single uppercase character identifying the ship (A, B, S, D, P)
     *   size -- number of cells the ship occupies
     *   hits -- number of times this ship has been hit (optional to use)
     */
    private abstract static class Ship {
        final char type;
        final int  size;
        int        hits;

        Ship(char type, int size) {
            this.type = type;
            this.size = size;
            this.hits = 0;
        }
    }

    /** Aircraft Carrier: type='A', size=6, count=1 */
    private static final class AircraftCarrier extends Ship {
        AircraftCarrier() { super('A', 6); }
    }

    /** Battleship: type='B', size=4, count=2 */
    private static final class BattleshipShip extends Ship {
        BattleshipShip() { super('B', 4); }
    }

    /** Submarine: type='S', size=3, count=2 */
    private static final class Submarine extends Ship {
        Submarine() { super('S', 3); }
    }

    /** Destroyer: type='D', size=3, count=1 */
    private static final class Destroyer extends Ship {
        Destroyer() { super('D', 3); }
    }

    /** Patrol Boat: type='P', size=2, count=4 */
    private static final class PatrolBoat extends Ship {
        PatrolBoat() { super('P', 2); }
    }

    // === Exceptions ===

    /** Thrown when N is not a positive integer. */
    private static class BombInputException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    /** Thrown when MODE is not d, D, r, or R. */
    private static class ModeInputException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    /**
     * Thrown when a coordinate is already shot, out of range, or malformed.
     * Caught in the game loop; prints "Try again" without consuming a bomb.
     */
    private static class HitException extends Exception {
        private static final long serialVersionUID = 1L;
    }
}
