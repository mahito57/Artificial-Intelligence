package N_puzzle;

import java.util.*;


public class Main {

    public static void main(String[] args) {
	// write your code here
        Scanner sc = new Scanner(System.in);
        System.out.println("Give dimension: ");
        int dimension = sc.nextInt();
        int boardsize = dimension*dimension;

        System.out.println("Give the numbers between 0 to "+(boardsize-1));
        int[][] initialBoard = BoardInputs(dimension);
        //BoardPrinter(initialBoard);

        //Check if board is solvable
        boolean isSolvable;
        if(dimension%2==0) {
            isSolvable = isSolvableEven(initialBoard);
        }else isSolvable = isSolvableOdd(initialBoard);

        if(isSolvable) System.out.println("Board is solvable");
        else System.out.println("Unsolvable Puzzle");

        //Solve the board
        PriorityQueue<Node> pq = new PriorityQueue<>();
        ArrayList<int[][]> closedList = new ArrayList<>();
        System.out.println("press 1 for manhattan, press 2 for hamming");
        int choice = sc.nextInt();
        if(choice == 1){
            Node initial = new Node(initialBoard,0,manhattanDistance(initialBoard));
            pq.add(initial);
        } else{
            Node initial = new Node(initialBoard,0,hammingDistance(initialBoard));
            pq.add(initial);
        }


        //Creating Goal Array
        int[][] goalArray = new int[dimension][dimension];
        int k = 1;
        for(int i = 0; i<dimension;i++){
            for(int j = 0; j< dimension; j++) {
                goalArray[i][j] = k++;
                if(i== dimension-1 && j==dimension-1) {
                    goalArray[i][j] = 0;
                }
            }
        }
        int minmove = -1;
        int expandednode = 0;
        if(isSolvable){
            while (!pq.isEmpty()) {
                Node currentNode = pq.poll();
                BoardPrinter(currentNode.board);
                if(equalitychecker(currentNode.board,goalArray)){
                    minmove=currentNode.gn;
                    break;
                }
                closedList.add(currentNode.board);
                System.out.println();
                for(Node e:addToQueue(closedList,currentNode,choice)){
                    pq.add(e);
                    expandednode++;
                }
            }
            System.out.println("Minimum number of moves: "+minmove);
            System.out.println("Total expanded node "+expandednode);
        }

    }

    private static PriorityQueue<Node> addToQueue(ArrayList<int[][]> closedList, Node curNode,int choice) {
        boolean match = false;
        PriorityQueue<Node> tempqueue = new PriorityQueue<>();

        Node temp = moveUp(curNode,choice);
        for(int i=0;i<closedList.size();i++){
            if(equalitychecker(temp.board,closedList.get(i))){
                match=true;
                break;
            }
        }
        if(match == false) tempqueue.add(temp);
        match = false;

        temp = moveDown(curNode,choice);
        for(int i=0;i<closedList.size();i++){
            if(equalitychecker(temp.board,closedList.get(i))){
                match=true;
                break;
            }
        }
        if(match == false) tempqueue.add(temp);
        match = false;

        temp = moveRight(curNode,choice);
        for(int i=0;i<closedList.size();i++){
            if(equalitychecker(temp.board,closedList.get(i))){
                match=true;
                break;
            }
        }
        if(match == false) tempqueue.add(temp);
        match = false;

        temp = moveLeft(curNode,choice);
        for(int i=0;i<closedList.size();i++){
            if(equalitychecker(temp.board,closedList.get(i))){
                match=true;
                break;
            }
        }
        if(match == false) tempqueue.add(temp);

        return tempqueue;
    }

    public static int[][] BoardInputs(int dimension){
        int boardsize = dimension*dimension;
        int[][] initialBoard = new int[dimension][dimension];
        for (int row = 0; row < initialBoard.length; row++) {
            for (int col = 0; col < initialBoard[row].length; col++) {
                initialBoard[row][col] = -1;
            }
        }
        Scanner sc = new Scanner(System.in);
        int tempsize = boardsize;
        int curRow = 0;
        int curCol = 0;
        while (tempsize>0){
            boolean bool = true;
            int x = sc.nextInt();
            if(x>=0 && x<boardsize){
                for (int row = 0; row < initialBoard.length; row++) {
                    for (int col = 0; col < initialBoard[row].length; col++) {
                        if(x==initialBoard[row][col]){
                            bool = false;
                            System.out.println("Dont give the same number");
                        }
                    }
                }
                if(bool){
                    initialBoard[curRow][curCol] = x;
                    curCol++;
                    if(curCol==dimension){
                        curCol = 0;
                        curRow++;
                    }
                    tempsize--;
                }
            }else System.out.println("Number is out of range");
        }
        return initialBoard;
    }

