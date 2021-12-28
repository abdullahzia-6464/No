package SineOfMadness;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Iterator;
import java.util.LinkedList;


// The spider like enemy. Found in level 3, the boss battle
public class Karen extends Character {
    // Animations
    private Animation karen;
    private Animation karenMovingUp;
    private Animation karenMovingDown;
    private Animation karenAttacking;

    // Variables to ensure projectiles aren't instantly after one another
    private long timePassedSinceProjectile = 200000;
    private long timeOfProjectile = System.currentTimeMillis();

    // Hitbox for collision detection
    protected Rectangle karenHitbox;

    // Bucky object
    private final Bucky bucky;

    // LinkedList to keep track of projectiles
    private LinkedList<Web> webs;

    // Spawn and movement coordinates + speed
    private final float spawnXcoordinate;
    private final float spawnYcoordinate;
    private final float pathUpperBound;
    private final float pathLowerBound;
    private final float moveSpeed;

    // SFX
    private Sound hitBucky;
    private Sound karenHit;

    // Equivalent to isRight but for vertical movement
    private boolean isGoingUp= true;

    // CONSTRUCTOR
    Karen(Bucky bucky, float spawnXcoordinate, float spawnYcoordinate, float pathUpperBound, float pathLowerBound, float moveSpeed) {
        this.bucky = bucky;
        this.spawnXcoordinate = spawnXcoordinate;
        this.spawnYcoordinate = spawnYcoordinate;
        this.pathLowerBound = pathLowerBound;
        this.pathUpperBound = pathUpperBound;
        this.moveSpeed = moveSpeed;
    }

    // INITIALISATION METHOD
    @Override
    public void init(GameContainer GC) throws SlickException {
        // Initialising image arrays
        Image[] movingUp = new Image[]{new Image("res/ArachnidUp1.png"), new Image("res/ArachnidUp2.png"),
                new Image("res/ArachnidUp1.png"), new Image("res/ArachnidUp3.png")};

        Image[] movingDown = new Image[]{new Image("res/ArachnidDown1.png"), new Image("res/ArachnidDown2.png"),
                new Image("res/ArachnidDown1.png"), new Image("res/ArachnidDown3.png")};

        Image[] attacking = new Image[]{new Image ("res/ArachnidAttack1.png"), new Image ("res/ArachnidAttack2.png")};

        // Initialising animations
        karenMovingUp = new Animation(movingUp, CONSTANT.ANIMATION_DURATION_3_FAST, true);
        karenMovingDown = new Animation(movingDown, CONSTANT.ANIMATION_DURATION_3_FAST, true);
        karenAttacking = new Animation(attacking, CONSTANT.ANIMATION_DURATION_1, true);
        karen = karenMovingUp;

        // Initialising SFX
        hitBucky = new Sound("res/hit.wav");
        karenHit = new Sound("res/KarenHit.ogg");

        // Initialising hitbox
        karenHitbox = new Rectangle(getXposition(), getYposition(), 170, 198);

        setHealth(50F);  // Health = 50

        // Initialising coordinates and speed
        setXposition(spawnXcoordinate);
        setYposition(spawnYcoordinate);
        setMoveSpeed(moveSpeed);

        // Initialising dimensions
        setEntityDimensions(new float[]{200, 190});

        // Initialising projectile linked list
        webs = new LinkedList<Web>();
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    @Override
    public void render(GameContainer GC, Graphics g, StateBasedGame sbg) throws SlickException {

        if (isAlive()) {  // ONLY DRAW KAREN IF ALIVE
            karen.draw(getXposition(), getYposition());
            karenHitbox.setX(getXposition());
            karenHitbox.setY(getYposition());
        }

        for (Web w : webs) {  // RENDER WEBS
            w.render(GC, g, sbg);
        }
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    @Override
    public void update(GameContainer gc, int delta, StateBasedGame sbg) throws SlickException {
        updateRange();

        // Incrementing time passed since last damage
        timePassedSinceDamage += (System.currentTimeMillis() - timeOfDamage);
        timePassedSinceProjectile += (System.currentTimeMillis() - timeOfProjectile);

        // MOVEMENT AND ANIMATION UPDATE
        if (isGoingUp){  // If karen's moving up
            setYposition(getYposition() - (float) delta * getMoveSpeed());
            if (getYposition() < pathLowerBound) {
                isGoingUp = false;
                karen = karenMovingDown;
            }
        } else {  // If Karen's moving down
            setYposition(getYposition() + (float) delta * getMoveSpeed());
            if (getYposition() > pathUpperBound) {
                isGoingUp = true;
                karen = karenMovingUp;
            }
        }

        // If Bucky collides with Karen
        if (karenHitbox.intersects(Bucky.getBuckyHitbox()) && isAlive() && timePassedSinceDamage > 300000) {
            timePassedSinceDamage = 0;
            timeOfDamage = System.currentTimeMillis();
            Bucky.setBuckyHealth(Bucky.getBuckyHealth() - 1);
            bucky.setXposition(getXposition() + 300);
            if(!hitBucky.playing())
                hitBucky.play();
            return;
        }

        // If bucky is in range and karen is alive, shoot a projectile
        if (bucky.getYposition() >= getYposition() && bucky.getYposition() <= getYposition() + 190 && isAlive()) {
            if (timePassedSinceProjectile > 150000) {
                karen = karenAttacking;

                webs.add(new Web(new Vector2f(getXposition() + 55, getYposition() + 30),
                        new Vector2f(130, 0), isRight()));

                webs.add(new Web(new Vector2f(getXposition() + 55, getYposition() + 30),
                        new Vector2f(130, 18), isRight()));

                webs.add(new Web(new Vector2f(getXposition() + 55, getYposition() + 30),
                        new Vector2f(130, -18), isRight()));

                timePassedSinceProjectile = 0;
                timeOfProjectile = System.currentTimeMillis();
            }
        }

        {  //  PROJECTILE BLOCK
            // If bucky's attack hits karen, damage karen
            Iterator<Arc> arcIterator = bucky.arcs.iterator();
            while (arcIterator.hasNext() && isAlive()) {
                Arc arc = arcIterator.next();
                if (arc.isActive() && arc.getArcHitBox().intersects(karenHitbox)) {
                    arc.setActive(false);
                    if (!karenHit.playing())
                        karenHit.play();  // Grunt when Karen gets hit by an arc
                    setHealth(getHealth() - 1);
                }
            }

            // Damage bucky if he makes contact with a web
            Iterator<Web> webIterator1 = webs.iterator();
            while (webIterator1.hasNext() && isAlive()) {
                Web web = webIterator1.next();
                if (web.isActive() && web.getWebHitbox().intersects(Bucky.getBuckyHitbox())) {
                    web.setActive(false);
                    if (timePassedSinceDamage > 300000) {
                        timePassedSinceDamage = 0;
                        timeOfDamage = System.currentTimeMillis();
                        Bucky.setBuckyHealth(Bucky.getBuckyHealth() - 1);
                        if (!hitBucky.playing())
                            hitBucky.play();
                    }
                }
            }

            // Remove inactive projectiles and update active ones
            Iterator<Web> webIterator2 = webs.iterator();
            while (webIterator2.hasNext()) {
                Web web = webIterator2.next();
                if (web.isActive()) {
                    web.update(gc, delta, sbg);
                } else {
                    webIterator2.remove();
                }
            }
        }  // END PROJECTILE BLOCK

        // If karen dies, set isAlive to false
        if (getHealth() <= 0)
            setAlive(false);
    }
}
