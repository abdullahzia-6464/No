package SineOfMadness;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

// To make different platforms in the levels
public class Platform extends Entity {

    // Image for platform
    private Image platform;

    // The dimensions to scale the platform to
    private final int scaledWidth;
    private final int scaledHeight;

    // Hitbox for the rectangle
    private Rectangle platformBounds;

    // CONSTRUCTOR
    Platform(Image platform, float Xposition, float Yposition, int scaledWidth, int scaledHeight){
        this.platform = platform;
        setXposition(Xposition);
        setYposition(Yposition);
        this.scaledWidth = scaledWidth;
        this.scaledHeight = scaledHeight;
    }

    // INITIALISATION METHOD
    @Override
    public void init(GameContainer GC) throws SlickException {
        platform = platform.getScaledCopy(scaledWidth, scaledHeight);
        setEntityDimensions(new float[]{scaledWidth, scaledHeight});  // Sending in scaled dimensions because these are the actual dimensions that it's covering
        updateRange();
        platformBounds = new Rectangle(getXposition(), getYposition(), scaledWidth, scaledHeight);
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    @Override
    public void render(GameContainer GC, Graphics g, StateBasedGame sbg) throws SlickException {
        platform.draw(getXposition(), getYposition());  // Starting point of platform (its top left corner)
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    @Override
    public void update(GameContainer gc, int delta , StateBasedGame sbg) throws SlickException {
        updateRange();
    }

    // Getter for platform hitbox. No setter required as it is final
    public Rectangle getPlatformBounds() {
        return platformBounds;
    }
}
