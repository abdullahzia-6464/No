package SineOfMadness;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

// Class for the wasp enemy.
public class Beethoven extends Character {

    // Animations for Beethoven
    private Animation beethoven, beethovenRight, beethovenLeft;

    private boolean flyingUp;  // Equivalent to isRight but for up and down

    private final boolean moveVertically;  // Parameter to be passed when creating object. Specifies if Beethoven is to move vertically or horizontally

    // Bucky object
    private final Bucky bucky;

    // Coordinates for spawning and moving
    private final float spawnXcoordinate;
    private final float spawnYcoordinate;
    private final float pathUpperBound;
    private final float pathLowerBound;

    // Hitbox for collision detection
    private Rectangle beethovenHitbox;

    // Sound for SFX
    private Sound hitBucky;

    // CONSTRUCTOR
    public Beethoven(Bucky bucky, float spawnXcoordinate, float spawnYcoordinate,
                     float pathLowerBound, float pathUpperBound, float moveSpeed, boolean moveVertically) {
        this.bucky = bucky;
        this.spawnXcoordinate = spawnXcoordinate;
        this.spawnYcoordinate = spawnYcoordinate;
        this.pathLowerBound = pathLowerBound;
        this.pathUpperBound = pathUpperBound;
        this.setMoveSpeed(moveSpeed);
        this.moveVertically = moveVertically;
    }

    // INITIALISATION METHOD
    public void init(GameContainer GC) throws SlickException {
        // Initialising image arrays
        Image[] flyingR = new Image[]{new Image("res/WaspRight1.png"), new Image("res/WaspRight2.png")};
        Image[] flyingL = new Image[]{new Image("res/WaspLeft1.png"), new Image("res/WaspLeft2.png")};

        hitBucky = new Sound("res/hit.wav"); // Initialising sound

        // Initialising Animations
        beethovenRight = new Animation(flyingR, CONSTANT.ANIMATION_DURATION_1, true);
        beethovenLeft = new Animation(flyingL, CONSTANT.ANIMATION_DURATION_1, true);
        beethoven = beethovenRight;

        flyingUp = true;
        setEntityDimensions(new float[]{70, 100});

        setHealth(12F);  // Initialise health

        // Initialise coordinates
        setXposition(spawnXcoordinate);
        setYposition(spawnYcoordinate);

        // Initialise hitbox
        beethovenHitbox = new Rectangle(getXposition(), getYposition(), 70, 100);
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    public void render(GameContainer GC, Graphics g, StateBasedGame sbg) throws SlickException {
        if (isAlive()) {  // Only draw Beethoven if alive.
            beethoven.draw(getXposition(), getYposition());
            beethovenHitbox.setY(getYposition());
            beethovenHitbox.setX(getXposition());
        }
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    public void update(GameContainer gc, int delta, StateBasedGame sbg) throws SlickException {

        updateRange();

        boolean chase = false;  // If bucky is in range in level 2, set chase to true and follow him
        timePassedSinceDamage += (System.currentTimeMillis() - timeOfDamage);  // Update time passed since last damage

        // If bucky collides with beethoven, decrease his health
        if (beethovenHitbox.intersects(Bucky.getBuckyHitbox()) && isAlive() && timePassedSinceDamage >= 200000) {
            timePassedSinceDamage = 0;
            timeOfDamage = System.currentTimeMillis();
            Bucky.setBuckyHealth(Bucky.getBuckyHealth() - 1);
            if (!hitBucky.playing())
                hitBucky.play();
            return;
        }

        // If bucky's attack hits beethoven decrease his health
        for (Arc arc : bucky.arcs) {
            if (arc.isActive() && arc.getArcHitBox().intersects(beethovenHitbox) && isAlive()) {
                arc.setActive(false);
                setHealth(getHealth() - 1);
            }
        }

        // Check for health being less than or equal to zero
        if (getHealth() <= 0) {
            setAlive(false);
        }

        // If bucky comes in range on level 2, chase him
        if (bucky.getYposition() > 390) {
            chase = true;
        }
        if (bucky.getYposition() > 450){
            chase = false;
        }

        if (moveVertically) {  // Updating position and animation if beethoven is moving vertically
            if (flyingUp) {
                setYposition(getYposition() - (float) delta * getMoveSpeed());
                if (getYposition() < pathUpperBound) {
                    flyingUp = false;
                }
            } else {
                setYposition(getYposition() + (float) delta * getMoveSpeed());
                if (getYposition() > pathLowerBound) {
                    flyingUp = true;
                }
            }
        } else {  // Updating position and animation if beethoven is moving horizontally
            // If chasing bucky
            if (chase) {
                if (isRight()) {
                    setXposition(getXposition() + (float) delta * getMoveSpeed());
                    if (getXposition() > bucky.getXposition()) {
                        setRight(false);
                    }
                    beethoven = beethovenRight;
                } else {
                    setXposition(getXposition() - (float) delta * getMoveSpeed());
                    if (getXposition() < bucky.getXposition()) {
                        setRight(true);
                    }
                    beethoven = beethovenLeft;
                }
            }
            // If not chasing Bucky
            else if (isRight()) {
                setXposition(getXposition() + (float) delta * getMoveSpeed());
                if (getXposition() > pathUpperBound) {
                    setRight(false);
                }
                beethoven = beethovenRight;
            } else {
                setXposition(getXposition() - (float) delta * getMoveSpeed());
                if (getXposition() < pathLowerBound) {
                    setRight(true);
                }
                beethoven = beethovenLeft;
            }
        }
    }
}