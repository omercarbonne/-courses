package bricker.brick_strategies;

import danogl.GameObject;

/**
 * interface for the strategys
 */
public interface CollisionStrategy {
    /**
     * determines what is the strategy when collision occurred with the brick
     * @param obj1 the brick with this effect
     * @param obj2 the ball that hit the brick
     */
    public void onCollision(GameObject obj1, GameObject obj2);
}
