package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Represents the avatar GameObject in the game world.
 */
public class Avatar extends GameObject {
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final float MAX_ENERGY = 100;
    private static final float MIN_ENERGY = 0;
    private static final float MOVE_ENERGY_LOSS = 0.5f;
    private static final float ANIMATION_TIME = 0.5f;
    private static final float JUMP_ENERGY_LOSS = 10;
    private static final float ADD_FRUIT_ENERGY = 10;
    private static final String PRESENT = "%";
    private static final String FRUIT = "fruit";
    private static final String AVATAR = "Avatar";
    private static final String[] jumpImageArray =
            {"assets/jump_0.png", "assets/jump_1.png", "assets/jump_2.png", "assets/jump_3.png"};
    private static final String[] moveImageArray =
            {"assets/run_0.png", "assets/run_1.png", "assets/run_2.png",
                    "assets/run_3.png", "assets/run_4.png", "assets/run_5.png"};
    private static final String[] idleImageArray =
            {"assets/idle_0.png", "assets/idle_1.png", "assets/idle_2.png", "assets/idle_3.png"};
    private static final String INITIALISE_IMAGE_PATH = "assets/idle_0.png";
    private static final int FACTOR = 50;
    private static final int ONE = 1;
    private float energy;
    private UserInputListener inputListener;
    private Renderable jump;
    private Renderable move;
    private Renderable idle;
    private boolean isJumping;

    /**
     * Constructs an Avatar object with the specified position, input listener, and image reader.
     *
     * @param pos          The position of the avatar.
     * @param inputListener The input listener to handle user input.
     * @param imageReader  The image reader to load avatar images.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(pos, Vector2.ONES.mult(FACTOR),
                imageReader.readImage(INITIALISE_IMAGE_PATH, true));
        // Others
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.energy = MAX_ENERGY;
        this.renderer().setRenderable(idle);
        // Animation
        jump = new AnimationRenderable(jumpImageArray, imageReader, true, ANIMATION_TIME);
        move = new AnimationRenderable(moveImageArray, imageReader, true, ANIMATION_TIME);
        idle = new AnimationRenderable(idleImageArray, imageReader, true, ANIMATION_TIME);
        this.setTag(AVATAR);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        isJumping = false;
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && energy > MIN_ENERGY) {
            xVel -= VELOCITY_X;
            this.renderer().setRenderable(move);
            this.renderer().setIsFlippedHorizontally(true);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && energy > MIN_ENERGY) {
            xVel += VELOCITY_X;
            this.renderer().setRenderable(move);
            this.renderer().setIsFlippedHorizontally(false);
        }
        transform().setVelocityX(xVel);
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                getVelocity().y() == 0 && energy >= JUMP_ENERGY_LOSS) {
            transform().setVelocityY(VELOCITY_Y);
            energy -= JUMP_ENERGY_LOSS;
            this.renderer().setRenderable(jump);
            isJumping = true;
        }
        if (getVelocity().y() == 0) {
            if (getVelocity().x() == 0) {
                if (energy >= MAX_ENERGY - ONE) {
                    energy = MAX_ENERGY;
                } else {
                    energy++;
                    this.renderer().setRenderable(idle);
                }
            } else {
                energy -= MOVE_ENERGY_LOSS;
            }
        } else {
            if (getVelocity().x() != 0) {
                energy -= MOVE_ENERGY_LOSS;
            } else {
                this.renderer().setRenderable(jump);
            }
        }
    }

    /**
     * Gets the energy level of the avatar.
     *
     * @return The energy level of the avatar as a formatted string.
     */
    public String getEnergy() {
        return String.valueOf(energy) + PRESENT;
    }

    /**
     * Increases the energy level of the avatar by the specified amount.
     *
     * @param num The amount by which to increase the energy level.
     */
    private void addEnergy(float num) {
        energy += num;
        if (energy > MAX_ENERGY) {
            energy = MAX_ENERGY;
        }
    }

    /**
     * Checks if the avatar is currently jumping.
     *
     * @return True if the avatar is jumping, false otherwise.
     */
    public boolean isJumping() {
        return isJumping;
    }

    /**
     * Checks if the avatar is currently on the ground.
     *
     * @return True if the avatar is on the ground, false otherwise.
     */
    public boolean isOnGround() {
        if (!inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(FRUIT)) {
            addEnergy(ADD_FRUIT_ENERGY);
        }
    }
}
