package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import java.awt.*;

/**
 * Represents the sky in the game world.
 */
public class Sky {

    /** The basic color of the sky. */
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /** The tag for the sky GameObject. */
    private static final String SKY = "sky";

    /**
     * Creates a GameObject representing the sky based on the given window dimensions.
     *
     * @param windowDimensions The dimensions of the game window.
     * @return The GameObject representing the sky.
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(SKY);
        return sky;
    }
}
