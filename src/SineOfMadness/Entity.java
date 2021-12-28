package SineOfMadness;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


// This class has all the basic things that everything on the screen (an entity) will have
public abstract class Entity{

    private float Xposition;  // Player and enemy X coordinates.
    private float Yposition;  // Player and enemy Y coordinates.
    private float[] entityDimensions = new float[2];  // The dimensions of the .png image of the entity

    private float xRange, yRange;  // The upper bounds for x and y coordinates taken up by the entity


    public abstract void init(GameContainer GC) throws SlickException;  // Initialisation method
    public abstract void render(GameContainer GC, Graphics g,StateBasedGame sbg) throws SlickException;  // Rendering method
    public abstract void update(GameContainer gc, int delta,StateBasedGame sbg) throws SlickException;  // Updating method (the loop)


    // Updating the upper bound for x and y coordinates of the space taken up by the entity
    public void updateRange(){
        xRange = Xposition + entityDimensions[0];
        yRange = Yposition + entityDimensions[1];
    }

    // GETTERS AND SETTERS

    // xPosition
    public void setXposition(float xPosition) {
        this.Xposition = xPosition;
    }

    public float getXposition() {
        return Xposition;
    }

    // yPosition
    public void setYposition(float yPosition) {
        this.Yposition = yPosition;
    }

    public float getYposition() {
        return Yposition;
    }

    // Ranges (don't need setters, have updateRange() method)
    public float getXRange() {
        return xRange;
    }

    public float getYRange() {
        return yRange;
    }

    // EntityDimensions
    public void setEntityDimensions(float[] entityDimensions) {
        this.entityDimensions = entityDimensions;
    }

    public float[] getEntityDimensions() {
        return entityDimensions;
    }


}
