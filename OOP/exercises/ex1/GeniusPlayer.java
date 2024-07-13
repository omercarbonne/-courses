public class GeniusPlayer implements Player{

    private WhateverPlayer whateverPlayer;
    public GeniusPlayer(){
        whateverPlayer = new WhateverPlayer();
    }

    @Override
    public void playTurn(Board board, Mark mark) {
        for(int row = 0; row < board.getSize(); row++){
            for(int col = 0; col < board.getSize(); col++){
                if(board.getMark(row, col)==Mark.BLANK) { // if an empty cell
                    if (hasNeighbor(board, row, col, mark)) { // if has similar neighbor
                        board.putMark(mark, row, col);
                        return;
                    }
                }

            }
        }
        //if couldnt find good spot, put random using whatever player
        whateverPlayer.playTurn(board, mark);
    }

    private boolean hasNeighbor(Board board, int row, int col, Mark mark){
        for(int i = row-1; i < row + 2; i++){
            for(int j = col-1; j < col+2; j++){
                if(board.getMark(i,j)==mark){
                    return true;
                }
            }
        }
        return false;
    }



}
