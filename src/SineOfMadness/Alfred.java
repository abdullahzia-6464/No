package SineOfMadness;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Iterator;
import java.util.LinkedList;

// This class is for the mini boss. The plus shaped enemy.
public class Alfred extends Character{
    // Animations and Images for graphics
    private Animation alfredAnimation; // The current animation alfred is set to
    private Animation movingRight; // Animation to set alfredAnimation to when moving right
    private Animation movingLeft; // Animation to set alfredAnimation to when moving left
    private Animation attacking; // Animation to set alfredAnimation to when attacking
    private Image healthPickup; // The image for the health that Alfred drops when killed

    private boolean isStatic; // Set to true when alfred is attacking so that he stops moving. Otherwise set to false
    private boolean healthPicked; // To check whether Bucky has picked up the health or not

    private long timePassedSinceProjectile = 200000; // The time passed since Alfred threw the previous projectile
    private long timeOfProjectile = System.currentTimeMillis(); // The time that the projectile was thrown

    // HITBOXES FOR COLLISION DETECTION
    private Rectangle alfredHitbox;
    private Rectangle healthHitbox;

    // BUCKY OBJECT
    private final Bucky bucky;

    // LinkedList to keep track of Alfred's projectiles
    private LinkedList<Lead> leads;

    // Coordinates for spawning Alfred
    private final float spawnXcoordinate;
    private final float spawnYcoordinate;

    // X coordinates between which Alfred is supposed to move
    private final float pathUpperBound;
    private final float pathLowerBound;

    // The speed at which Alfred moves
    private final float moveSpeed;

    // Sounds for SFX
    private Sound fire;
    private Sound hitBucky;
    private Sound healthPowerup;

    // CONSTRUCTOR
    public Alfred(Bucky bucky, float spawnXcoordinate, float spawnYcoordinate, float pathLowerBound, float pathUpperBound, float moveSpeed) {
        this.bucky = bucky;
        this.spawnXcoordinate = spawnXcoordinate;
        this.spawnYcoordinate = spawnYcoordinate;
        this.pathLowerBound = pathLowerBound;
        this.pathUpperBound = pathUpperBound;
        this.moveSpeed = moveSpeed;
    }


