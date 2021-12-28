package SineOfMadness;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

// Karen's (the boss) attack
public class Web extends Projectile{

    // Hitbox for collision detection
    private Rectangle webHitbox;

    // CONSTRUCTOR
    Web(Vector2f position, Vector2f speed, boolean isRight) throws SlickException {
        super(position, speed, isRight,
                new Image[]{new Image("res/WebAttack.png")}, new Image[]{new Image("res/WebAttack.png")},
                46, 130);
        webHitbox = new Rectangle(position.getX(), position.getY(), 70, 20);
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    public void render(GameContainer gc, Graphics g, StateBasedGame sbg) throws SlickException {
        g.setColor(Color.black);
        super.render(gc, g, sbg);
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    public void update(GameContainer gc, int delta, StateBasedGame sbg) throws SlickException {
        super.update(gc, delta, sbg);

        // Update hitbox coordinates
        webHitbox.setX(getPosition().getX() + 130);
        webHitbox.setY(getPosition().getY() + 46);
    }

    // Getter for hitbox
    public Rectangle getWebHitbox() {
        return webHitbox;
    }
}
