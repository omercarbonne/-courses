package bricker.brick_strategies;

import danogl.util.Counter;
import bricker.main.BrickerGameManager;

import java.util.Random;

/**
 * class for deciding what strategy the brick will get
 */
public class StrategiesFactory {
    private Random random = new Random();
    private BrickerGameManager basicGM;
    private Counter doubleCounter;
    private static final int SPECIAL_STRATEGY=4;
    private static final int NORMAL_STRATEGY = 10;
    private static final int NO_MORE_DOUBLE=2;
    private static final int PUCK = 0;
    private static final int PADDLE = 1;
    private static final int CAMERA = 2;
    private static final int FALLING_HEART = 3;
    private static final int DOUBLE = 4;

    /**
     * create new StrategiesFactory object
     * @param basicGM the game maneger
     * @param doubleCounter counter how much double strategies this brick has
     */
    public StrategiesFactory(BrickerGameManager basicGM, Counter doubleCounter) {
        this.basicGM = basicGM;
        this.doubleCounter = doubleCounter;
    }

    /**
     * choose strategy for the brick
     * @param includeBasic boolean type if the strategy should be only special or not
     * @return new strategy for the brick
     */
    public CollisionStrategy getStrategy(boolean includeBasic) {
        int num = random.nextInt(SPECIAL_STRATEGY);
        CollisionStrategy col = new BasicCollisionStrategy(basicGM);
        if(includeBasic) {
            num = random.nextInt(NORMAL_STRATEGY);
        }
        if(doubleCounter.value() == NO_MORE_DOUBLE) {
            num = random.nextInt(SPECIAL_STRATEGY);
        }
        switch (num) {
            case PUCK:
                col = new PuckCollisionStrategy(basicGM);
                break;
            case PADDLE:
                col = new PaddleCollisionStrategy(basicGM);
                break;
            case CAMERA:
                col = new CameraCollisionStrategy(basicGM);
                break;
            case FALLING_HEART:
                col = new FallingHeartCollisionStrategy(basicGM);
                break;
            case DOUBLE:
                doubleCounter.increment();
                col = new DoubleCollisionStrategy(basicGM, doubleCounter);
                break;
        }
        return col;
    }
}
