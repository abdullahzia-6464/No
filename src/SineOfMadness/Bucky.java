package SineOfMadness;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import java.util.Iterator;
import java.util.LinkedList;


// The player's class
public class Bucky extends Character {
    // Animations for Bucky
    private Animation buckyAnimation;
    private Animation movingRight, movingLeft;
    private Animation jumpingR, jumpingL;
    private Animation standR, standL;
    private Animation attackL, attackR;
    private Animation fallingR, fallingL;

    // Variable for if user wants to pause the game
    private boolean paused = false;

    // Variables to control the jump
    private long jumpLimit = 0, jumpTime = System.currentTimeMillis();
    private boolean jumping = false;
    private boolean collidingFromTop = true;

    // Hitbox for collision detection
    private static Rectangle buckyHitbox;

    // Bucky's health is not using Character's inherited health
    // because it is static to ensure it is the same across all levels
    private static float buckyHealth;

    // The platforms in the current level
    private final Platform levelBase;
    private final Platform floorOne;
    private final Platform floorTwo;
    private final Platform floorThree;

    // Images for the HUD
    Image health0, health1, health2, health3;
    Image keyPicked;

    // SFX
    Sound arcAttackSound;
    Sound buckyRunning;

    // Whether bucky has picked the key or not
    private static boolean pickedKey;

    // LinkedList to keep track of Bucky's arcs
    protected LinkedList<Arc> arcs;

    // CONSTRUCTOR
    public Bucky(Platform levelBase, Platform floorOne, Platform floorTwo, Platform floorThree) {
        this.levelBase = levelBase;
        this.floorOne = floorOne;
        this.floorTwo = floorTwo;
        this.floorThree = floorThree;
    }

