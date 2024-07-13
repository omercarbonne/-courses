package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * class for the hearts cell in the bricker game
 */
public class HeartsCell {
    private static final int HEART_GAP = 20;
    private static final int PADDING = 30;
    private GameObject[] heartArray;

    /**
     * crate new HeartsCell object
     * @param maxHearts maximum number of hearts in the game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *      *                 See its documentation for help.
     * @param windowD game window sizes
     * @param heartImg the image of the heart object
     * @param heartSize the size of the heart object
     */
    public HeartsCell(int maxHearts, ImageReader imageReader,
                      Vector2 windowD, String heartImg, int heartSize) {
        heartArray = new GameObject[maxHearts];
        // create hearts to start the game
        for(int i=0; i<maxHearts; i++) {
            Renderable heartImage = imageReader.readImage(heartImg, true);
            GameObject heartObject = new GameObject(
                    new Vector2(i*HEART_GAP + PADDING, windowD.y()-HEART_GAP),
                    new Vector2(heartSize, heartSize), heartImage);
            heartArray[i] = heartObject;
        }
    }

    /**
     * return the heart in the given index cell
     * @param index the cell in the hearts array
     * @return object game with static heart properties
     */
    public GameObject getHeart(int index) {
        return heartArray[index];
    }
}
