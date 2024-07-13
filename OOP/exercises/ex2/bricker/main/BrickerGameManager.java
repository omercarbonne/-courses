package bricker.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * BrickerGameManager is a class that run and control the bricker game.
 */
public class BrickerGameManager extends GameManager{
    private static final int DEFAULT_LIFE = 2;
    private static final int DEFAULT_ROWS = 7;
    private static final int DEFAULT_COLS = 8;
    private static final int WINDOW_LENGTH = 700;
    private static final int WINDOW_WIDTH = 500;
    private static final float BALL_SPEED = 150;
    private static final int BALL_SIZE = 20;
    private static final String BALL_IMG = "assets/ball.png";
    private static final String BALL_SOUND = "assets/blop_cut_silenced.wav";
    private static final String PADDLE_IMG = "assets/paddle.png";
    private static final int PADDLE_LENGTH = 100;
    private static final int PADDLE_WIDTH = 15;
    private static final int PADDLE_PADDING = 30;
    private static final int WALL_WIDTH = 5;
    private static final String BG_IMG = "assets/DARK_BG2_small.jpeg";
    private static final String BRICK_IMG = "assets/brick.png";
    private static final int BRICK_WIDTH = 15;
    private static final String LOSE_MSG = "You lose! Play again?";
    private static final String WIN_MSG = "You win! Play again?";
    private static final String PUCK_IMG = "assets/mockBall.png";
    private int rows;
    private int cols;
    private Ball ball;
    private String ballTag;
    private int ballCounter;
    private int lifeCounter = DEFAULT_LIFE;
    private Vector2 windowD;
    private WindowController windowController;
    private HeartsCell heartCell;
    private TextRenderable text;
    private Counter brickCounter;
    private UserInputListener inputListener;
    private Ball[] pucks = new Ball[100];
    private int pucksCounter = 0;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private String MAIN_PADDLE_TAG = "main";
    private int NO_EXTRA_PADDLE = 4;
    private Paddle extraPaddle;
    private static final String HEART_IMG = "assets/heart.png";
    private static final int HEART_SIZE = 15;
    private static final int HEART_SPEED = 100;
    private static final int MAX_HEARTS = 4;
    private static final int STOP_CAMERA = 4;
    private static final float SET_CENTER = 0.5f;
    private static final float PUCK_SIZE = 0.75f;
    private static final float CAMERA_SIZE = 1.2f;
    private static final int DIVIDE_BY_2 = 2;
    private static final int ONE = 1;
    private static final int TEXT_SIZE = 15;
    private static final int TEXT_Y_PADDING = 20;
    private static final int TEXT_X_PADDING = 5;
    private static final int CUSTOM_GAME = 2;
    private static final int COLS_ARRGS = 1;
    private static final int ROWS_ARRGS = 0;
    private static final int DEFUALT_COLS = 4;
    private static final int DEFUALT_ROWS = 3;

    /**
     * create a new BrickerGameManager object
     * @param windowTitle the name of the game
     * @param windowD window game size
     * @param rows the game rows
     * @param cols the game cols
     */
    public BrickerGameManager(String windowTitle, Vector2 windowD, int rows, int cols) {
        super(windowTitle, windowD);
        this.rows = rows;
        this.cols = cols;
        this.brickCounter = new Counter(rows*cols);
    }

    /**
     * create a new BrickerGameManager object
     * @param windowTitle the name of the game
     * @param windowD window game size
     */
    public BrickerGameManager(String windowTitle, Vector2 windowD) {
        super(windowTitle, windowD);
        this.rows = DEFAULT_ROWS;
        this.cols = DEFAULT_COLS;
    }

    /**
     * initialize the game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowD = windowController.getWindowDimensions();
        this.windowController = windowController;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        createBall();
        GameObject mainPaddle = createPaddle();
        mainPaddle.setTag(MAIN_PADDLE_TAG);
        gameObjects().addGameObject(mainPaddle);
        extraPaddle = (Paddle) createPaddle();
        extraPaddle.setCollisionCounter(NO_EXTRA_PADDLE);
        addWalls();
        createBackground();
        createBricks();
        createHearts();
        createText();
    }

    /**
     * update the game
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkPucks();
        if(extraPaddle.getCollisionCounter() == NO_EXTRA_PADDLE) {
            gameObjects().removeGameObject(extraPaddle);
        }
        if(camera() != null) {
            if(ball.getCollisionCounter() >= ballCounter+STOP_CAMERA) {
                setCamera(null);
            }
        }
        float ballHeight = ball.getCenter().y();
        if(ballHeight > windowD.y()) {
            removeHeart();
            ball.setCenter(windowD.mult(SET_CENTER));
            setBallDirection();
            setCamera(null);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_W)) {
            winGame();
        }
    }

    /**
     * check if there are pucks balls that fall out of the game
     * and delete them
     */
    private void checkPucks() {
        for(Ball ball : pucks) {
            if(ball == null) {
                continue;
            }
            float ballHeight = ball.getCenter().y();
            if(ballHeight > windowD.y()) {
                gameObjects().removeGameObject(ball);
            }
        }
    }

