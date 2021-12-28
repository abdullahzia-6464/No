package SineOfMadness;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;

// An abstract class for the attacks Arc, Lead and Web
public abstract class Projectile{

    // Vector objects with y and x values to specify vector quantities
    private Vector2f position;  // Position at which projectile is to start
    private Vector2f velocity;  // The speed and direction at which it is to go

    private float lifetime;  // How long the projectile has stayed active

    private boolean isActive = true;  // Whether projectile is active or not

    private Animation projectileL, projectileR;  // Animations for projectile

    private boolean isRight;  // Whether the projectile is to be facing right or not

    private int yOffset, xOffset;  // To specify the distance at which the projectile begins from the character

    // CONSTRUCTOR
    Projectile(Vector2f position, Vector2f velocity, boolean isRight,
               Image[] projectileRight, Image[] projectileLeft,
               int yOffset, int xOffset) throws SlickException{
        this.position = position;
        this.velocity = velocity;
        this.isRight = isRight;

        // Initialise projectile animation
        projectileR = new Animation(projectileRight, CONSTANT.ANIMATION_DURATION_2, false);
        projectileL = new Animation(projectileLeft, CONSTANT.ANIMATION_DURATION_2, false);

        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    public void render(GameContainer GC, Graphics g, StateBasedGame sbg) throws SlickException {

        // Render projectile
        if(isRight){
            projectileR.draw(position.getX() + xOffset, position.getY() + yOffset);
        }
        else {
            projectileL.draw(position.getX() + xOffset, position.getY() + yOffset);
        }
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    public void update(GameContainer gc, int delta, StateBasedGame sbg) throws SlickException {

        // Only update the projectile if it is active
        if (isActive){
            Vector2f actualVelocity = velocity.copy();
            actualVelocity.scale(delta/200.0F);  // Scale it down so it represents speed more accurately in game
            position.add(actualVelocity);  // Add the velocity to the position of the projectile

            lifetime += delta;  // Increment lifetime

            // If projectile has exceeded the constant projectile lifetime, deactivate it
            if (lifetime > CONSTANT.PROJECTILE_LIFETIME)
                isActive = false;
        }
    }

    // Getter & setter isActive
    public boolean isActive(){
        return isActive;
    }
    public void setActive(boolean active){
        this.isActive = false;
    }

    // Getter for position
    public Vector2f getPosition(){
        return position;
    }
}
