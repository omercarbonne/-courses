public class Game {
    static final int DEFAULT_SIZE = 4;
    static final int DEFAULT_WIN_STREAK = 3;
    private Board board;
    private int numOfEmptyCells;
    private int boardSize;
    private Player playerX;
    private Player playerO;
    private Renderer renderer;
    private int winStreak;

    public Game(Player playerX,Player playerO, Renderer renderer){
        this.board = new Board();
        this.boardSize = board.getSize();
        this.numOfEmptyCells = boardSize*boardSize;
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        this.winStreak = DEFAULT_WIN_STREAK;
    }

    public Game(Player playerX,Player playerO, int size, int winStreak,Renderer renderer){
        this.board = new Board(size);
        this.boardSize = size;
        this.numOfEmptyCells = size*size;
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        this.winStreak = winStreak;
    }

    public int getWinStreak(){
        return winStreak;
    }

    public int getBoardSize(){
        return board.getSize();
    }

    public  Mark run(){
        renderer.renderBoard(board);
        while (numOfEmptyCells > 0){
            Mark winner = null;
            playerX.playTurn(board, Mark.X);
            numOfEmptyCells--;
            renderer.renderBoard(board);
            winner = checkIfPlayerWon();
            if(winner != null){ // if someone won
                return winner;
            }
            if(numOfEmptyCells == 0) {break;}

            playerO.playTurn(board, Mark.O);
            numOfEmptyCells--;
            renderer.renderBoard(board);
            winner = checkIfPlayerWon();
            if(winner != null){ // if someone won
                return winner;
            }
        }
        return Mark.BLANK;
    }


    private Mark checkIfPlayerWon(){
        Mark winner = checkRows();
        if(winner != Mark.BLANK){
            return winner;
        }
        winner = checkColumns();
        if(winner != Mark.BLANK) {
            return winner;
        }
        winner = checkDiagonals();
        if(winner != Mark.BLANK) {
            return winner;
        }
        return null;
    }

    private Mark checkRows(){
        int countX = 0;
        int countO = 0;
        for(int row = 0; row < boardSize; row++){
            countO = 0;
            countX = 0;
            for(int col = 0; col < boardSize; col++){
                switch (board.getMark(row,col)){
                    case X:
                        countX++;
                        if(countX == winStreak){
                            return Mark.X;
                        }
                        break;
                    case O:
                        countO++;
                        if(countO == winStreak) {
                            return Mark.O;
                        }
                        break;
                }
            }
        }
        return Mark.BLANK;
    }

    private Mark checkColumns(){
        int countX = 0;
        int countO = 0;
        for(int col = 0; col < boardSize; col++){
            countO = 0;
            countX = 0;
            for(int row = 0; row < boardSize; row++){
                switch (board.getMark(row,col)){
                    case X:
                        countX++;
                        if(countX == winStreak){
                            return Mark.X;
                        }
                        break;
                    case O:
                        countO++;
                        if(countO == winStreak){
                            return Mark.O;
                        }
                        break;
                }
            }
        }
        return Mark.BLANK;

    }

    private Mark checkDiagonals(){
        Mark winner = checkDiagonalsLTR();
        if(winner != Mark.BLANK){
            return winner;
        }
        return checkDiagonalsRTL();
    }

    private Mark checkDiagonalsLTR(){
        Mark winner = Mark.BLANK;
        for(int row = 0; row < boardSize; row++){
            winner = checkOneDiagonalLTR(row, 0);
            if(winner != Mark.BLANK){
                return winner;
            }
        }
        for(int col = 0; col < boardSize; col++){
            winner = checkOneDiagonalLTR(0, col);
            if(winner != Mark.BLANK){
                return winner;
            }
        }
        return winner;

    }

    private Mark checkOneDiagonalLTR(int startRow, int startCol){
        int countX = 0;
        int countO = 0;
        while((startRow < boardSize)&&(startCol<boardSize)){
            Mark mark = board.getMark(startRow, startCol);
            if(mark == Mark.X){
                countX++;
                if(countX == winStreak){return Mark.X;}
            }
            if(mark == Mark.O){
                countO++;
                if(countO == winStreak){return Mark.O;}
            }
            startRow++;
            startCol++;
        }
        return Mark.BLANK;
    }

    private Mark checkDiagonalsRTL(){
        Mark winner = Mark.BLANK;
        for(int row = 0; row < boardSize; row++){
            winner = checkOneDiagonalRTL(row, boardSize-1);
            if(winner != Mark.BLANK){
                return winner;
            }
        }
        for(int col = 0; col < boardSize; col++){
            winner = checkOneDiagonalRTL(0, col);
            if(winner != Mark.BLANK){
                return winner;
            }
        }
        return winner;

    }

    private Mark checkOneDiagonalRTL(int startRow, int startCol){
        int countX = 0;
        int countO = 0;
        while((startRow < boardSize)&&(startCol>=0)){
            Mark mark = board.getMark(startRow, startCol);
            if(mark == Mark.X){
                countX++;
                if(countX == winStreak){return Mark.X;}
            }
            if(mark == Mark.O){
                countO++;
                if(countO == winStreak){return Mark.O;}
            }
            startRow++;
            startCol--;
        }
        return Mark.BLANK;

    }




}