    // INITIALISATION METHOD
    public void init(GameContainer GC) throws SlickException {
        // INITIALISING THE IMAGE ARRAYS
        Image[] walkR = new Image[]{new Image("res/AlfredRight1.png"), new Image("res/AlfredRight2.png"),
                new Image("res/AlfredRight1.png"), new Image("res/AlfredRight3.png")};
        Image[] walkL = new Image[]{new Image("res/AlfredLeft1.png"), new Image("res/AlfredLeft2.png"),
                new Image("res/AlfredLeft1.png"), new Image("res/AlfredLeft3.png" ),};
        Image[] attack = new Image[]{new Image("res/AlfredRight1.png"), new Image("res/AlfredAttack2.png"), new Image("res/AlfredAttack1.png")};

        healthPickup = new Image("res/HealthPickup.png");

        // INITIALISING SOUNDS
        healthPowerup = new Sound("res/HealthPowerup.wav");
        fire = new Sound("res/AlfredFire.wav");
        hitBucky = new Sound("res/hit.wav");

        // INITIALISING THE ANIMATIONS
        movingRight = new Animation(walkR, CONSTANT.ANIMATION_DURATION_3, true);
        movingLeft = new Animation(walkL, CONSTANT.ANIMATION_DURATION_3, true);
        attacking = new Animation(attack, new int[]{(150), (400), (400)},true);
        alfredAnimation = movingRight;  // Alfred starts with moving right

        setHealth(25F);

        setEntityDimensions(new float []{140, 120});

        setXposition(spawnXcoordinate);  // initial position of Alfred
        setYposition(spawnYcoordinate);

        setMoveSpeed(moveSpeed);

        alfredHitbox = new Rectangle(getXposition(), getYposition(), 186, 160);
        healthHitbox = new Rectangle(getXposition(), getYposition() + 100, 60, 60);

        leads = new LinkedList<Lead>();
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    public void render(GameContainer GC, Graphics g, StateBasedGame sbg) throws SlickException {
        if (isAlive()) {  // Only draw Alfred and update positions if he is alive
            alfredAnimation.draw(getXposition(), getYposition());
            alfredHitbox.setX(getXposition());
            alfredHitbox.setY(getYposition());

            // Updating health drop positions as Alfred walks so it is dropped where he dies
            healthHitbox.setX(getXposition());
            healthHitbox.setY(getYposition() + 100);

            // Rendering Alfred's projectiles
            for (Lead l : leads){
                l.render(GC, g, sbg);
            }
        } else {  // Render the health pickup once Alfred dies
            if (!healthPicked) healthPickup.draw(getXposition(), getYposition() + 100);
        }
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    public void update(GameContainer gc, int delta, StateBasedGame sbg) throws SlickException {

        updateRange();

        // Updating variables controlling the projectiles
        timePassedSinceDamage += (System.currentTimeMillis() - timeOfDamage);
        timePassedSinceProjectile += (System.currentTimeMillis() - timeOfProjectile);

        // if Bucky collides with Alfred, decrease Bucky's health
        if (alfredHitbox.intersects(Bucky.getBuckyHitbox()) && isAlive() && timePassedSinceDamage > 200000) {
            timePassedSinceDamage = 0;
            timeOfDamage = System.currentTimeMillis();

            // Bucky instantly dies even with full health since Alfred is a mini-boss.
            // Prevents Bucky from walking through Alfred, taking one hit, and then killing him from behind.
            Bucky.setBuckyHealth(Bucky.getBuckyHealth() - 3);
            bucky.setXposition(getXposition() + 300);
            if (!hitBucky.playing())
                hitBucky.play();
            return;
        }

        // Decreasing Alfred's health if Bucky's Arc collides with him
        Iterator<Arc> arcIterator = bucky.arcs.iterator();
        while (arcIterator.hasNext() && isAlive()) {
            Arc arc = arcIterator.next();
            if (arc.isActive() && arc.getArcHitBox().intersects(alfredHitbox)) {
                arc.setActive(false);
                setHealth(getHealth() - 1);
            }
        }

        // What to do after Alfred dies
        if (getHealth() <= 0) {
            setAlive(false);
            if (Bucky.getBuckyHitbox().intersects(healthHitbox) && !healthPicked){
                healthPicked = true;
                if (Bucky.getBuckyHealth() < 3) {
                    if (!healthPowerup.playing()) healthPowerup.play();
                    Bucky.setBuckyHealth(Bucky.getBuckyHealth() + 1);
                }
            }
        }

        // Alfred's attack
        {
            // Decreasing Bucky's health if Alfred's lead collides with him
            Iterator<Lead> leadIterator1 = leads.iterator();
            while (leadIterator1.hasNext() && isAlive()) {
                Lead lead = leadIterator1.next();
                if (lead.isActive() && lead.getLeadHitBox().intersects(Bucky.getBuckyHitbox())) {
                    if (!hitBucky.playing()) hitBucky.play();
                    lead.setActive(false);
                    Bucky.setBuckyHealth(Bucky.getBuckyHealth() - 1);
                }
            }
            // Updating the lead if it is active and removing it if it is not
            Iterator<Lead> leadIterator2 = leads.iterator();
            while (leadIterator2.hasNext()) {
                Lead lead = leadIterator2.next();
                if (lead.isActive()) {
                    lead.update(gc, delta, sbg);
                } else {
                    leadIterator2.remove();
                }
            }
        }

        // Updating the animations if Alfred is alive
        if (!isStatic && isAlive()) {
            if (isRight()) {
                alfredAnimation = movingRight;
                setXposition(getXposition() + (float) delta * moveSpeed);
                if (getXposition() > pathUpperBound) {
                    setRight(false);
                }
            } else {
                alfredAnimation = movingLeft;
                setXposition(getXposition() - (float) delta * moveSpeed);
                if (getXposition() < pathLowerBound) {
                    setRight(true);
                    alfredAnimation = movingRight;
                }

            }
        }

        // if bucky comes into shooting range, stop moving, turn right and attack
        if (bucky.getYposition() >= getYposition() && bucky.getYposition() <= getYposition() + 160 && isAlive()
                && !gc.isPaused()) {
            if (timePassedSinceProjectile > 200000) {
                alfredAnimation = attacking;
                isStatic = true;
                setXposition(getXposition());
                if (!fire.playing())
                    fire.play();

                leads.add(new Lead(new Vector2f(getXposition() + 55, getYposition() + 30),
                        new Vector2f(130, 0), isRight()));
                timePassedSinceProjectile = 0;
                timeOfProjectile = System.currentTimeMillis();
            }
        } else {
            isStatic = false;
        }
    }
}