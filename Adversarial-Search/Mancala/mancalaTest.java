package Mancala;

public class mancalaTest {
    public static void main(String[] args) {
        Board board = new Board();
        MancalaAI player1 = new MancalaAI(4);
        MancalaAI player2 = new MancalaAI(3);


        // Game loop
        while (!board.isGameOver()) {
            int currentPlayer = board.getCurrentPlayer();
            // Print the current board state
            System.out.println("Current Board: ");
            System.out.println(board);

            // Determine the move for the current player
            int currentMove;
            if (currentPlayer == 0) {
                currentMove = player1.chooseMove(board);
                System.out.println("PLayer 1's move: "+currentMove);
            } else {
                currentMove = player2.chooseMove(board);
                System.out.println("PLayer 2's move: "+currentMove);
            }

            // Make the move for the current player
            boolean additionalmove = board.makeMove(currentMove);

            //Extra move if last index is in store
            if(additionalmove) {
                continue;
            }

            // Switch to the next player
            board.switchPlayer();
        }

        // Print the final board state and the winner
        System.out.println("Final Board: ");
        System.out.println(board);

        int player1Score = board.getStores()[0];
        int player2Score = board.getStores()[1];

        if (player1Score > player2Score) {
            System.out.println("Player 1 wins!");
        } else if (player2Score > player1Score) {
            System.out.println("Player 2 wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }

}

