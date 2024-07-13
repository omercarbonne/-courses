package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Tree;

import java.util.List;

/**
 * Manages the main game logic and initialization for the Pepse game.
 */
public class PepseGameManager extends GameManager {

    /** The layer for sky GameObjects. */
    private static final int SKY_LAYER = Layer.BACKGROUND;

    /** The layer for ground GameObjects. */
    private static final int GROUND_LAYER = Layer.STATIC_OBJECTS;

    /** The layer for night GameObjects. */
    private static final int NIGHT_LAYER = Layer.UI;

    /** The default seed for terrain generation. */
    private static final int DEFAULT_SEED = 1;

    /** The cycle length of the night. */
    private static final float NIGHT_CYCLE = 30;

    /** The cycle length of the sun. */
    private static final float SUN_CYCLE = 30;

    /** The layer for sun halo GameObjects. */
    private static final int HALO_LAYER = -190;

    /** The buffer for avatar initialization on the X-axis. */
    private static final int AVATAR_INITALISE_BUFFER_X = 60;

    /** The buffer for avatar initialization on the Y-axis. */
    private static final float AVATAR_INITALISE_BUFFER_Y = (float) 1/3;

    /** The layer for avatar GameObjects. */
    private static final int AVATAR_LAYER = Layer.DEFAULT;

    /** The layer for energy GameObjects. */
    private static final int ENERGY_LAYER = Layer.UI;

    /** The layer for leaf GameObjects. */
    private static final int LEAF_LAYER = -50;

    /** The Avatar instance. */
    private Avatar avatar;

    /** The list of Tree instances. */
    private List<Tree> treeList;

    /** Indicates if the avatar is jumping. */
    private boolean isAvatarJumping;

    /** The Terrain instance. */
    private Terrain terrain;

    /**
     * Initializes the sky GameObject.
     *
     * @param windowController The window controller.
     */
    private void initialiseSky(WindowController windowController) {
        // Sky
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, SKY_LAYER);
    }

    /**
     * Initializes the terrain GameObjects.
     *
     * @param windowController The window controller.
     */
    private void initialiseTerrain(WindowController windowController){
        // Terrain
        terrain = new Terrain(windowController.getWindowDimensions(), DEFAULT_SEED);
        List<Block> ground = terrain.createInRange(0,(int)windowController.getWindowDimensions().x());
        for(Block e : ground) {
            gameObjects().addGameObject(e, GROUND_LAYER);
        }
    }

    /**
     * Initializes the night GameObject.
     *
     * @param windowController The window controller.
     */
    private void initialiseNight(WindowController windowController) {
        // Night
        GameObject night = Night.create(windowController.getWindowDimensions(), NIGHT_CYCLE);
        gameObjects().addGameObject(night, NIGHT_LAYER);
    }

    /**
     * Initializes the sun and halo GameObjects.
     *
     * @param windowController The window controller.
     */
    private void initialiseSunAndHalo(WindowController windowController) {
        // sun
        GameObject sun = Sun.create(windowController.getWindowDimensions(), SUN_CYCLE);
        gameObjects().addGameObject(sun, SKY_LAYER);
        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, HALO_LAYER);
    }

    /**
     * Initializes the avatar and energy GameObjects.
     *
     * @param windowController The window controller.
     * @param inputListener    The input listener.
     * @param imageReader      The image reader.
     */
    private void initialiseAvatarAndEnergy(WindowController windowController,
                                           UserInputListener inputListener, ImageReader imageReader) {
        //Avatar
        Vector2 pos = new Vector2(windowController.getWindowDimensions().x()-AVATAR_INITALISE_BUFFER_X,
                windowController.getWindowDimensions().y()*AVATAR_INITALISE_BUFFER_Y);
        avatar = new Avatar(pos, inputListener, imageReader);
        gameObjects().addGameObject(avatar,AVATAR_LAYER);
        // Energy
        GameObject energy = Energy.create(avatar);
        gameObjects().addGameObject(energy, ENERGY_LAYER);
    }

    /**
     * Initializes the trees GameObjects.
     *
     * @param windowController The window controller.
     */
    private void initialiseTrees(WindowController windowController) {
        // Trees
        Flora flora = new Flora(terrain::getHeight);
        treeList = flora.createInRange(0,(int)windowController.getWindowDimensions().x());
        for(Tree tree : treeList) {
            GameObject stem = tree.getStem();
            gameObjects().addGameObject(stem, GROUND_LAYER);
            GameObject[][] alva = tree.getAlva();
            GameObject[][] fruits = tree.getFruits();
            for(int i=0; i<alva.length; i++) {
                for(int j=0; j<alva[0].length; j++) {
                    if(alva[i][j] != null) {
                        gameObjects().addGameObject(alva[i][j], LEAF_LAYER);
                    }
                    if(fruits[i][j] != null) {
                        gameObjects().addGameObject(fruits[i][j], AVATAR_LAYER);
                    }
                }
            }
        }
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        initialiseSky(windowController);
        initialiseTerrain(windowController);
        initialiseNight(windowController);
        initialiseSunAndHalo(windowController);
        initialiseAvatarAndEnergy(windowController, inputListener, imageReader);
        initialiseTrees(windowController);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(avatar.isJumping()){
            isAvatarJumping = true;
            for(Tree e : treeList){
                e.heJumpedApp();
            }
        }
        else if(avatar.isOnGround() && isAvatarJumping){
            isAvatarJumping = false;
            for(Tree e : treeList){
                e.heIsOnGround();
            }
        }
    }

    /**
     * The entry point of the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
