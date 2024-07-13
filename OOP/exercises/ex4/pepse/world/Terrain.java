package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.TreeMap;

/**
 * Represents the terrain in the game world.
 */
public class Terrain {

    /** The base color of the ground. */
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    /** The depth of the terrain. */
    private static final int TERRAIN_DEPTH = 20;

    /** The default ratio of ground height to window height. */
    private static final float DEFAULT_GROUND_HEIGHT_RATIO = (float) 2/3;

    /** The factor for generating noise. */
    private static final int NOISE_FACTOR = 7;

    /** The constant to add when calculating the number of blocks. */
    private static final int ADD_1 = 1;

    /** The tag for ground GameObjects. */
    private static final String GROUND = "ground";

    /** The height of the ground at x = 0. */
    private final float groundHeightAtX0;

    /** The NoiseGenerator object for terrain generation. */
    private final NoiseGenerator noiseGenerator;

    /** TreeMap to store terrain heights. */
    private TreeMap<Float, Float> terrainHeight = new TreeMap<Float,Float>();

    /**
     * Constructs a Terrain object with the given window dimensions and seed.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed             The seed for terrain generation.
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        groundHeightAtX0 = windowDimensions.y() * DEFAULT_GROUND_HEIGHT_RATIO;
        noiseGenerator = new NoiseGenerator(seed, ((int) groundHeightAtX0));
    }

    /**
     * Calculates the height of the ground at a given x-coordinate.
     *
     * @param x The x-coordinate.
     * @return The height of the ground at the specified x-coordinate.
     */
    public float groundHeightAt(float x) {
        double noiseAtX = noiseGenerator.noise(x, Block.SIZE * NOISE_FACTOR);
        float y = (float) (groundHeightAtX0 + noiseAtX);
        return y;
    }

    /**
     * Creates a list of ground blocks within the specified range of x-coordinates.
     *
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     * @return The list of ground blocks.
     */
    public List<Block> createInRange(int minX, int maxX) {
        // Initialize
        ArrayList<Block> blockArrayList = new ArrayList<Block>();
        RectangleRenderable renderable =
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        // Get the X and Y
        int numberOfBlocks = (int) (Math.floor(Math.abs(maxX - minX)) / Block.SIZE) + ADD_1;
        int startPoint = maxX - (Block.SIZE * numberOfBlocks);
        for (int i = startPoint; i < maxX; i += Block.SIZE) {
            int coordinateYAtX = (int) Math.floor(groundHeightAt(i) / Block.SIZE) * Block.SIZE;
            terrainHeight.put((float) i, (float) coordinateYAtX);
            for (int j = coordinateYAtX; j <= (coordinateYAtX + (Block.SIZE * TERRAIN_DEPTH));
                 j += Block.SIZE) {
                Block ground = new Block(new Vector2(i, j), renderable);
                ground.setTag(GROUND);
                blockArrayList.add(ground);
            }
        }
        return blockArrayList;
    }

    /**
     * Gets the height of the terrain at a given x-coordinate.
     *
     * @param x The x-coordinate.
     * @return The height of the terrain at the specified x-coordinate.
     */
    public float getHeight(float x) {
        return terrainHeight.get(terrainHeight.lowerKey(x));
    }
}
