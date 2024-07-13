import java.util.Random;
public class CleverPlayer implements Player{

    private GeniusPlayer geniusPlayer = new GeniusPlayer();
    private WhateverPlayer whateverPlayer = new WhateverPlayer();
    private Random random = new Random();

    public CleverPlayer(){}

    public void playTurn(Board board, Mark mark){
        if(random.nextBoolean()){
            geniusPlayer.playTurn(board, mark);
        }
        else{
            whateverPlayer.playTurn(board,mark);
        }
    }

}
