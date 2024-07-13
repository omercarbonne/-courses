package image;
import java.awt.*;

/**
 * A class to handle the image functions in the ascii algoritem
 */
public class ImageFunctions {
    private static final int WHITE_COLOR = 255;
    private static final int BY_TWO = 2;
    private static final double ALGO_RED = 0.2126;
    private static final double ALGO_GREEN = 0.7152;
    private static final double ALGO_BLUE = 0.0722;

    /**
     * static func that gets an image and return a buffed image
     * @param image image to be buffed
     * @return new buffer image
     */
    public static Image buffer(Image image) {
        // calculate new size
        int newWidth = getClosestPowerOfTwo(image.getWidth());
        int newHeight = getClosestPowerOfTwo(image.getHeight());
        // check if the image doesn't need buufer
        if((newWidth == image.getWidth()) && (newHeight == image.getHeight())) {
            return image;
        }
        // create the new pixels array
        int bufferWidth = (newWidth - image.getWidth()) / BY_TWO;
        int bufferHeight = (newHeight - image.getHeight()) / BY_TWO;
        Color[][] newImageColor = new Color[newHeight][newWidth];
        // first blanks lines
        for(int i=0; i<bufferHeight; i++) {
            for(int j=0; j<newWidth; j++) {
                newImageColor[i][j] = new Color(WHITE_COLOR,WHITE_COLOR,WHITE_COLOR);
            }
        }
        // image lines - buffer only in the sides
        for(int i=bufferHeight; i<image.getHeight()+bufferHeight; i++) {
            for(int j=0; j<newWidth; j++) {
                if((j < bufferWidth) || (j >= bufferWidth+image.getWidth())) {
                    newImageColor[i][j] = new Color(WHITE_COLOR,WHITE_COLOR,WHITE_COLOR);
                    continue;
                }
                newImageColor[i][j] = image.getPixel( i-bufferHeight, j-bufferWidth);
            }
        }
        // last blanks lines
        for(int i=image.getHeight()+bufferHeight; i<newHeight; i++) {
            for(int j=0; j<newWidth; j++) {
                newImageColor[i][j] = new Color(WHITE_COLOR,WHITE_COLOR,WHITE_COLOR);
            }
        }
        // create buffer image
        return new Image(newImageColor, newWidth, newHeight);
    }

    private static int getClosestPowerOfTwo(int num) {
        int res = 1;
        while(res < num) {
            res*=BY_TWO;
        }
        return res;
    }

    /**
     * static func that gets and image and new width
     * and split the image to newWidth semi-images.
     * @param image image to be change
     * @param newWidth number of semi-images (the new resolution)
     * @return image 2-D array with all the new semi-images
     */
    public static Image[][] split(Image image, int newWidth) {
        int semiImageSize = image.getWidth() / newWidth;
        int newHeight = image.getHeight() / semiImageSize;
        Image[][] newImage = new Image[newHeight][newWidth];
        for(int i=0; i<newHeight; i++) {
            for(int j=0; j<newWidth; j++) {
                newImage[i][j] = createSemiImage(image, semiImageSize, i*semiImageSize, j*semiImageSize);
            }
        }
        return newImage;
    }

    private static Image createSemiImage(Image image, int size, int y, int x) {
        Color[][] pixelsArray = new Color[size][size];
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                pixelsArray[i][j] = image.getPixel(i+y,j+x);
            }
        }
        return new Image(pixelsArray, size, size);
    }

    /**
     * static func the gets an image and calculate its brightness according to the ascii algoritem
     * @param image image to be calculate
     * @return the calculated brightness
     */
    public static double calculateBrightness(Image image) {
        int pixelAmount = image.getHeight() * image.getWidth();
        double sum = 0;
        for(int i=0; i<image.getHeight();i++) {
            for(int j=0; j<image.getWidth(); j++) {
                sum += calculateGreyValue(image.getPixel(i,j));
            }
        }
        return sum / (WHITE_COLOR * pixelAmount);
    }
    private static double calculateGreyValue(Color color) {
        return (color.getRed() * ALGO_RED) + (color.getGreen() * ALGO_GREEN) + (color.getBlue() * ALGO_BLUE);
    }

    private Image image;
    private int resolution;
    private Image bufferImage;
    private Image[][] semiImageArray;
    private boolean isResUpdated;
    private boolean isImageUpdated;
    private int semiImageSize;
    private int newHeight;
    private double[][] brightnessArray;

    /**
     * create new ImageFunctions object
     * @param image an image to transfer
     * @param resolution the new resolution
     */
    public ImageFunctions(Image image, int resolution) {
        this.image = image;
        this.resolution = resolution;
        this.isImageUpdated = true;
        this.isResUpdated = true;
        brightnessArray = createBrightnessArray();
    }

    /**
     * update the image in the object
     * @param image new image
     */
    public void updateImage(Image image) {
        this.image = image;
        isImageUpdated = true;
        isResUpdated = true;
    }

    /**
     * update the resolution
     * @param resolution new resolution
     */
    public void updatedRes(int resolution) {
        this.resolution = resolution;
        isResUpdated = true;
    }

    /**
     * create new brightness array
     * @return 2-D double array represent the brightness of each semi-image in the 2-D image array
     */
    public double[][] createBrightnessArray() {
        if(isImageUpdated) {
            this.bufferImage =  ImageFunctions.buffer(image);
            isImageUpdated = false;
        }
        if(isResUpdated) {
            this.semiImageArray = ImageFunctions.split(bufferImage, resolution);
            this.semiImageSize = bufferImage.getWidth() / resolution;
            this.newHeight = bufferImage.getHeight() / semiImageSize;
            brightnessArray = new double[newHeight][resolution];
            for(int i=0; i<newHeight;i++) {
                for(int j=0; j<resolution; j++) {
                    brightnessArray[i][j] = ImageFunctions.calculateBrightness(semiImageArray[i][j]);
                }
            }
            isResUpdated = false;
        }
        return  brightnessArray;
    }

    /**
     * return the width of the ascii image
     * @return the ascii image width
     */
    public int getWidth() {
        return resolution;
    }

    /**
     * return the height of the ascii image
     * @return the ascii image height
     */
    public int getHeight() {
        return newHeight;
    }

    /**
     * return the width of the buffered image
     * @return the buffered image width
     */
    public int getBufferWidth() {
        if(isImageUpdated) {
            this.bufferImage =  ImageFunctions.buffer(image);
            isImageUpdated = false;
        }
        return bufferImage.getWidth();
    }

    /**
     * return the height of the buffered image
     * @return the buffered image height
     */
    public int getBufferHeight() {
        if(isImageUpdated) {
            this.bufferImage =  ImageFunctions.buffer(image);
            isImageUpdated = false;
        }
        return bufferImage.getHeight();
    }
}
