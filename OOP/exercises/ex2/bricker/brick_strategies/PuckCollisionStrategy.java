package bricker.brick_strategies;

import danogl.GameObject;
import bricker.main.BrickerGameManager;

/**
 * class for the punk strategy
 */
public class PuckCollisionStrategy implements CollisionStrategy{
    private BrickerGameManager basicGM;

    /**
     * create new PuckCollisionStrategy object
     * @param basicGM the game menger
     */
    public PuckCollisionStrategy(BrickerGameManager basicGM) {
        this.basicGM = basicGM;
    }

    /**
     * determines what is the strategy when collision occurred with the brick
     * @param obj1 the brick with this effect
     * @param obj2 the ball that hit the brick
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        basicGM.addPuck(obj1.getCenter());
        basicGM.addPuck(obj1.getCenter());
        basicGM.deleteBrickObject(obj1);
    }
}
