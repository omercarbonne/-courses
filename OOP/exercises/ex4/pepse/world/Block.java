package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a block in the game world.
 */
public class Block extends GameObject {

    /** The size of the block. */
    public static final int SIZE = 30;

    /**
     * Constructs a Block object with the specified top-left corner position and renderable.
     *
     * @param topLeftCorner The top-left corner position of the block.
     * @param renderable    The renderable to represent the block visually.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