    public static void BoardPrinter(int[][] board){
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                System.out.print(board[row][col]+" ");
            }
            System.out.println();
        }
    }

    static int getInvCount(int[] arr) {
        int inv_count = 0;
        for (int i = 0; i < arr.length; i++)
            for (int j = i + 1; j < arr.length; j++)

                // Value 0 is used for empty space
                if (arr[i] > 0 &&
                        arr[j] > 0 && arr[i] > arr[j])
                    inv_count++;
        return inv_count;
    }

    static boolean isSolvableOdd(int[][] board) {
        int linearboard[] = covertTolinearBoard(board);

        // Count inversions in given 8 puzzle
        int invCount = getInvCount(linearboard);

        // return true if inversion count is even.
        return (invCount % 2 == 0);
    }

    static int findPositionOfSpace(int[][] board) {
        int n= board.length;
        for(int i = n-1;i>=0;i--){
            for(int j = n-1;j>=0; j--){
                if(board[i][j] == 0)
                    return n-i;
            }
        } return -1;
    }

    static boolean isSolvableEven(int[][] board) {
        int positionOfSpace = findPositionOfSpace(board);
        int[] linearboard = covertTolinearBoard(board);
        int invCount = getInvCount(linearboard);
        if(positionOfSpace%2 == 0) {
            if(invCount%2 == 0) return false;
            else return true;
        }else {
            if(invCount%2 == 0) return true;
            else return false;
        }
    }

    static int[] covertTolinearBoard(int[][] board) {
        int linearboard[];
        int length = board.length;
        linearboard = new int[length*length];
        int k = 0;

        // Converting 2-D puzzle to linear form
        for(int i=0; i<length; i++)
            for(int j=0; j<length; j++)
                linearboard[k++] = board[i][j];

        return linearboard;
    }

    static int hammingDistance(int[][] board) {
        int count = 0;
        int[] linearBoard = covertTolinearBoard(board);
        for(int i = 0;i<linearBoard.length;i++) {
            int x = linearBoard[i];
            if(x>0 && x!=i+1) count++;
        }
        return count;
    }

    static int manhattanDistance(int[][] board) {
        int goalrow,goalcol,curDist;
        int totalDist = 0;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                int x = board[row][col];
                if(x>0) {
                    x=x-1;
                    goalcol = x%3;
                    goalrow = x/3;
                    curDist = Math.abs(goalcol-col) +Math.abs(goalrow-row);
                    totalDist+=curDist;
                }
            }
        }
        return totalDist;
    }

    static Node moveUp(Node temp,int choice) {
        int [][]arr = new int[temp.board.length][temp.board.length];
        Node node = new Node(arr,0,0);
        for(int i = 0; i< temp.board.length;i++){
            for (int j = 0; j <temp.board.length;j++){
               node.board[i][j] = temp.board[i][j];
                }
            }
        node.hn = temp.hn;
        node.gn = temp.gn;
        int RowofSpace = 0,ColofSpace = 0;
        int[][] curBoard = node.board;
        for(int i = 0; i< curBoard.length;i++){
            for (int j = 0; j <curBoard.length;j++){
                if(curBoard[i][j] == 0){
                    RowofSpace = i;
                    ColofSpace = j;
                }
            }
        }
        if(RowofSpace != 0) {
            curBoard[RowofSpace][ColofSpace] = curBoard[RowofSpace-1][ColofSpace];
            curBoard[RowofSpace-1][ColofSpace] = 0;
        }
        if(choice == 1){
            Node upNode = new Node(curBoard,node.gn+1,manhattanDistance(curBoard));
            return upNode;
        } else {
            Node upNode = new Node(curBoard,node.gn+1,hammingDistance(curBoard));
            return upNode;
        }
    }
    static Node moveDown(Node temp,int choice) {
        int [][]arr = new int[temp.board.length][temp.board.length];
        Node node = new Node(arr,0,0);
        for(int i = 0; i< temp.board.length;i++){
            for (int j = 0; j <temp.board.length;j++){
                node.board[i][j] = temp.board[i][j];
            }
        }
        node.hn = temp.hn;
        node.gn = temp.gn;
        int RowofSpace = 0,ColofSpace = 0;
        int[][] curBoard = node.board;
        for(int i = 0; i< curBoard.length;i++){
            for (int j = 0; j <curBoard.length;j++){
                if(curBoard[i][j] == 0){
                    RowofSpace = i;
                    ColofSpace = j;
                }
            }
        }
        if(RowofSpace != curBoard.length-1) {
            curBoard[RowofSpace][ColofSpace] = curBoard[RowofSpace+1][ColofSpace];
            curBoard[RowofSpace+1][ColofSpace] = 0;
        }
        if(choice == 1){
            Node downNode = new Node(curBoard,node.gn+1,manhattanDistance(curBoard));
            return downNode;
        } else {
            Node downNode = new Node(curBoard,node.gn+1,hammingDistance(curBoard));
            return downNode;
        }
    }
    static Node moveLeft(Node temp,int choice) {
        int [][]arr = new int[temp.board.length][temp.board.length];
        Node node = new Node(arr,0,0);
        for(int i = 0; i< temp.board.length;i++){
            for (int j = 0; j <temp.board.length;j++){
                node.board[i][j] = temp.board[i][j];
            }
        }
        node.hn = temp.hn;
        node.gn = temp.gn;
        int RowofSpace = 0,ColofSpace = 0;
        int[][] curBoard = node.board;
        for(int i = 0; i< curBoard.length;i++){
            for (int j = 0; j <curBoard.length;j++){
                if(curBoard[i][j] == 0){
                    RowofSpace = i;
                    ColofSpace = j;
                }
            }
        }
        if(ColofSpace != 0) {
            curBoard[RowofSpace][ColofSpace] = curBoard[RowofSpace][ColofSpace-1];
            curBoard[RowofSpace][ColofSpace-1] = 0;
        }
        if(choice == 1){
            Node leftNode = new Node(curBoard,node.gn+1,manhattanDistance(curBoard));
            return leftNode;
        } else {
            Node leftNode = new Node(curBoard,node.gn+1,hammingDistance(curBoard));
            return leftNode;
        }
    }
    static Node moveRight(Node temp,int choice) {
        int [][]arr = new int[temp.board.length][temp.board.length];
        Node node = new Node(arr,0,0);
        for(int i = 0; i< temp.board.length;i++){
            for (int j = 0; j <temp.board.length;j++){
                node.board[i][j] = temp.board[i][j];
            }
        }
        node.hn = temp.hn;
        node.gn = temp.gn;
        int RowofSpace = 0,ColofSpace = 0;
        int[][] curBoard = node.board;
        for(int i = 0; i< curBoard.length;i++){
            for (int j = 0; j <curBoard.length;j++){
                if(curBoard[i][j] == 0){
                    RowofSpace = i;
                    ColofSpace = j;
                }
            }
        }
        if(ColofSpace != curBoard.length-1) {
            curBoard[RowofSpace][ColofSpace] = curBoard[RowofSpace][ColofSpace+1];
            curBoard[RowofSpace][ColofSpace+1] = 0;
        }
        if(choice == 1){
            Node rightNode = new Node(curBoard,node.gn+1,manhattanDistance(curBoard));
            return rightNode;
        } else {
            Node rightNode = new Node(curBoard,node.gn+1,hammingDistance(curBoard));
            return rightNode;
        }
    }

    static boolean equalitychecker(int[][] a,int[][] b){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a.length;j++){
                if(a[i][j]!=b[i][j]) return false;
            }
        }
        return true;
    }

}

class Node implements Comparable<Node>{
    int[][] board;
    int gn,hn,fn;

    public Node(int[][] board,int gn,int hn) {
        this.board = board;
        this.gn = gn;
        this.hn = hn;
        this.fn = gn+hn;
    }

    public Node(Node node) {
        for(int i = 0; i<node.board.length;i++){
            for(int j = 0; j< node.board.length; j++) {
               this.board[i][j] = node.board[i][j];
            }
        }
        this.hn=node.hn;
        this.gn=node.gn;
        this.fn=node.fn;
    }

    @Override
    public int compareTo(Node other) {
        return this.fn- other.fn;
    }
}

