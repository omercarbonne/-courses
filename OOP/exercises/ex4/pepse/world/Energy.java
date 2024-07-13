package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the energy display in the game world.
 */
public class Energy {

    /** The text indicating full energy. */
    private static final String FULL_ENERGY = "100%";

    /** The size of the energy text. */
    private static final float TEXT_SIZE = 40;

    /** The tag for the energy text GameObject. */
    private static final String ENERGY_TEXT = "energyText";

    /**
     * Creates a GameObject representing the energy display based on the given Avatar object.
     *
     * @param avatar The avatar object to display energy for.
     * @return The GameObject representing the energy display.
     */
    public static GameObject create(Avatar avatar){
        TextRenderable renderable = new TextRenderable(FULL_ENERGY);
        renderable.setColor(Color.BLACK);
        GameObject energy = new GameObject(Vector2.ZERO, new Vector2(TEXT_SIZE, TEXT_SIZE), renderable);
        energy.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        energy.setTag(ENERGY_TEXT);
        energy.addComponent((deltaTime) -> {
            renderable.setString(avatar.getEnergy());
        });
        return energy;
    }
}
