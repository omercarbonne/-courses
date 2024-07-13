public class Tournament {
    static final String RESULTS_MESSAGE = "######### Results #########\n"+
      "Player 1, %s won: %d rounds\n"+
        "Player 2, %s won: %d rounds\n"+
            "Ties: %d";
    private Player[] players;
    private int roundsLeft;
    private Renderer renderer;
    private int[] results;
    private int turn;

    public Tournament(int rounds, Renderer renderer, Player player1, Player player2){
        this.roundsLeft = rounds;
        this.renderer = renderer;
        this.players = new Player[] {player1, player2};
        this.results = new int[] {0,0,0};
        this.turn = 0;
    }

    public void playTournament(int size, int winStreak, String playerName1, String playerName2){


        while(roundsLeft > 0){
            Game game = new Game(players[turn], players[(turn+1)%2], size, winStreak, renderer);
            Mark res = game.run();
            switch (res){
                case BLANK: {
                    results[2]++;
                    break;
                }
                case X:{
                    results[turn]++;
                    break;
                }
                case O:{
                    results[(turn+1)%2]++;
                    break;
                }
            }
            turn = (turn+1)%2;
            roundsLeft--;
        }
        System.out.println(String.format(RESULTS_MESSAGE, playerName1, results[0],
                playerName2, results[1], results[2]));


    }

    public static void main(String[] args){
        PlayerFactory playerFactory = new PlayerFactory();
        String namePlayer1 =  args[4];
        Player player1 = playerFactory.buildPlayer(namePlayer1);
        if(player1 == null){return;}
        String namePlayer2 = args[5];
        Player player2 = playerFactory.buildPlayer(namePlayer2);
        if(player2 == null){return;}
        RendererFactory rendererFactory = new RendererFactory();
        int size = Integer.parseInt(args[1]);
        Renderer renderer = rendererFactory.buildRenderer(args[3],size);
        if(renderer == null){return;}
        int rounds = Integer.parseInt(args[0]);
        int winStreak = Integer.parseInt(args[2]);

        Tournament tournament = new Tournament(rounds, renderer, player1, player2);
        tournament.playTournament(size, winStreak, namePlayer1, namePlayer2);
    }



}
