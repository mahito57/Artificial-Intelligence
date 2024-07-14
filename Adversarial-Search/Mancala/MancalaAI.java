package Mancala;

class MancalaAI {
    private static final int MAX_DEPTH = 6;
    int n;
    int AdditionalMoveEarned;
    boolean additionalMove;
    int stonesCaptured;
    public MancalaAI (int n){
        this.n = n;
    }

    public int chooseMove(Board board) {
        int currentPlayer = board.getCurrentPlayer();
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        Move[] possibleMoves = board.getPossibleMoves(currentPlayer);
        for (Move move : possibleMoves) {
            AdditionalMoveEarned = 0;
            stonesCaptured = 0;
            Board clonedBoard = board.clone(); // Create a clone of the board for simulation
            additionalMove = clonedBoard.makeMove(move.getPitIndex());
            if(additionalMove) AdditionalMoveEarned++;
            stonesCaptured += clonedBoard.getStonesCaptured();

            int score = minimax(clonedBoard, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE,
                    board.getOpponentPlayer(currentPlayer), currentPlayer);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move.getPitIndex();
            }
        }

        return bestMove;
    }

    private int minimax(Board board, int depth, int alpha, int beta, int currentPlayer, int maximizingPlayer) {
        if (depth == 0 || board.isGameOver()) {
            if(n==1) return board.evaluate1(currentPlayer);
            else if(n==2) return board.evaluate2(currentPlayer);
            else if(n==3) return board.evaluate3(currentPlayer, AdditionalMoveEarned);
            else return board.evaluate4(currentPlayer, AdditionalMoveEarned, stonesCaptured);
        }

        if (currentPlayer == maximizingPlayer) {
            int maxScore = Integer.MIN_VALUE;
            Move[] possibleMoves = board.getPossibleMoves(currentPlayer);
            for (Move move : possibleMoves) {
                Board clonedBoard = board.clone();
                additionalMove = clonedBoard.makeMove(move.getPitIndex());
                if(additionalMove) AdditionalMoveEarned++;
                stonesCaptured += clonedBoard.getStonesCaptured();

                int score = minimax(clonedBoard, depth - 1, alpha, beta, board.getOpponentPlayer(currentPlayer), maximizingPlayer);
                maxScore = Math.max(maxScore, score);
                alpha = Math.max(alpha, score);
                if (beta <= alpha) {
                    break; // Beta cutoff
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            Move[] possibleMoves = board.getPossibleMoves(currentPlayer);
            for (Move move : possibleMoves) {
                Board clonedBoard = board.clone();
                additionalMove = clonedBoard.makeMove(move.getPitIndex());
                if(additionalMove) AdditionalMoveEarned--;
                stonesCaptured -= clonedBoard.getStonesCaptured();

                int score = minimax(clonedBoard, depth - 1, alpha, beta, board.getOpponentPlayer(currentPlayer), maximizingPlayer);
                minScore = Math.min(minScore, score);
                beta = Math.min(beta, score);
                if (beta <= alpha) {
                    break; // Alpha cutoff
                }
            }
            return minScore;
        }
    }
}