    /**
     * create a new bal to the game
     */
    private void createBall() {
        // create ball
        Renderable ballImage = imageReader.readImage(BALL_IMG, true);
        Sound collisionSound = soundReader.readSound(BALL_SOUND);
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE,BALL_SIZE), ballImage, collisionSound);
        ballTag = ball.getTag();
        // initialize ball direction
        setBallDirection();
        // centering the ball
        ball.setCenter(windowD.mult(SET_CENTER));
        // add the ball to game objects
        gameObjects().addGameObject(ball);
    }

    /**
     * create new Falling Heart object to the game
     * @param brickLocation the center location of the brick that had this effect
     */
    public void createFallingHeart(Vector2 brickLocation) {
        Renderable heartImage = imageReader.readImage(HEART_IMG, true);
        GameObject heartObject = new FallingHeart(brickLocation, new Vector2(HEART_SIZE,HEART_SIZE),
                heartImage, MAIN_PADDLE_TAG, this, windowD);
        heartObject.setVelocity(new Vector2(0, HEART_SPEED));
        gameObjects().addGameObject(heartObject);
    }

    /**
     * sets the ball direction
     */
    private void setBallDirection() {
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean()) {
            ballVelX *= -(ONE);
        }
        if(rand.nextBoolean()) {
            ballVelY *= -(ONE);
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * create a new paddle
     * @return new paddle that is not yet added to the game objects
     */
    private GameObject createPaddle() {
        // create paddle
        Renderable paddleImage = imageReader.readImage(PADDLE_IMG, true);
        GameObject paddleObject = new Paddle(Vector2.ZERO,
                new Vector2(PADDLE_LENGTH, PADDLE_WIDTH), paddleImage, inputListener, windowD);
        // centering the paddle
        paddleObject.setCenter(new Vector2(windowD.x()/DIVIDE_BY_2, windowD.y()-PADDLE_PADDING));
        // add the paddle to game objects
        return paddleObject;
    }

    /**
     * create the walls of the game
     */
    private void addWalls() {
        // create the walls
        GameObject leftWallObject = new GameObject(new Vector2(0-WALL_WIDTH, 0),
                new Vector2(WALL_WIDTH, windowD.y()), null);
        GameObject rightWallObject = new GameObject(new Vector2(windowD.x(), 0),
                new Vector2(WALL_WIDTH, windowD.y()), null);
        GameObject topWallObject = new GameObject(new Vector2(0, 0-WALL_WIDTH),
                new Vector2(windowD.x(), WALL_WIDTH), null);
        // add the walls to the game objects
        gameObjects().addGameObject(leftWallObject, Layer.STATIC_OBJECTS);
        gameObjects().addGameObject(rightWallObject, Layer.STATIC_OBJECTS);
        gameObjects().addGameObject(topWallObject, Layer.STATIC_OBJECTS);
    }

    /**
     * create the background of the game
     */
    private void createBackground() {
        // create background
        Renderable backgroundImage =
                imageReader.readImage(BG_IMG, false);
        GameObject backgroundObject = new GameObject(Vector2.ZERO, windowD, backgroundImage);
        // centering background with the camera
        backgroundObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        // add background to the game objects
        gameObjects().addGameObject(backgroundObject, Layer.BACKGROUND);
    }

    /**
     * create the bricks of the game
     */
    private void createBricks() {
        Renderable brickImage = imageReader.readImage(BRICK_IMG, false);
        int speaceForBrick = (int) windowD.x() / cols;
        for(int i=0; i<(rows*(BRICK_WIDTH+ONE)); i+=(BRICK_WIDTH+ONE)) {
            for(int j=0; j< windowD.x(); j+=speaceForBrick) {
                GameObject brickObject = new Brick(new Vector2(j+ONE,i),
                        new Vector2(speaceForBrick-ONE,BRICK_WIDTH), brickImage,this);
                // add the brick to game objects
                gameObjects().addGameObject(brickObject, Layer.STATIC_OBJECTS);
            }
        }
    }

    /**
     * create the heart counters of the player in the game
     */
    public void createHearts() {
        heartCell = new HeartsCell(MAX_HEARTS, imageReader, windowD, HEART_IMG, HEART_SIZE);
        for(int i=0; i<(DEFAULT_LIFE+ONE); i++){
            GameObject heart = heartCell.getHeart(i);
            gameObjects().addGameObject(heart, Layer.UI);
        }
    }

    /**
     * create the life text number of the player in the game
     */
    public void createText() {
        text = new TextRenderable(String.valueOf(lifeCounter+ONE));
        text.setColor(Color.green);
        GameObject textObject = new GameObject(new Vector2(TEXT_X_PADDING,
                windowD.y()-TEXT_Y_PADDING), new Vector2(TEXT_SIZE,TEXT_SIZE),text);
        gameObjects().addGameObject(textObject, Layer.UI);
    }

    /**
     * delete one brick object and update the brick counter
     * @param obj the deleted brick
     */
    public void deleteBrickObject(GameObject obj) {
        gameObjects().removeGameObject(obj, Layer.STATIC_OBJECTS);
        brickCounter.decrement();
        if(brickCounter.value() == 0) {
            winGame();
        }
    }

    /**
     * increase the brick counter by 1
     */
    public void increaseBrickCounter() {
        brickCounter.increment();
    }

    /**
     * wining window, check if the player want to play again
     */
    private void winGame() {
        if(windowController.openYesNoDialog(WIN_MSG)) {
            windowController.resetGame();
        }
        else {
            windowController.closeWindow();
        }
    }

    /**
     * losing window, check if the player want to play again
     */
    private void loseGame() {
        if(windowController.openYesNoDialog(LOSE_MSG)) {
            windowController.resetGame();
        }
        else {
            windowController.closeWindow();
        }
    }

    /**
     * remove the object from the game objects
     * @param obj the object to be removed
     */
    public void removeObject(GameObject obj) {
        gameObjects().removeGameObject(obj);
    }

    /**
     * add 1 heart to the heart counter of the player in the game
     */
    public void addHeart() {
        if(lifeCounter == MAX_HEARTS-ONE) {
            return;
        }
        GameObject heart = heartCell.getHeart(lifeCounter+ONE);
        gameObjects().addGameObject(heart, Layer.UI);
        lifeCounter++;
        updateText();
    }

    /**
     * remove 1 heart to the heart counter of the player in the game
     */
    public void removeHeart() {
        GameObject heart = heartCell.getHeart(lifeCounter);
        gameObjects().removeGameObject(heart, Layer.UI);
        lifeCounter--;
        updateText();
        if(lifeCounter == -(ONE)) {
            loseGame();
        }
    }

    /**
     * update the life text counter of the player in the game
     */
    public void updateText() {
        text.setString(String.valueOf(lifeCounter+ONE));
        if(lifeCounter >= DEFAULT_LIFE){
            text.setColor(Color.green);
            return;
        }
        if(lifeCounter == ONE) {
            text.setColor(Color.yellow);
            return;
        }
        text.setColor(Color.red);
    }

    /**
     * add new puck ball to the game
     * @param brickLocation the center location of the brick that had this effect
     */
    public void addPuck(Vector2 brickLocation) {
        // create ball
        Renderable pucklImage = imageReader.readImage(PUCK_IMG, true);
        Sound collisionSound = soundReader.readSound(BALL_SOUND);
        pucks[pucksCounter] = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE,BALL_SIZE).mult(PUCK_SIZE),
                pucklImage, collisionSound);
        // initialize puck direction
        Random random = new Random();
        double angel = random.nextDouble() * Math.PI;
        float velX = (float) Math.cos(angel) * BALL_SPEED;
        float velY = (float) Math.sin(angel) * BALL_SPEED;
        pucks[pucksCounter].setVelocity(new Vector2(velX, velY));
        // centering the puck to the brick
        pucks[pucksCounter].setCenter(brickLocation);
        // add the puck to game objects
        gameObjects().addGameObject(pucks[pucksCounter]);
        pucksCounter++;
    }

    /**
     * add the extra paddle to the game
     */
    public void addExtraPaddle() {
        if(extraPaddle.getCollisionCounter() == NO_EXTRA_PADDLE) {
            extraPaddle.setCollisionCounter(0);
            extraPaddle.setCenter(new Vector2(windowD.x()/DIVIDE_BY_2, windowD.y()/DIVIDE_BY_2));
            gameObjects().addGameObject(extraPaddle);
        }
    }

    /**
     * change to camera mode
     * @param objTag the tag of the object that hit the brick with this effect
     */
    public void cameraMode(String objTag) {
        if(objTag.equals(ballTag) && camera() == null) {
            setCamera(new Camera(ball, Vector2.ZERO, windowController.getWindowDimensions().mult(CAMERA_SIZE),
                    windowController.getWindowDimensions()));
            ballCounter = ball.getCollisionCounter();
        }
    }

    /**
     * the main of the program, initialize the program
     * @param args arguments from the user
     */
    public static void main(String[] args) {
        if(args.length == CUSTOM_GAME) {
            new BrickerGameManager("Test", new Vector2(WINDOW_LENGTH, WINDOW_WIDTH),
                    Integer.parseInt(args[COLS_ARRGS]), Integer.parseInt(args[ROWS_ARRGS])).run();
        }
        else {
            new BrickerGameManager("Test",
                    new Vector2(WINDOW_LENGTH, WINDOW_WIDTH),DEFUALT_ROWS,DEFUALT_COLS).run();
        }
    }
}