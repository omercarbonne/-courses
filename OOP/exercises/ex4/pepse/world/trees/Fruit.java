package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a fruit GameObject in the game world.
 */
public class Fruit extends GameObject {
    private static final float INITIAL_VALUE = 0;
    private static final float FINAL_VALUE = 0;
    private static final float CYCLE_LENGTH = 30;
    private static final String AVATAR = "Avatar";
    private static final String FRUIT = "fruit";
    private static final float VISIBLE = 1;
    private static final String INVISIBLE_FRUIT = "invisibleFruit";
    private static final float INVISIBLE = 0;

    /**
     * Constructs a Fruit object with the specified parameters.
     *
     * @param topLeftCorner The position of the top-left corner of the fruit.
     * @param dimensions    The dimensions of the fruit.
     * @param renderable    The renderable component for the fruit.
     */
    public Fruit(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(AVATAR)) {
            // Transition to make the fruit invisible when colliding with the avatar
            Transition<Float> transition = new Transition<>(this,
                    (num) -> onCollisionWithAvatar(), INITIAL_VALUE, FINAL_VALUE,
                    Transition.LINEAR_INTERPOLATOR_FLOAT,
                    CYCLE_LENGTH, Transition.TransitionType.TRANSITION_ONCE, () -> bringBackFruit());
        }
    }

    /**
     * Makes the fruit visible again after transitioning from being invisible.
     */
    private void bringBackFruit() {
        this.setTag(FRUIT);
        this.renderer().setOpaqueness(VISIBLE);
    }

    /**
     * Makes the fruit invisible when colliding with the avatar.
     */
    private void onCollisionWithAvatar() {
        this.setTag(INVISIBLE_FRUIT);
        this.renderer().setOpaqueness(INVISIBLE);
    }
}
