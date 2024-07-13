package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * class for the paddle in the bricker game
 */
public class Paddle extends GameObject {
    private static final float MOVMENT_SPEED = 300;
    private UserInputListener inputListener;
    private Vector2 windowD;
    private int collisionCounter = 0;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *      *                      a given key is currently pressed by the user or not. See its
     *      *                      documentation.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowD) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowD = windowD;
    }

    /**
     * update the paddle object in the game
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkBorder();
        Vector2 movmentDir = Vector2.ZERO;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movmentDir = movmentDir.add(Vector2.LEFT);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movmentDir = movmentDir.add(Vector2.RIGHT);
        }
        setVelocity(movmentDir.mult(MOVMENT_SPEED));
    }

    /**
     * determines what happened when collision occurred with the paddle
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionCounter++;
    }

    /**
     * @return the collision Counter
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }

    /**
     * set the collisionCounter
     * @param collisionCounter new int to set the collision counter
     */
    public void setCollisionCounter(int collisionCounter) {
        this.collisionCounter = collisionCounter;
    }

    /**
     * makes sure the paddle not going out of the game window
     */
    private void checkBorder() {
        Vector2 topLeft = getTopLeftCorner();
        if(topLeft.x() < 0) {
            setTopLeftCorner(new Vector2(0, topLeft.y()));
        }
        if(topLeft.x() > windowD.x() - getDimensions().x()) {
            setTopLeftCorner(new Vector2(windowD.x() - getDimensions().x(), topLeft.y()));
        }
    }
}
