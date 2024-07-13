package pepse.world.trees;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

/**
 * Represents a tree GameObject in the game world.
 */
public class Tree {
    private static final float BLOCK_SIZE = 30;
    private static final int ALVA_SIZE = 8;
    private static final int HALF_ALVA_SIZE = ALVA_SIZE / 2;
    private GameObject stem;
    private GameObject[][] alva = new GameObject[ALVA_SIZE][ALVA_SIZE];
    private GameObject[][] fruits = new GameObject[ALVA_SIZE][ALVA_SIZE];
    private Random random = new Random();
    private static final int RANDOM_LIMIT = 10;
    private static final int FRUIT_PRE = 0;
    private static final int NULL_PRE = 6;
    private static final String FRUIT = "fruit";

    /**
     * Constructs a Tree object with the specified coordinates.
     *
     * @param x The x-coordinate of the tree.
     * @param y The y-coordinate of the tree.
     */
    public Tree(float x, float y) {
        stem = Stem.create(new Vector2(x, y));
        float topLeftX = stem.getCenter().x() - (HALF_ALVA_SIZE * BLOCK_SIZE);
        float topLeftY = stem.getTopLeftCorner().y() - (HALF_ALVA_SIZE * BLOCK_SIZE);
        for (int i = 0; i < ALVA_SIZE; i++) {
            for (int j = 0; j < ALVA_SIZE; j++) {
                alva[i][j] = getRandomLeaf(new Vector2(topLeftX + (i * BLOCK_SIZE),
                        topLeftY + (j * BLOCK_SIZE)));
                fruits[i][j] = getRandomFruit(new Vector2(topLeftX + (i * BLOCK_SIZE),
                        topLeftY + (j * BLOCK_SIZE)));
            }
        }

    }

    private GameObject getRandomLeaf(Vector2 coordinate) {
        int num = random.nextInt(RANDOM_LIMIT);
        if (num >= NULL_PRE) {
            return null;
        }
        return Leaf.create(coordinate);
    }

    private GameObject getRandomFruit(Vector2 coordinate) {
        int num = random.nextInt(RANDOM_LIMIT);
        if (num <= FRUIT_PRE) {
            Renderable renderable = new OvalRenderable(Color.RED);
            GameObject fruit = new Fruit(coordinate, new Vector2(BLOCK_SIZE, BLOCK_SIZE), renderable);
            fruit.setTag(FRUIT);
            return fruit;
        }
        return null;
    }

    /**
     * Retrieves the stem of the tree.
     *
     * @return The stem GameObject of the tree.
     */
    public GameObject getStem() {
        return stem;
    }

    /**
     * Retrieves the array of leaf GameObjects of the tree.
     *
     * @return A 2D array of leaf GameObjects of the tree.
     */
    public GameObject[][] getAlva() {
        return alva;
    }

    /**
     * Retrieves the array of fruit GameObjects of the tree.
     *
     * @return A 2D array of fruit GameObjects of the tree.
     */
    public GameObject[][] getFruits() {
        return fruits;
    }

    /**
     * Updates the appearance of the tree when the character jumps on it.
     */
    public void heJumpedApp() {
        Stem.heJumpedApp(stem);
        for (int i = 0; i < ALVA_SIZE; i++) {
            for (int j = 0; j < ALVA_SIZE; j++) {
                if (alva[i][j] != null) {
                    Leaf.transitionAngle90D(alva[i][j]);
                }
                if (fruits[i][j] != null) {
                    (fruits[i][j]).renderer().setRenderable(new OvalRenderable(Color.ORANGE));
                }
            }
        }
    }

    /**
     * Updates the appearance of the tree when the character is on the ground.
     */
    public void heIsOnGround() {
        Stem.heIsOnGround(stem);
        for (int i = 0; i < ALVA_SIZE; i++) {
            for (int j = 0; j < ALVA_SIZE; j++) {
                if (fruits[i][j] != null) {
                    (fruits[i][j]).renderer().setRenderable(new OvalRenderable(Color.RED));
                }
            }
        }
    }
}
