package SineOfMadness;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;

// This class handles the player's attack move.
public class Arc extends Projectile{

    private Rectangle arcHitBox; // Hitbox for collision detection

    // CONSTRUCTOR
    Arc(Vector2f position, Vector2f speed, boolean isRight) throws SlickException{
        super(position, speed, isRight,
                new Image[]{new Image("res/ArcRight.png")}, new Image[]{new Image("res/ArcLeft.png")},
                -25, 0);
        arcHitBox = new Rectangle(position.getX(), position.getY(), 30, 60);
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    public void render(GameContainer gc, Graphics g, StateBasedGame sbg) throws SlickException {
        super.render(gc, g, sbg);
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    public void update(GameContainer gc, int delta, StateBasedGame sbg) throws SlickException {
        super.update(gc, delta, sbg);

        arcHitBox.setX(getPosition().getX());
        arcHitBox.setY(getPosition().getY() - 12);
    }

    public Rectangle getArcHitBox() {
        return arcHitBox;
    }
}
