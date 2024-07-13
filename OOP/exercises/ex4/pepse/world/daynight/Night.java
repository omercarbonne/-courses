package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the night-time effect in the game world.
 */
public class Night {
    /** the initial value of the night status - day */
    public static final float INITIAL_VALUE = 0;
    /** the final value of the night in its circle - midnight */
    public static final float FINAL_VALUE = 0.5f;
    private static final String NIGHT = "night";

    /**
     * Creates a GameObject representing the night-time effect.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength      The length of the day-night cycle in seconds.
     * @return A GameObject representing the night-time effect.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        // Create night-time renderable
        Renderable renderable = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, renderable);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT);
        // Create transition for night-time effect
        Transition<Float> transition = new Transition<>(night, night.renderer()::setOpaqueness,
                INITIAL_VALUE, FINAL_VALUE, Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return night;
    }
}