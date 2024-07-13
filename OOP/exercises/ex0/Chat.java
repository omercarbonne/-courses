import java.util.Scanner;

class Chat{

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        ChatterBot[] bots  = new ChatterBot[2];
        String[] legal = {"To say "+ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE+
                "? okay: <phrase> ", "Here:" + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE + " ",
        "MMMM MMMM: " + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE};
        String[] ilegal = {"say what? " + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST + "? what’s "
                + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST + "? ", "What? ",
        "Did you mean " + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST + "? "};


        bots[0] = new ChatterBot("Omer", legal, ilegal);
        bots[1] = new ChatterBot("Yuval",legal, ilegal);

        String statement = "";

        while (true){
            for(ChatterBot bot: bots){
                statement = bot.replyTo(statement);
                System.out.println(bot.getName() + ": " + statement);
                scanner.nextLine();
            }
        }

    }

}