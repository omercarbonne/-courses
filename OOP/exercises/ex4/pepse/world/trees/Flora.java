package pepse.world.trees;

import pepse.world.Block;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Represents the flora (plants, trees, etc.) generation in the world.
 */
public class Flora {
    private static Random random = new Random();
    private static final int RANDOM_LIMIT = 10;
    private static final int PUT_TREE = 0;
    private static final int ADD_1 = 1;
    private Function<Float, Float> func;

    /**
     * Constructs a Flora object with the specified function for generating plant positions.
     *
     * @param func The function used to generate plant positions.
     */
    public Flora(Function<Float, Float> func) {
        this.func = func;
    }

    /**
     * Creates a list of trees within the specified range.
     *
     * @param minX The minimum X-coordinate for tree generation.
     * @param maxX The maximum X-coordinate for tree generation.
     * @return A list of Tree objects generated within the specified range.
     */
    public List<Tree> createInRange(int minX, int maxX) {
        List<Tree> treesList = new ArrayList<Tree>();
        // Calculate the number of blocks within the specified range
        int numberOfBlocks = (int) (Math.floor(Math.abs(maxX - minX)) / Block.SIZE) + ADD_1;
        // Calculate the start point for tree generation
        int startPoint = maxX - (Block.SIZE * numberOfBlocks);
        // Iterate through each block within the range
        for (int i = startPoint; i <= maxX; i += Block.SIZE) {
            // Generate a random number to decide whether to put a tree or not
            int num = random.nextInt(RANDOM_LIMIT);
            if (num == PUT_TREE) {
                // Calculate the Y-coordinate for the tree using the provided function
                float y = func.apply((float) i) + Block.SIZE;
                // Create a new Tree object and add it to the list
                treesList.add(new Tree(i, y));
                // Increment the loop variable to skip the next block
                i += Block.SIZE;
            }
        }
        return treesList;
    }
}