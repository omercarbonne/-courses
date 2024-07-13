package image_char_matching;
import java.util.*;

/**
 * A class to handle the converting grey pixel to chars according to the ascii algoritem
 */
public class SubImgCharMatcher {
    private TreeMap<Double, Character> charMap = new TreeMap<Double, Character>();
    private HashSet<Character> charSet = new HashSet<Character>();
    private boolean isUpdated;
    private double maxBrightness;
    private double minBrightness;

    /**
     * Create new SubImgCharMatcher object
     * @param charset array of chars that will be using in the ascii algoritem
     */
    public SubImgCharMatcher(char[] charset) {
        // create the charMap
        HashMap<Character, Double> partialBrightness = new HashMap<Character, Double>();
        this.maxBrightness = 0;
        this.minBrightness = 1;
        for(char c: charset) {
            this.charSet.add(c);
        }
        calculatePartialBrightness(this.charSet, partialBrightness);
        calculateFinalBrightness(this.charSet, partialBrightness);
        this.isUpdated = true;
    }

    /**
     * gets brightness level between 0-1 and return the nearest char according to his brightness level.
     * @param brightness number between 0-1 that represents the brightness of a semi-image
     * @return nearest brightness value in the object
     */
    public char getCharByImageBrightness(double brightness) {
        if(!isUpdated) {
            update();
        }
        Double lower = charMap.floorKey(brightness);
        Double upper = charMap.higherKey(brightness);
        // check if null
        if(lower == null) {
            return charMap.get(upper);
        }
        if(upper == null) {
            return charMap.get(lower);
        }
        // check who's the closest
        if(Math.abs(brightness-upper) > Math.abs(brightness-lower)) {
            return charMap.get(lower);
        }
        return charMap.get(upper);
    }

    /**
     * add new char to the map
     * @param c new char to be added
     */
    public void addChar(char c) {
        if(charSet.contains(c)) {
           return;
        }
        charSet.add(c);
        isUpdated = false;
    }

    /**
     * remove a char from the map
     * @param c char to be removed
     */
    public void removeChar(char c) {
        if(!charSet.contains(c)) {
            return;
        }
        charSet.remove(c);
        double valueToDelete = -1;
        for(Map.Entry<Double,Character> entry: charMap.entrySet()) {
            if(entry.getValue() == c) {
                valueToDelete = entry.getKey();
            }
        }
        if(valueToDelete != -1) {
            charMap.remove(valueToDelete);
        }
        isUpdated = false;
    }

    private void update() {
        HashMap<Character, Double> partialBrightness = new HashMap<Character, Double>();
        maxBrightness = 0;
        minBrightness = 1;
        calculatePartialBrightness(charSet, partialBrightness);
        calculateFinalBrightness(charSet, partialBrightness);
        isUpdated = true;
    }

    private void calculateFinalBrightness(HashSet<Character> charset,
                                          HashMap<Character,Double> partialBrightness) {
        for(char c : charset) {
            double charPartialBrightness = partialBrightness.get(c);
            double newValue = (charPartialBrightness-minBrightness) / (maxBrightness- minBrightness);
            // check if there is a char with the value in the map
            if(charMap.containsKey(newValue)) {
                // check if the new char is with lower ASCII
                if(c > charMap.get(newValue)) {
                    continue;
                }
            }
            charMap.put(newValue, c);
        }
    }

    private void calculatePartialBrightness(HashSet<Character> charset,
                                            HashMap<Character,Double> partialBrightness) {
        for(char c: charset) {
            double charBrightness = getPartialBrightness(c);
            partialBrightness.put(c,charBrightness);
            // check to update max
            if(charBrightness > maxBrightness) {
                maxBrightness = charBrightness;
            }
            // check to update min
            if(charBrightness < minBrightness) {
                minBrightness = charBrightness;
            }
        }
    }

    private double getPartialBrightness(char c) {
        boolean[][] charBooleanArray = CharConverter.convertToBoolArray(c);
        int count = 0;
        for(int i=0; i<16; i++) {
            for(int j=0; j<16; j++) {
                if(charBooleanArray[i][j]){
                    count++;
                }
            }
        }
        return (double) count /
                (CharConverter.DEFAULT_PIXEL_RESOLUTION*CharConverter.DEFAULT_PIXEL_RESOLUTION);
    }

    /**
     * prints all the chars in the object by order (from small to large)
     */
    public void printCharSetSorted() {
        if(charSet.isEmpty()) {
            System.out.println();
            return;
        }
        // sort the set
        ArrayList<Character> sortedCharList = new ArrayList<Character>(charSet);
        Collections.sort(sortedCharList);
        String toPrint = "";
        for(char c : sortedCharList){
            toPrint += c+" ";
        }
        // print sorted set
        System.out.println(toPrint.substring(0,toPrint.length()-1));
    }

    /**
     * checks if the object has no chars
     * @return true if empty, false other
     */
    public boolean isEmpty(){
        return charSet.isEmpty();
    }
}
