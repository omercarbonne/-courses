package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * class for the basic strategy
 */
public class BasicCollisionStrategy implements CollisionStrategy {

    private BrickerGameManager basicGM;

    /**
     * create new BasicCollisionStrategy object
     * @param basicGM the game menger
     */
    public BasicCollisionStrategy(BrickerGameManager basicGM) {
        this.basicGM = basicGM;
    }

    /**
     * determines what is the strategy when collision occurred with the brick
     * @param obj1 the brick with this effect
     * @param obj2 the ball that hit the brick
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        //System.out.println("collision with brick detected");
        basicGM.deleteBrickObject(obj1);
    }
}
