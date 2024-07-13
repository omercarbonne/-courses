package bricker.brick_strategies;

import danogl.GameObject;
import bricker.main.BrickerGameManager;

/**
 * class for the camera strategy
 */
public class CameraCollisionStrategy implements CollisionStrategy{

    private BrickerGameManager basicGM;

    /**
     * create new CameraCollisionStrategy object
     * @param basicGM the game menger
     */
    public CameraCollisionStrategy(BrickerGameManager basicGM) {
        this.basicGM = basicGM;
    }

    /**
     * determines what is the strategy when collision occurred with the brick
     * @param obj1 the brick with this effect
     * @param obj2 the ball that hit the brick
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        basicGM.cameraMode(obj2.getTag());
        basicGM.deleteBrickObject(obj1);
    }
}
