package bricker.brick_strategies;

import danogl.GameObject;
import bricker.main.BrickerGameManager;

/**
 * class for the falling heart strategy
 */
public class FallingHeartCollisionStrategy implements CollisionStrategy{
    private BrickerGameManager basicGM;

    /**
     * create new FallingHeartCollisionStrategy object
     * @param basicGM the game menger
     */
    public FallingHeartCollisionStrategy(BrickerGameManager basicGM) {
        this.basicGM = basicGM;
    }

    /**
     * determines what is the strategy when collision occurred with the brick
     * @param obj1 the brick with this effect
     * @param obj2 the ball that hit the brick
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        basicGM.createFallingHeart(obj1.getCenter());
        basicGM.deleteBrickObject(obj1);
    }
}
