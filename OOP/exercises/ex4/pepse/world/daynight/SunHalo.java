package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the halo effect around the sun during daytime.
 */
public class SunHalo {
    private static final String SUN_HALO = "sunHalo";
    private static final float COMPARE_TO_SUN = 3;
    private static final int RED = 255;
    private static final int GREEN = 255;
    private static final int BLUE = 0;
    private static final int ALPHA = 20;
    private static final int DIVIDE_2 = 2;

    /**
     * Creates a GameObject representing the halo effect around the sun during daytime.
     *
     * @param sun The GameObject representing the sun.
     * @return A GameObject representing the halo effect around the sun.
     */
    public static GameObject create(GameObject sun) {
        float haloSize = sun.getDimensions().y() * COMPARE_TO_SUN;
        Renderable renderable = new OvalRenderable(new Color(RED, GREEN, BLUE, ALPHA));
        Vector2 topLeftSun = new Vector2(sun.getCenter().x() - (haloSize / DIVIDE_2),
                sun.getCenter().y() - (haloSize / DIVIDE_2));
        GameObject sunHalo = new GameObject(topLeftSun, new Vector2(haloSize, haloSize), renderable);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(SUN_HALO);
        // Ensure the sun halo is always positioned at the center of the sun
        sunHalo.addComponent((deltaTime) -> {
            sunHalo.setCenter(sun.getCenter());
        });
        return sunHalo;
    }
}
