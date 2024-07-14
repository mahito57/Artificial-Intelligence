package Mancala;

import java.util.Arrays;


public class Board implements Cloneable{
    private int[] pits; // Array representing the pits of the board
    private int[] stores; // Array representing the stores (mancalas) of each player
    private int currentPlayer; // Current player
    private boolean additionalmoveearned;
    private int stonesCaptured;

    public Board() {
        pits = new int[14];
        stores = new int[2];
        Arrays.fill(pits, 0, 6, 4);
        Arrays.fill(pits, 7, 13, 4);
        currentPlayer = 0; // Set the initial current player (0 for player 1)
    }

//    public int[] getPits() {
//        return pits.clone();
//    }

    public int[] getStores() {
        return stores.clone();
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getOpponentPlayer(int currentPlayer) {
        return (currentPlayer == 0) ? 1 : 0;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 0) ? 1 : 0;
    }

    public int getStonesCaptured(){
        return stonesCaptured;
    }

    public boolean isGameOver() {
        // Game is over if either player has no stones in their pits
        boolean player1PitsEmpty = true;
        boolean player2PitsEmpty = true;

        for (int i = 0; i < 6; i++) {
            if (pits[i] > 0) {
                player1PitsEmpty = false;
                break;
            }
        }

        for (int i = 7; i < 13; i++) {
            if (pits[i] > 0) {
                player2PitsEmpty = false;
                break;
            }
        }

        return player1PitsEmpty || player2PitsEmpty;
    }

    public int evaluate1(int currentPlayer) {
        // Simple evaluation: the difference between the current player's store and the opponent's store
        int currentPlayerStore = stores[currentPlayer];
        int opponentPlayerStore = stores[getOpponentPlayer(currentPlayer)];
        return 10 * (currentPlayerStore - opponentPlayerStore);
    }

    public int evaluate2(int currentPlayer) {
        int currentPlayerStore = stores[currentPlayer];
        int opponentPlayerStore = stores[getOpponentPlayer(currentPlayer)];
        int stonesOnMySide = 0;
        int stonesOnOpponentSide = 0;

        if(currentPlayer == 0){
            for (int i = 0; i < 6; i++) {
                stonesOnMySide += pits[i];
                stonesOnOpponentSide += pits[i + 7];
            }
        } else {
            for (int i = 0; i < 6; i++) {
                stonesOnOpponentSide += pits[i];
                stonesOnMySide += pits[i + 7];
            }
        }


        // Apply the weights to calculate the final evaluation score
        int score = ( 6 * (currentPlayerStore - opponentPlayerStore) +
                4 * (stonesOnMySide - stonesOnOpponentSide) );

        return score;
    }

    public int evaluate3(int currentPlayer, int additionalMoveEarned) {
        int currentPlayerStore = stores[currentPlayer];
        int opponentPlayerStore = stores[getOpponentPlayer(currentPlayer)];
        int stonesOnMySide = 0;
        int stonesOnOpponentSide = 0;

        if(currentPlayer == 0){
            for (int i = 0; i < 6; i++) {
                stonesOnMySide += pits[i];
                stonesOnOpponentSide += pits[i + 7];
            }
        } else {
            for (int i = 0; i < 6; i++) {
                stonesOnOpponentSide += pits[i];
                stonesOnMySide += pits[i + 7];
            }
        }

        // Apply the weights to calculate the final evaluation score
        int score = (5 * (currentPlayerStore - opponentPlayerStore) +
                3 * (stonesOnMySide - stonesOnOpponentSide) +
                2 * additionalMoveEarned );

        return score;
    }

    public int evaluate4(int currentPlayer, int additionalMoveEarned, int stonesCaptured) {
        int currentPlayerStore = stores[currentPlayer];
        int opponentPlayerStore = stores[getOpponentPlayer(currentPlayer)];
        int stonesOnMySide = 0;
        int stonesOnOpponentSide = 0;

        if(currentPlayer == 0){
            for (int i = 0; i < 6; i++) {
                stonesOnMySide += pits[i];
                stonesOnOpponentSide += pits[i + 7];
            }
        } else {
            for (int i = 0; i < 6; i++) {
                stonesOnOpponentSide += pits[i];
                stonesOnMySide += pits[i + 7];
            }
        }

        // Apply the weights to calculate the final evaluation score
        int score = (4 * (currentPlayerStore - opponentPlayerStore) +
                3 * (stonesOnMySide - stonesOnOpponentSide) +
                2 * additionalMoveEarned +
                1 * stonesCaptured);

        return score;
    }


    public boolean makeMove(int pitIndex) {
        additionalmoveearned = false;
        int stones = pits[pitIndex];
        pits[pitIndex] = 0;
        int currentPlayerStore = (pitIndex < 6) ? 0 : 1;
        int currentIndex = pitIndex;
        while (stones > 0) {
            currentIndex = (currentIndex + 1) % 14;
            if (( currentPlayerStore == 0 && currentIndex != 13 ) || ( currentPlayerStore == 1 && currentIndex != 6 )) {
                pits[currentIndex]++;
                stones--;
            }
        }

        //Capturing Stone from opponent
        stonesCaptured = 0;
        if(currentPlayerStore == 0 && currentIndex < 6 && pits[currentIndex]==1){
            int oppositePitIndex = 12 - currentIndex;
            if(pits[oppositePitIndex] != 0){
                pits[6] += 1; //from current pit
                pits[6] += pits[oppositePitIndex]; //From opponents pit
                stonesCaptured += (pits[oppositePitIndex]+1);
                pits[currentIndex] = 0;
                pits[oppositePitIndex] = 0;
            }

        }else if(currentPlayerStore == 1 && currentIndex > 6 && currentIndex < 13 && pits[currentIndex]==1){
            int oppositePitIndex = 12 - currentIndex;
            if(pits[oppositePitIndex] != 0){
                pits[13] += 1; //from current pit
                pits[13] += pits[oppositePitIndex]; //From opponents pit
                stonesCaptured += (pits[oppositePitIndex]+1);
                pits[currentIndex] = 0;
                pits[oppositePitIndex] = 0;
            }
        }

        if(currentPlayerStore==0) {
            stores[0] = pits[6];
        }else stores[1] = pits[13];


        if(currentPlayer == 0 && currentIndex == 6) {
            additionalmoveearned = true;
        }else if(currentPlayer == 1 && currentIndex == 13){
            additionalmoveearned = true;
        }
        return additionalmoveearned;
    }

    public Move[] getPossibleMoves(int currentPlayer) {
        int startIndex = currentPlayer * 7;
        int moveCount = 0;
        Move[] moves = new Move[6];

        for (int i = startIndex; i < startIndex + 6; i++) {
            if (pits[i] > 0) {
                moves[moveCount++] = new Move(i);
            }
        }

        return Arrays.copyOf(moves, moveCount);
    }

    @Override
    public Board clone() {
        try {
            Board clonedBoard = (Board) super.clone();
            clonedBoard.pits = pits.clone();
            clonedBoard.stores = stores.clone();
            return clonedBoard;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Player 2's side: [");
        for (int i = 12; i > 7; i--) {
            builder.append(pits[i]).append(", ");
        }
        builder.append(pits[7]).append("]");
        builder.append("\n");
        builder.append("Player 2's store: ").append(stores[1]).append("\n");
        builder.append("\n");
        builder.append("Player 1's side: ").append(Arrays.toString(Arrays.copyOfRange(pits, 0, 6))).append("\n");
        builder.append("Player 1's store: ").append(stores[0]).append("\n");

        return builder.toString();
    }
}

