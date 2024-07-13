package pepse.world.trees;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.GameObjectPhysics;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.Random;

/**
 * Represents a stem GameObject in the game world.
 */
public class Stem {

    private static final int MIN_HEIGHT = 8;
    private static final int MAX_HEIGHT = 12;
    private static final int DEFAULT_WIDTH = 30;
    private static final Color DEFAULT_COLOR = new Color(100, 50, 20);
    private static final String STEM = "Stem";
    private static final int ADD_1 = 1;
    private static final Random random = new Random();

    /**
     * Creates a stem GameObject at the specified coordinates.
     *
     * @param coordinates The coordinates of the stem GameObject.
     * @return A stem GameObject.
     */
    public static GameObject create(Vector2 coordinates) {
        Renderable renderable = new RectangleRenderable(DEFAULT_COLOR);
        int height = getRandomHeight();
        Vector2 topLeft = new Vector2(coordinates.x(), coordinates.y() - height * DEFAULT_WIDTH);
        GameObject stem = new GameObject(topLeft, new Vector2(DEFAULT_WIDTH,
                height * DEFAULT_WIDTH), renderable);
        stem.setTag(STEM);
        stem.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        stem.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        return stem;
    }

    /**
     * Generates a random height for the stem.
     *
     * @return A random height for the stem.
     */
    private static int getRandomHeight() {
        int res = random.nextInt(MAX_HEIGHT - MIN_HEIGHT + ADD_1);
        return res + MIN_HEIGHT;
    }

    /**
     * Updates the appearance of the stem when the character jumps on it.
     *
     * @param stem The stem GameObject.
     */
    public static void heJumpedApp(GameObject stem) {
        stem.renderer().setRenderable(
                new RectangleRenderable(ColorSupplier.approximateColor(DEFAULT_COLOR)));
    }

    /**
     * Restores the appearance of the stem when the character is on the ground.
     *
     * @param stem The stem GameObject.
     */
    public static void heIsOnGround(GameObject stem) {
        stem.renderer().setRenderable(new RectangleRenderable(DEFAULT_COLOR));
    }
}
