package ascii_art;

import image.ImageFunctions;
import image_char_matching.SubImgCharMatcher;

/**
 * A class that runs the ascii algoritem
 */
public class AsciiArtAlgorithm {
    private ImageFunctions image;
    private SubImgCharMatcher charMatcher;

    /**
     * create new AsciiArtAlgorithm object
     * @param image image to be change to ascii image
     * @param charMatcher set of char to paint the ascii image
     */
    public AsciiArtAlgorithm(ImageFunctions image, SubImgCharMatcher charMatcher) {
        this.image = image;
        this.charMatcher = charMatcher;
    }

    /**
     * runs the asciiArt algoritem
     * @return 2-D char Array represent the ascii image
     */
    public char[][] run() {
        double[][] brightnessArray = image.createBrightnessArray();
        char[][] asciiImage = new char[image.getHeight()][image.getWidth()];
        for(int i=0; i< image.getHeight();i++) {
            for(int j=0; j< image.getWidth(); j++) {
                asciiImage[i][j] = charMatcher.getCharByImageBrightness(brightnessArray[i][j]);
            }
        }
        return asciiImage;
    }

}
