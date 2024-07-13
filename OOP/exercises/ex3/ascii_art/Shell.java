package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageFunctions;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;

/**
 * A class for the user interface in the ascii algoritem
 */
public class Shell {
    private class OutOfBoundariesException extends Exception {
        public OutOfBoundariesException(String msg) {
            super(msg);
        }
    }

    private class EmptyCharSetException extends Exception {
        public EmptyCharSetException(String msg) {
            super(msg);
        }
    }

    private static final String USER_INPUT = ">>> ";
    private static final char[] DEFAULT_CHAR_SET = {'0','1','2','3','4','5','6','7','8','9'};
    private static final String GENERAL_ERROR_MSG = "Did not execute due to incorrect command.";
    private static final String CHARS_ERROR_MSG = "Did not show chars due to incorrect command.";
    private static final String ADD_ERROR_MSG = "Did not add due to incorrect format.";
    private static final String REMOVE_ERROR_MSG = "Did not remove due to incorrect format.";
    private static final String RES_SUCCESS_MSG = "Resolution set to ";
    private static final String RES_ERROR_MSG = "Did not change resolution due to incorrect format.";
    private static final String RES_OUT_OF_BOUNDARIES_MSG =
            "Did not change resolution due to exceeding boundaries.";
    private static final String OUTPUT_ERROR_MSG = "Did not change output method due to incorrect format.";
    private static final String ASCII_EMPTY_CHAR_SET_ERROR_MSG = "Did not execute. Charset is empty.";
    private static final String IMAGE_READ_FILE_ERROR_MSG = "Did not execute due to problem with image file.";
    private static final String OUTPUT_HTML_FILE_NAME = "out.html";
    private static final String OUTPUT_FONT = "Courier New";
    private static final String DEFAULT_IMAGE_PATH = "./cat.jpeg";
    private static final String HTML = "html";
    private static final String CONSOLE = "console";
    private static final int DEFAULT_RES = 128;
    private static final int ARRAY_SIZE_2 = 2;
    private static final int ARRAY_SIZE_1 = 1;
    private static final int BY_TWO = 2;
    private static final String EXIT = "exit";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String RES = "res";
    private static final String CHARS = "chars";
    private static final String IMAGE = "image";
    private static final String OUTPUT = "output";
    private static final String ASCII_ART = "asciiArt";
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final int ADD_ONE_LETTER = 1;
    private static final int ADD_SPACE_LETTER = 5;
    private static final int ADD_AMOUNT_OF_LETTER = 3;
    private static final String SPACE = "space";
    private static final String ALL = "all";
    private static final int SECOND_LETTER = 2;
    private static final char ADD_SPLIT_LETTERS = '-';
    private static final char ASCII_START = 32;
    private static final char ASCII_END = 126;
    private SubImgCharMatcher charMatcher;
    private int resolution;
    private int minRes;
    private int maxRes;
    private ImageFunctions originImage;
    private AsciiOutput output;
    private AsciiArtAlgorithm asciiAlgo;

    /**
     * create new Shell object
     * @throws IOException can throw IO Exception if the image path not right
     */
    public Shell() throws IOException {
        this.charMatcher = new SubImgCharMatcher(DEFAULT_CHAR_SET);
        this.resolution = DEFAULT_RES;
        this.originImage = new ImageFunctions(new Image(DEFAULT_IMAGE_PATH),resolution);
        this.minRes = Math.max(1, originImage.getBufferWidth() / originImage.getBufferHeight());
        this.maxRes = originImage.getBufferWidth();
        this.output = new ConsoleAsciiOutput();
        this.asciiAlgo = new AsciiArtAlgorithm(originImage,charMatcher);
    }

