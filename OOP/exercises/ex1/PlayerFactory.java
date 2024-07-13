public class PlayerFactory {
    static final String ERROR_MESSAGE = "Choose a player, and start again.\n" +
            "The players: [human, clever, whatever, genius]";
    public PlayerFactory(){}

    public Player buildPlayer(String type){
        type = type.toLowerCase();
        switch (type){
            case "human":{
                return new HumanPlayer();
            }
            case "whatever":{
                return new WhateverPlayer();
            }
            case "clever":{
                return new CleverPlayer();
            }
            case "genius":{
                return new GeniusPlayer();
            }
            default:{
                System.out.println(ERROR_MESSAGE);
                return null;
            }
        }
    }
}
