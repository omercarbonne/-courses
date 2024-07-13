package bricker.gameobjects;

import bricker.brick_strategies.*;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.main.BrickerGameManager;

/**
 * class for the bricks in the bricker game
 */
public class Brick extends GameObject {
    private CollisionStrategy collisionstrategy;
    private Counter doubleCounter = new Counter(0);

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable);
        StrategiesFactory factory =
                new StrategiesFactory(brickerGameManager, doubleCounter);
        this.collisionstrategy = factory.getStrategy(true);
    }

    /**
     * determines what happened when collision occurred with the brick
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        collisionstrategy.onCollision(this, other);
    }
}
