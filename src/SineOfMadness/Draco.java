package SineOfMadness;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;


// The lizard looking enemy
public class Draco extends Character{
    // Animations
    private Animation Draco;
    private Animation movingRight;
    private Animation movingLeft;
    private Animation dashingRight;
    private Animation dashingLeft;

    // Hitbox
    protected Rectangle dracoHitbox;

    // Bucky object
    private final Bucky bucky;

    // Coordinates for spawning and moving
    private final float spawnXcoordinate;
    private final float spawnYcoordinate;
    private final float pathUpperBound;
    private final float pathLowerBound;
    private final float dashLowerBound;
    private final float dashUpperBound;

    // SFX
    Sound hitBucky;

    // CONSTRUCTOR
    public Draco(Bucky bucky, float spawnXcoordinate, float spawnYcoordinate, float pathLowerBound, float pathUpperBound, float dashLowerBound, float dashUpperBound, float moveSpeed) {
        this.bucky = bucky;
        this.spawnXcoordinate = spawnXcoordinate;
        this.spawnYcoordinate = spawnYcoordinate;
        this.pathLowerBound = pathLowerBound;
        this.pathUpperBound = pathUpperBound;
        this.dashLowerBound = dashLowerBound;
        this.dashUpperBound = dashUpperBound;
        setMoveSpeed(moveSpeed);
    }

    // INITIALISATION METHOD
    public void init(GameContainer GC) throws SlickException {
        // Initialising image arrays
        Image[] walkR = new Image[]{new Image("res/DracoRight1.png"), new Image("res/DracoRight2.png")};
        Image[] walkL = new Image[]{new Image("res/DracoLeft1.png"), new Image("res/DracoLeft2.png")};
        Image[] dashR = new Image[]{new Image("res/DracoRight1.png"), new Image("res/DracoRightSmoke.png")};
        Image[] dashL = new Image[]{new Image("res/DracoLeft1.png"), new Image("res/DracoLeftSmoke.png")};

        // Initialising Animations
        dashingRight = new Animation(dashR, CONSTANT.ANIMATION_DURATION_1_FAST, true);
        dashingLeft = new Animation(dashL, CONSTANT.ANIMATION_DURATION_1_FAST, true);
        movingRight = new Animation(walkR, CONSTANT.ANIMATION_DURATION_1, true);
        movingLeft = new Animation(walkL, CONSTANT.ANIMATION_DURATION_1, true);
        Draco = movingRight;

        // Initialising SFX
        hitBucky = new Sound("res/hit.wav");

        setHealth(20f); // Initialising health

        // Initialising dimensions and coordinates
        setEntityDimensions(new float []{94, 100});
        setXposition(spawnXcoordinate);  // Initial position of draco
        setYposition(spawnYcoordinate);

        // Initialising hitbox
        dracoHitbox = new Rectangle(getXposition(), getYposition(), 94 , 94);
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    public void render(GameContainer GC, Graphics g, StateBasedGame sbg) throws SlickException {
        if (isAlive()) {  // Only draw Draco if he is alive
            Draco.draw(getXposition(), getYposition());
            dracoHitbox.setX(getXposition());
            dracoHitbox.setY(getYposition());
        }
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    public void update(GameContainer gc, int delta, StateBasedGame sbg) throws SlickException {

        updateRange();
        timePassedSinceDamage += (System.currentTimeMillis() - timeOfDamage);  // Incrementing time passed since last damage

        // Collision detection
        if (dracoHitbox.intersects(Bucky.getBuckyHitbox()) && isAlive() && timePassedSinceDamage > 200000){
            timePassedSinceDamage = 0;
            timeOfDamage = System.currentTimeMillis();
            Bucky.setBuckyHealth(Bucky.getBuckyHealth() - 1);
            if (!hitBucky.playing()) hitBucky.play();
            return;
        }

        // Decrementing health if Bucky's arc hits draco
        for (Arc arc : bucky.arcs) {
            if (arc.isActive() && arc.getArcHitBox().intersects(dracoHitbox) && isAlive()) {
                arc.setActive(false);
                setHealth(getHealth() - 1);
            }
        }

        // Checking to see if health is below 1
        if (getHealth() <= 0){
            setAlive(false);
        }

        // Automatic movement of Draco between bounds + updating animations
        if (isRight()) {  // If draco is facing right
            if ( getXposition() > dashLowerBound && getXposition() < dashUpperBound){  // If draco is in dash bounds
                Draco = dashingRight;
                setXposition(getXposition() + (float) (delta * getMoveSpeed() * 3.5));
            }
            else {  // If draco is to move at normal speed
                Draco = movingRight;
                setXposition(getXposition() + (float) delta * getMoveSpeed());
                if (getXposition() > pathUpperBound) {
                    setRight(false);
                }
            }
        }
        else {  // If draco is facing left
            if (getXposition() > dashLowerBound && getXposition() < dashUpperBound){  // If draco is in dash bounds
                Draco = dashingLeft;
                setXposition(getXposition() - (float) (delta * getMoveSpeed() * 3.5));
            }
            else {  // If draco is to move at normal speed
                Draco = movingLeft;
                setXposition(getXposition() - (float) delta * getMoveSpeed());
                if (getXposition() < pathLowerBound) {
                    setRight(true);
                }
            }
        }
    }
}