    /**
     * run the user interface of the asciiArt algoritem
     */
    public void run() {
        while(true) {
            System.out.print(USER_INPUT);
            String[] userInput = KeyboardInput.readLine().split(" ");
            switch (userInput[0]) {
                case EXIT:
                    return;
                case CHARS:
                    try {
                        charsCmd(userInput);
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ADD:
                    try {
                        addCmd(userInput);
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case REMOVE:
                    try {
                        removeCmd(userInput);
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case RES:
                    try {
                        resCmd(userInput);
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    catch (OutOfBoundariesException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case IMAGE:
                    try {
                        imageCmd(userInput[1]);
                    }
                    catch (IOException e) {
                        System.out.println(IMAGE_READ_FILE_ERROR_MSG);
                    }
                    break;
                case OUTPUT:
                    try {
                        outputCmd(userInput);
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case ASCII_ART:
                    try {
                        asciiArtCmd(userInput);
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    catch (EmptyCharSetException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println(GENERAL_ERROR_MSG);
            }
        }
    }

    private void asciiArtCmd(String[] userInput) throws IOException,EmptyCharSetException {
        if(userInput.length != ARRAY_SIZE_1) {
            throw new IOException(GENERAL_ERROR_MSG);
        }
        if(charMatcher.isEmpty()){
            throw new EmptyCharSetException(ASCII_EMPTY_CHAR_SET_ERROR_MSG);
        }
        // run algo
        output.out(asciiAlgo.run());
    }

    private void outputCmd(String[] userInput) throws IOException{
        if(userInput.length != ARRAY_SIZE_2) {
            throw new IOException(OUTPUT_ERROR_MSG);
        }
        switch (userInput[1]) {
            case HTML:
                output = new HtmlAsciiOutput(OUTPUT_HTML_FILE_NAME, OUTPUT_FONT);
                break;
            case CONSOLE:
                output = new ConsoleAsciiOutput();
                break;
            default:
                throw new IOException(OUTPUT_ERROR_MSG);
        }
    }

    private void imageCmd(String newImagePath) throws IOException {
        // update image
        Image newImage = new Image(newImagePath);
        originImage.updateImage(newImage);
        // update res
        //originImage.updatedRes(originImage.getBufferWidth()/BY_TWO);
        //this.resolution = originImage.getBufferWidth()/BY_TWO;
        minRes = Math.max(1, originImage.getBufferWidth() / originImage.getBufferHeight());
        maxRes = originImage.getBufferWidth();
    }

    private void resCmd(String[] userInpt) throws IOException, OutOfBoundariesException{
        if(userInpt.length != ARRAY_SIZE_2) {
            throw new IOException(RES_ERROR_MSG);
        }
        switch (userInpt[1]){
            case UP:
                if(resolution*BY_TWO > maxRes) {
                    throw new OutOfBoundariesException(RES_OUT_OF_BOUNDARIES_MSG);
                }
                resolution*=BY_TWO;
                originImage.updatedRes(resolution);
                System.out.println(RES_SUCCESS_MSG+resolution+".");
                break;
            case DOWN:
                if(resolution/BY_TWO<minRes){
                    System.out.println(RES_OUT_OF_BOUNDARIES_MSG);
                    break;
                }
                resolution/=BY_TWO;
                originImage.updatedRes(resolution);
                System.out.println(RES_SUCCESS_MSG+resolution+".");
                break;
            default:
                throw new IOException(RES_ERROR_MSG);
        }
    }

    private void removeCmd(String[] userInput) throws IOException{
        if(userInput.length != ARRAY_SIZE_2) {
            throw new IOException(REMOVE_ERROR_MSG);
        }
        char[] charArray = userInput[1].toCharArray();
        switch (charArray.length) {
            case ADD_ONE_LETTER:
                charMatcher.removeChar(charArray[0]);
                break;
            case ADD_AMOUNT_OF_LETTER:
                if(charArray[1] == ADD_SPLIT_LETTERS ){
                    // check if the input is switched
                    if(charArray[0] > charArray[SECOND_LETTER]){
                        removeCharsToCharSet(charArray[SECOND_LETTER], charArray[0]);
                        break;
                    }
                    removeCharsToCharSet(charArray[0],charArray[SECOND_LETTER]);
                    break;
                }
                if(userInput[1].equals(ALL)) {
                    removeCharsToCharSet(ASCII_START , ASCII_END);
                    break;
                }
            case ADD_SPACE_LETTER:
                if(userInput[1].equals(SPACE)) {
                    charMatcher.removeChar(' ');
                    break;
                }
            default:
                throw new IOException(REMOVE_ERROR_MSG);
        }
    }

    private void removeCharsToCharSet(char start, char end) {
        for(char i=start; i<=end; i++){
            charMatcher.removeChar(i);
        }
    }
    private void addCmd(String[] userInput) throws IOException{
        if(userInput.length != ARRAY_SIZE_2) {
            throw new IOException(ADD_ERROR_MSG);
        }
        char[] charArray = userInput[1].toCharArray();
        switch (charArray.length) {
            case ADD_ONE_LETTER:
                charMatcher.addChar(charArray[0]);
                break;
            case ADD_AMOUNT_OF_LETTER:
                if(charArray[1] == ADD_SPLIT_LETTERS){
                    // check if the input is switched
                    if(charArray[0] > charArray[SECOND_LETTER]){
                        addCharsToCharSet(charArray[SECOND_LETTER], charArray[0]);
                        break;
                    }
                    addCharsToCharSet(charArray[0],charArray[SECOND_LETTER]);
                    break;
                }
                if(userInput[1].equals(ALL)) {
                    addCharsToCharSet(ASCII_START , ASCII_END);
                    break;
                }
            case ADD_SPACE_LETTER:
                if(userInput[1].equals(SPACE)) {
                    charMatcher.addChar(' ');
                    break;
                }
            default:
                throw new IOException(ADD_ERROR_MSG);
        }
    }
    private void addCharsToCharSet(char start, char end) {
        for(char i=start; i<=end; i++){
            charMatcher.addChar(i);
        }
    }

    private void charsCmd(String[] userInput) throws IOException{
        if(userInput.length != ARRAY_SIZE_1) {
            throw new IOException(CHARS_ERROR_MSG);
        }
        charMatcher.printCharSetSorted();
    }

    /**
     * the main func, initialise the program
     * @param args args from the terminal
     * @throws IOException Exception that the func can throw
     */
    public static void main(String[] args) throws IOException {
        Shell algo = new Shell();
        algo.run();
    }
}
