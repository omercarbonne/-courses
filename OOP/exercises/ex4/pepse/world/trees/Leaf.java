package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;

/**
 * Represents a leaf GameObject in the game world.
 */
public class Leaf {
    private static final int DEFAULT_SIZE = 30;
    private static final float ANGLE_90 = 90;
    private static final float ANGLE_FULL = 360;
    private static final int CYCLE_LENGTH = 15;
    private static final int CYCLE_LENGTH_SPECIAL = 1;
    private static final int RANDOM_LIMIT = 10;
    private static final int TASK_DIVIDER = 5;
    private static final Vector2 VECTOR_INITIAL_VALUE = new Vector2(DEFAULT_SIZE, DEFAULT_SIZE);
    private static final Vector2 VECTOR_FINAL_VALUE = new Vector2(DEFAULT_SIZE / 1.5f, DEFAULT_SIZE);
    private static final int RED = 50;
    private static final int GREEN = 200;
    private static final int BLUE = 30;
    private static final String LEAF = "leaf";
    private static Random random = new Random();

    /**
     * Creates a leaf GameObject at the specified coordinates.
     *
     * @param coordinates The coordinates of the leaf GameObject.
     * @return A leaf GameObject.
     */
    public static GameObject create(Vector2 coordinates) {
        Renderable renderable = new RectangleRenderable(new Color(RED, GREEN, BLUE));
        GameObject leaf = new GameObject(coordinates, new Vector2(DEFAULT_SIZE, DEFAULT_SIZE), renderable);
        leaf.setTag(LEAF);
        // Transitions
        int num = random.nextInt(RANDOM_LIMIT);
        ScheduledTask angleTask = new ScheduledTask(leaf, num / TASK_DIVIDER, false, () -> {
            transitionAngle(leaf);
        });
        ScheduledTask widthTask = new ScheduledTask(leaf, num / TASK_DIVIDER, false, () -> {
            transitionWidth(leaf);
        });
        return leaf;
    }

    /**
     * Performs a transition to change the angle of the leaf GameObject by 90 degrees.
     *
     * @param currentLeaf The leaf GameObject to transition.
     */
    public static void transitionAngle90D(GameObject currentLeaf) {
        Transition<Float> transitionAngle = new Transition<>(currentLeaf, (Float angle) ->
                currentLeaf.renderer().setRenderableAngle(angle),
                currentLeaf.renderer().getRenderableAngle(),
                (currentLeaf.renderer().getRenderableAngle() + ANGLE_90),
                Transition.LINEAR_INTERPOLATOR_FLOAT, CYCLE_LENGTH_SPECIAL,
                Transition.TransitionType.TRANSITION_ONCE, () -> transitionAngle(currentLeaf));
    }

    /**
     * Performs a transition to change the angle of the leaf GameObject by 360 degrees.
     *
     * @param currentLeaf The leaf GameObject to transition.
     */
    private static void transitionAngle(GameObject currentLeaf) {
        Transition<Float> transitionAngle = new Transition<>(currentLeaf, (Float angle) ->
                currentLeaf.renderer().setRenderableAngle(angle),
                currentLeaf.renderer().getRenderableAngle(),
                currentLeaf.renderer().getRenderableAngle() + ANGLE_FULL,
                Transition.LINEAR_INTERPOLATOR_FLOAT, CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * Performs a transition to change the width of the leaf GameObject.
     *
     * @param currentLeaf The leaf GameObject to transition.
     */
    private static void transitionWidth(GameObject currentLeaf) {
        Transition<Vector2> transitionWidth = new Transition<>(currentLeaf, (Vector2 dimensionsAsVector2) ->
                currentLeaf.setDimensions(dimensionsAsVector2),
                VECTOR_INITIAL_VALUE, VECTOR_FINAL_VALUE, Transition.LINEAR_INTERPOLATOR_VECTOR, CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }
}
