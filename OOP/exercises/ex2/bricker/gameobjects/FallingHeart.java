package bricker.gameobjects;

import com.sun.jdi.request.InvalidRequestStateException;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.main.BrickerGameManager;

/**
 * class for the falling hearts in the bricker game
 */
public class FallingHeart extends GameObject {
    private String paddleTag;
    private BrickerGameManager brickerGameManager;
    private Vector2 windowD;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public FallingHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        String paddleTag, BrickerGameManager brickerGameManager, Vector2 windowD) {
        super(topLeftCorner, dimensions, renderable);
        this.paddleTag = paddleTag;
        this.brickerGameManager = brickerGameManager;
        this.windowD = windowD;
    }

    /**
     * check if the falling heart collied with the main paddle
     * @param other The other GameObject.
     * @return true if collide with the main paddle, others false
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        if(other.getTag().equals(paddleTag)) {
            return super.shouldCollideWith(other);
        }
        return false;
    }

    /**
     * update the falling heart object in the game
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        float heartHeight = getCenter().y();
        float windowHeight = windowD.y();
        if(heartHeight >= windowHeight) {
            brickerGameManager.removeObject(this);
        }
        super.update(deltaTime);
    }

    /**
     * determines what happened when falling heart collied with the main paddle
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        brickerGameManager.addHeart();
        brickerGameManager.removeObject(this);
    }
}
