
public class HumanPlayer implements Player {
    static final String REQUEST_COORDS = "Enter coordinates (YX): ";
    static final String OUTSIDE_BOARD_ERROR = "Invalid mark position, please choose" +
            " a different position.\n" +
            "Invalid coordinates, type again: ";
    static final String OCCUPIED_POSITION_ERROR = "Mark position is already occupied.\n" +
            "Invalid coordinates, type again: ";

    public HumanPlayer(){

    }

    private boolean insideBoard(Board board, int row, int col){
        int size = board.getSize();
        if((row<size)&&(row>=0)&&(col<size)&&(col>=0)){
            return true;
        }
        return false;
    }
    private int[] getCoordsFromUser(Board board){
        System.out.print(REQUEST_COORDS);
        while (true) {
            int row = KeyboardInput.readInt();
            int col = KeyboardInput.readInt();
            if(insideBoard(board, row, col)){
                if(board.getMark(row, col)==Mark.BLANK){
                    return new int[]{row, col};
                }
                else{
                    System.out.println(OCCUPIED_POSITION_ERROR);
                }
            }
            else{
                System.out.println(OUTSIDE_BOARD_ERROR);
            }
        }


    }

    public void playTurn(Board board, Mark mark){
        int[] coords = getCoordsFromUser(board);
        board.putMark(mark, coords[0], coords[1]);
    }
}


