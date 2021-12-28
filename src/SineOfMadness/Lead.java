package SineOfMadness;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;

// Alfred's attack (the plus shaped enemy)
public class Lead extends Projectile{

    // Hitbox for collision detection
    private Rectangle leadHitBox;

    // CONSTRUCTOR
    Lead(Vector2f position, Vector2f speed, boolean isRight) throws SlickException{
        super(position, speed, isRight,
                new Image[]{new Image("res/Lead.png")}, new Image[]{new Image("res/Lead.png")},
                46, 130);
        leadHitBox = new Rectangle(position.getX(), position.getY(), 17, 20);
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    public void render(GameContainer gc, Graphics g, StateBasedGame sbg) throws SlickException {

        super.render(gc, g, sbg);

        // Initialising hitbox
        leadHitBox.setX(getPosition().getX() + 130);
        leadHitBox.setY(getPosition().getY() + 46);
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    public void update(GameContainer gc, int delta, StateBasedGame sbg) throws SlickException {
        super.update(gc, delta, sbg);
    }

    // GETTER FOR HITBOX
    public Rectangle getLeadHitBox() {
        return leadHitBox;
    }
}
