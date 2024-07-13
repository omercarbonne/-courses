public class Board {
    private Mark[][] board;
    private int size;
   // private int emptyCells;

    public Board(){
        // Default constructor
        this.size = 4;
        board = createEmptyBoard(size);
        //emptyCells = size*size;
    }

    public  Board(int size){
        // constructor
        this.size = size;
        board = createEmptyBoard(size);
        //emptyCells = size*size;
    }

    private Mark[][] createEmptyBoard(int size){
        //fills an empty board with Mark.BLANK
        Mark[][] board = new Mark[size][size];
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                board[row][col] = Mark.BLANK;
            }
        }
        return board;
    }

    public int getSize(){
        return size;
    }

    private boolean insideBoard(int row, int col){
        // checks if location is inside the board
        if((row<size)&&(row>=0)&&(col<size)&&(col>=0)){
            return true;
        }
        return false;
    }
    boolean putMark(Mark mark, int row, int col){
        // checks if location is valid
        if(insideBoard(row, col)){
            // checks if location is empty
            if(board[row][col] == Mark.BLANK){
                board[row][col] = mark;
                return true;
            }
        }
        return false;
    }

    Mark getMark(int row, int col){
        if(insideBoard(row, col)){
            return board[row][col];
        }
        return Mark.BLANK;
    }






}
