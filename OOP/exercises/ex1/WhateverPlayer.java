import java.util.Random;
public class WhateverPlayer implements Player{
    Random random = new Random();
    public void playTurn(Board board, Mark mark){
        int randnum = random.nextInt(getNumOfEmptyCells(board));
        int[][] arrOfEmptyCells = getListOfEmptyCells(board);
        int row = arrOfEmptyCells[randnum][0];
        int col = arrOfEmptyCells[randnum][1];
        board.putMark(mark, row,col);
    }

    private int[][] getListOfEmptyCells(Board board){
        int size = board.getSize();
        int len = getNumOfEmptyCells(board);
        int[][] arrOfEmptyCells = new int[len][2];
        int location = 0;
        for(int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++){
                if(board.getMark(row, col) == Mark.BLANK){
                    arrOfEmptyCells[location][0]=row;
                    arrOfEmptyCells[location][1]=col;
                    location++;
                }
            }
        }
        return arrOfEmptyCells;

    }

    private int getNumOfEmptyCells(Board board){
        int size = board.getSize();
        int count = 0;
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                if(board.getMark(row, col) == Mark.BLANK){
                    count++;
                }
            }

        }
        return count;
    }

}