    // INITIALISATION METHOD
    public void init(GameContainer gc) throws SlickException {
        setEntityDimensions(new float []{80, 97});
        updateRange();

        buckyHitbox = new Rectangle(getXposition(), getYposition(), 55, 97);
        buckyHealth = 3f;
        setPickedKey(false);

        // Initialising Image arrays
        Image[] walkRight = new Image[]{new Image("res/RunningRight1.png"), new Image("res/RunningRight2.png")};
        Image[] walkLeft  = new Image[]{new Image("res/RunningLeft1.png"), new Image("res/RunningLeft2.png")};
        Image[] jumpRight = new Image[]{new Image("res/JumpingRight.png")};
        Image[] jumpLeft  = new Image[]{new Image("res/JumpingLeft.png")};
        Image[] standingLeft  = new Image[]{new Image("res/StandingLeft1.png"), new Image("res/StandingLeft2.png")};
        Image[] standingRight = new Image[]{new Image("res/StandingRight1.png"), new Image("res/StandingRight2.png")};
        Image[] attackRight = new Image[]{new Image("res/AttackRight.png")};
        Image[] attackLeft = new Image[]{new Image("res/AttackLeft.png")};
        Image[] fallingRight = new Image[]{new Image("res/FallingRight.png")};
        Image[] fallingLeft = new Image[]{new Image("res/FallingLeft.png")};

        // What appears on the top right corer of the screen while playing
        health3 = new Image("res/Health3.png");
        health2 = new Image("res/Health2.png");
        health1 = new Image("res/Health1.png");
        health0 = new Image("res/Health0.png");
        keyPicked = new Image("res/KeyPicked.png");

        // Initialising animations
        movingRight  = new Animation(walkRight, CONSTANT.ANIMATION_DURATION_1, false);
        movingLeft   = new Animation(walkLeft,  CONSTANT.ANIMATION_DURATION_1, false);
        jumpingR     = new Animation(jumpRight, CONSTANT.ANIMATION_DURATION_2, false);
        jumpingL     = new Animation(jumpLeft,  CONSTANT.ANIMATION_DURATION_2, false);
        standR       = new Animation(standingRight,  CONSTANT.BREATHING_ANIMATION_DURATION, true);
        standL       = new Animation(standingLeft, CONSTANT.BREATHING_ANIMATION_DURATION, true);
        attackL      = new Animation(attackLeft, CONSTANT.ANIMATION_DURATION_2, false);
        attackR      = new Animation(attackRight, CONSTANT.ANIMATION_DURATION_2, false);
        fallingR     = new Animation(fallingRight, CONSTANT.ANIMATION_DURATION_2, true);
        fallingL     = new Animation(fallingLeft, CONSTANT.ANIMATION_DURATION_2, true);
        buckyAnimation = standR; // Initialising Bucky's animation to standing right.

        // Initialising linked list for attacks
        arcs = new LinkedList<Arc>();

        // Initialising SFX
        arcAttackSound = new Sound("res/ArcAttackSound.ogg");
        buckyRunning = new Sound("res/BuckyRunning.ogg");
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    public void render(GameContainer gc, Graphics g, StateBasedGame sbg) throws SlickException {

        // Draw Bucky's animation
        buckyAnimation.draw(getXposition() ,getYposition());


        {  // Draw HUD
            if (pickedKey()) {
                keyPicked.draw(840, 20);
            }

            switch ((int) buckyHealth) {
                case 3:
                    health3.draw(900, 20);
                    break;
                case 2:
                    health2.draw(900, 20);
                    break;
                case 1:
                    health1.draw(900, 20);
                    break;
                default:
                    health0.draw(900, 20);

            }
        }


        // Render attacks
        for (Arc a : arcs){
            a.render(gc, g, sbg);
        }

        // If user pauses the game
        if (paused) {
            gc.setPaused(true);
            g.drawString("Resume(R)", 500, 10.0F);
            g.drawString("Main Menu(M)", 500, 30.0F);
            g.drawString("Quit(Q)", 500, 50.0F);
            if (!paused) {
                gc.setPaused(false);
                g.clear();
            }
        }
    }

    public void update(GameContainer gameContainer, int delta, StateBasedGame stateBasedGame) throws SlickException {
        updateRange();

        // Update hitbox coordinates
        buckyHitbox.setX(getXposition());
        buckyHitbox.setY(getYposition());

        Input input = gameContainer.getInput();

        // Adding gravity if bucky isn't jumping and isn't walking on a platform
        if (!jumping) {
            if (isColliding() && collidingFromTop) {
                setYposition(getYposition());
            } else {
                setYposition(getYposition() + CONSTANT.GRAVITY);
            }
        }


        {  // Movement controls (Updating position and animations)
            if (input.isKeyDown(Input.KEY_RIGHT)) {
                setRight(true);
                buckyAnimation = movingRight;

                if (isColliding() && !buckyRunning.playing()) buckyRunning.play();

                setXposition(getXposition() + ((float) delta * 0.4F));
                movingRight.update(delta);
                if (getXposition() > 1005.0F) {
                    setXposition(getXposition() - (float) delta * 0.9F);
                }
            }

            if (input.isKeyDown(Input.KEY_LEFT)) {
                setRight(false);
                buckyAnimation = movingLeft;

                if (isColliding() && !buckyRunning.playing())
                    buckyRunning.play();

                setXposition(getXposition() - ((float) delta * 0.4F));
                movingLeft.update(delta);
                if (getXposition() < 0.0F) {
                    setXposition(getXposition() + (float) delta * 0.9F);
                }

                if (stateBasedGame.getCurrentStateID() == 3) {
                    if (getXposition() < 200)
                        setXposition(getXposition() + (float) (delta * 0.9));
                }
            }

            if (input.isKeyPressed(Input.KEY_UP) && !jumping && isColliding()) {
                jumping = true;
                jumpTime = System.currentTimeMillis();
                jumpLimit = 0;
            }
            if (jumping) {
                if (isRight()) {
                    buckyAnimation = jumpingR;
                } else {
                    buckyAnimation = jumpingL;
                }
                setYposition(getYposition() - (float) delta * 0.8F);
                jumping = jumpLimit < 320;  // Didn't know you could do this
                jumpLimit = System.currentTimeMillis() - jumpTime;

                // If bucky collides with the platform from below excluding the level base.
                if (isColliding() && !buckyHitbox.intersects(levelBase.getPlatformBounds()) && !collidingFromTop) {
                    jumping = false;
                }
            }

            // Setting animation for falling
            if (!(isColliding() || jumping)) {
                if (isRight()) buckyAnimation = fallingR;
                else buckyAnimation = fallingL;
            }
        }

        {  // Bucky's attack

            // Removing inactive arcs and updating active ones
            Iterator<Arc> arcIterator1 = arcs.iterator();
            while (arcIterator1.hasNext()) {
                Arc arc = arcIterator1.next();
                if (arc.isActive()) {
                    arc.update(gameContainer, delta, stateBasedGame);
                } else {
                    arcIterator1.remove();
                }
            }

            // Listening for inputs for the arc attack
            if (input.isKeyPressed(Input.KEY_SPACE)) {
                if (isRight()) {
                    if (arcs.size() < 4) {
                        arcAttackSound.play();
                        arcs.add(new Arc(new Vector2f(getXposition() + 55, getYposition() + 30),
                                new Vector2f(100, 0), isRight()));
                        buckyAnimation = attackR;
                    }
                }
                else {
                    if (arcs.size() < 4) {
                        arcAttackSound.play();
                        arcs.add(new Arc(new Vector2f(getXposition(), getYposition() + 30),
                                new Vector2f(-100, 0), isRight()));
                        buckyAnimation = attackL;
                    }
                }
            }
        }

        // If user is pressing none of the keys used to control bucky, reset the animation
        if (!((input.isKeyDown(Input.KEY_UP))
                || (input.isKeyDown(Input.KEY_DOWN))
                || (input.isKeyDown(Input.KEY_RIGHT))
                || (input.isKeyDown(Input.KEY_LEFT))
                || (input.isKeyDown(Input.KEY_SPACE)))
                && isColliding()){
            if (isRight()) buckyAnimation = standR;
            else buckyAnimation = standL;
        }

        // If bucky's health falls below 1
        if (buckyHealth <= 0){
            setAlive(false);
        }

        // To pause the game by pressing esc
        if (input.isKeyDown(1)) {
            gameContainer.setPaused(true);
            paused = true;
        }

        // If user wants to resume the game
        if (paused && input.isKeyDown(19)) {
            gameContainer.setPaused(false);
            paused = false;
        }

        // If user wants to exit to menu
        if (paused && input.isKeyDown(50)) {
            gameContainer.setPaused(false);
            paused = false;
            stateBasedGame.getState(CONSTANT.MENU_STATE).init(gameContainer, stateBasedGame);
            stateBasedGame.enterState(CONSTANT.MENU_STATE, new FadeOutTransition(Color.black, 1000),
                                                            new FadeInTransition(Color.black, 1000));
        }

        // If user wants to exit the game
        if (paused && input.isKeyDown(16)) {
            System.exit(0);
        }


    }

    // Function to check if Bucky is walking on a platform
    public boolean isColliding(){
        if (buckyHitbox.intersects(levelBase.getPlatformBounds())){
            collidingFromTop = true;
            return true;
        } else if (buckyHitbox.intersects(floorOne.getPlatformBounds())){
            collidingFromTop = !(getYposition() + buckyHitbox.getHeight() - 1 > floorOne.getYposition());
            return true;
        } else if (buckyHitbox.intersects(floorTwo.getPlatformBounds())){
            collidingFromTop = !(getYposition() + buckyHitbox.getHeight() - 1 > floorTwo.getYposition());
            return true;
        } else if (buckyHitbox.intersects(floorThree.getPlatformBounds())){
            collidingFromTop = !(getYposition() + buckyHitbox.getHeight() - 1 > floorThree.getYposition());
            return true;
        }
        return false;
    }

    // GETTERS AND SETTERS

    public static boolean pickedKey(){
        return pickedKey;
    }
    public static void setPickedKey(boolean pickedKeyParam){
        pickedKey = pickedKeyParam;
    }

    public static Rectangle getBuckyHitbox(){
        return buckyHitbox;
    }

    public static float getBuckyHealth() {
        return buckyHealth;
    }
    public static void setBuckyHealth(float buckyHealth) {
        Bucky.buckyHealth = buckyHealth;
    }
}
