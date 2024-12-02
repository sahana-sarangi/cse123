//Sahana Sarangi
//1 November 2024

import java.util.*;

//Creates a two-player Chomp game by extending AbstractStrategyGame. 
public class Chomp extends AbstractStrategyGame {

    private int[][][] board;
    private static final int HEIGHT = 3;
    private int length;
    private int width;
    public boolean p2turn;


    //This constructor initializes a new gameboard for a Chomp game. It also initializes
    //the first player as Player 1. 
    //Parameters:
    //  - length: an integer number of rows the user wants the gameboard to have
    //  - width: an integer number of columns the user wants the gameboard to have
    public Chomp(int length, int width) {
        this.length = length;
        this.width = width;
        this.board = new int[HEIGHT][length][width];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < length; j++) {
                for (int k = 0; k < width; k++) {
                    board[i][j][k] = 1;
                }
            }
        }
        board[0][0][0] = 0;
        p2turn = false;
    }

    //This method returns the instructions for the chomp game as a string.
    public String instructions() {
        return "Two players alternate turns to play this game. When prompted, the current" +
                " player chooses the row and column of the square on the gameboard they would" +
                " like to 'chomp.' When a square is chomped, all squares in that square's layer" +
                " above and to the right of it are also chomped. Chomped squares are identified" +
                " using the number 3, and unchomped squares are identified using the number 1." +
                " Chomping squares leaves reveals the unchomped squares beneath, until there" +
                " are no more layers to reveal. Whichever player chomps the top leftmost" +
                " square in the final layer is the loser. This square is identified using the" +
                " number 0.";     
    }


    //Behavior: This method finds whether or not there is a winner for the chomp game, and if
    // there is, an integer representing the player that won is returned. Players cannot tie, so
    // the method does not consider that case.
    //Return: If player 2 won the game, the method returns 2. If player 1 won the game, the
    // method returns 1. If nobody has won yet, it returns -1.
    public int getWinner() {
        if (board[0][0][0] != 0) {
            if (p2turn == true) {
                return 2;
            } else if (p2turn == false) {
                return 1;
            } 
        } 
        return -1;
    }


    //Behavior: finds and returns which player's turn it is, if the game is not over.
    //Return: if the game is over, -1 is returned. If it is player 1's turn, 1 is returned.
    // If it is player 2's turn, 2 is returned.
    public int getNextPlayer() {
        if (isGameOver()) {
            return -1;
        } else if (p2turn == false) {
            return 1;
        } else {
            return 2;
        }
    }


    //Behavior: this method allows the player whose turn it is to make a move by prompting them 
    //for the row and column of the square they want to chomp. If there is at least one layer of
    //squares at the specified row and column, that square and all the squares to the right and 
    //below it will be chomped. The method then changes which player's turn it is.
    //Exceptions:
    //  - If the user indicates they want to chomp a square at a row or column that is outside of
    //    the bounds of the gameboard, an IllegalArgumentException is thrown.
    //  - If the user indicates they want to chomp a square at a row or column that has no more 
    //    layers of squares left, an IllegalArgumentException is thrown.
    //Parameters:
    //  - input: a non-null scanner that scans over the user's input (the row/column they want
    //           to chomp at)
    public void makeMove(Scanner input) {
        System.out.println("Which column is the piece you want to chomp in?");
        int column = input.nextInt();
        System.out.println("Which row is the piece you want to chomp in?");
        int row = input.nextInt();

        if (row >= length || row < 0 || column >= width || column < 0) {
            throw new IllegalArgumentException("Square with coordinates (" + column + ", " + row
                    + ") is out of bounds (does not exist).");
        }

        if (board[0][row][column] == 3 && board[1][row][column] == 3 && 
                board[2][row][column] == 3) {
                throw new IllegalArgumentException("Squares with coordinates (" + column +
                        ", " + row + ") in all layers have already been chomped.");
        }

        int currLayer = 2;
        boolean hasChomped = false;
        while (currLayer != -1 && !hasChomped) {
            if (board[currLayer][row][column] != 3) {
                chomp(currLayer, row, column);
                hasChomped = true;
            }
            currLayer--;
        }
        p2turn = !p2turn;
    }


    //Behavior: chomps the square at the user's specified row and column, as well as all the 
    // squares to the right and below it. Chomped squares are replaced with the integer 3
    // to indicate they are chomped.
    //Parameters:
    //  - layer: the layer (integer) at which squares need to be chomped at.
    //  - row: the row (integer) at which the user wants to chomp a square at
    //  - column: the column (integer) at which the user wants to chomp a square at
    private void chomp(int layer, int row, int column) {
        for (int i = row; i < length; i++) {
            for (int j = column; j < width; j++) {
                board[layer][i][j] = 3;
            }
        }
    }


    //This method returns the string representation of the chomp gameboard. Each layer of the
    // gameboard is displayed separately, with 3 in the place of chomped squares, 0 in the place
    // of the top leftmost square, and 1 in the place of unchomped squares.
    public String toString() {
        String currBoard = "";
        for (int i = 2; i > -1; i--) {
            for (int j = 0; j < length; j++) {
                for (int k = 0; k < width; k++) {
                    currBoard += Integer.toString(board[i][j][k]);
                }
                currBoard += "\n";
            }
            currBoard += "\n\n";
        }
        return currBoard;
    }
}
