package bricker.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;
import bricker.main.BrickerGameManager;

/**
 * class for the double strategy
 */
public class DoubleCollisionStrategy implements CollisionStrategy{
    private BrickerGameManager basicGM;
    private Counter strategyCounter;
    private CollisionStrategy strategy1;
    private CollisionStrategy strategy2;

    /**
     * create new DoubleCollisionStrategy object
     * @param basicGM the game menger
     * @param doubleCounter counter how much double strategies this brick has
     */
    public DoubleCollisionStrategy(BrickerGameManager basicGM, Counter doubleCounter) {
        this.basicGM = basicGM;
        this.strategyCounter = doubleCounter;
        StrategiesFactory factory = new StrategiesFactory(basicGM, strategyCounter);
        strategy1 = factory.getStrategy(false);
        strategy2 = factory.getStrategy(false);
    }

    /**
     * determines what is the strategy when collision occurred with the brick
     * @param obj1 the brick with this effect
     * @param obj2 the ball that hit the brick
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        basicGM.increaseBrickCounter();
        strategy1.onCollision(obj1, obj2);
        strategy2.onCollision(obj1,obj2);
    }
}
