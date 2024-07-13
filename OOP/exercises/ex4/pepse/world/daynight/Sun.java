package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the sun object and its movement during the day-night cycle.
 */
public class Sun {
    private static final int SUN_SIZE = 70;
    private static final float INITIAL_VALUE = 0;
    private static final float FINAL_VALUE = 360;
    private static final String SUN = "sun";
    private static final int DIVIDE_2 = 2;
    private static final float THIRD = (float) 1 / 3;
    private static final float TWO_THIRD = THIRD * 2;

    /**
     * Creates a GameObject representing the sun.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength      The length of the day-night cycle in seconds.
     * @return A GameObject representing the sun.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        // Create sun object
        Renderable renderable = new OvalRenderable(Color.YELLOW);
        Vector2 centerSky = new Vector2(windowDimensions.x() / DIVIDE_2, windowDimensions.y() * THIRD);
        Vector2 topLeftSun = new Vector2(centerSky.x() -
                (SUN_SIZE / DIVIDE_2), centerSky.y() - (SUN_SIZE / DIVIDE_2));
        GameObject sun = new GameObject(topLeftSun, new Vector2(SUN_SIZE, SUN_SIZE), renderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN);
        // Create transition for sun movement
        Vector2 initialSunCenter = centerSky;
        Vector2 cycleCenter = new Vector2(windowDimensions.x()
                / DIVIDE_2, windowDimensions.y() * (TWO_THIRD));
        Transition<Float> transition = new Transition<>(sun, (Float angle) ->
                sun.setCenter(initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter)),
                INITIAL_VALUE, FINAL_VALUE, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);
        return sun;
    }
}
