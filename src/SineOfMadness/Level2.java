// 
//  Source code recreated from a .class file by IntelliJ IDEA
//  (powered by Fernflower decompiler)
// 

package SineOfMadness;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

// Setup level 2
public class Level2 extends BasicGameState{
    // Objects for all the characters
    private Bucky bucky;
    private Character draco;
    private Character beethoven;
    private Character alfred;

    // Images
    private Image background;
    private Image platform;
    private Image[] key;
    private Image door;

    // Platforms
    private Platform levelBase;
    private Platform floorOne;
    private Platform floorTwo;
    private Platform floorThree;

    // Animation for key
    private Animation keyAnimation;

    // Hitboxes for door and key
    private Rectangle keyHitbox;
    private Rectangle doorHitbox;

    // SFX
    Sound keyPicked;

    // CONSTRUCTOR
    public Level2(int state) {
    }

    public int getID() {
        return CONSTANT.LEVEL_2_STATE;
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

        platform = new Image("res/platform.png");
        bucky = new Bucky(levelBase, floorOne, floorTwo, floorThree);
        draco = new Draco(bucky, 200, 243, 260, 850, 400, 700, 0.12F);
        beethoven = new Beethoven(bucky, 120, 370, 45, 230, 0.25F, false);
        alfred = new Alfred(bucky, 300, 540, 260, 700, 0.08F);
        keyPicked = new Sound("res/keyCollect.ogg");

        bucky.init(gc);
        draco.init(gc);
        beethoven.init(gc);
        alfred.init(gc);

        bucky.setXposition(940F);
        bucky.setYposition(78F);
        bucky.setRight(false);

        background = new Image("res/level2BG.jpg");
        background = background.getScaledCopy(CONSTANT.DISPLAY_WIDTH, CONSTANT.DISPLAY_HEIGHT);

        // Initialize the platforms
        {
            levelBase = new Platform(platform,0, 693, CONSTANT.DISPLAY_WIDTH, 20);
            levelBase.init(gc);

            floorOne = new Platform(platform,0, 490, CONSTANT.DISPLAY_WIDTH - 280, 9);
            floorOne.init(gc);

            floorTwo = new Platform(platform,280, 337, CONSTANT.DISPLAY_WIDTH - 280, 9);
            floorTwo.init(gc);

            floorThree = new Platform(platform,800, 184, CONSTANT.DISPLAY_WIDTH - 280, 9);
            floorThree.init(gc);
        }

        // Initialising key and door animations and hitboxes
        key = new Image[]{new Image("res/Key1.png"), new Image("res/Key2.png")};
        keyAnimation = new Animation(key, CONSTANT.ANIMATION_DURATION_1, true);
        keyHitbox = new Rectangle(20, levelBase.getYposition() - 85, 45, 60);
        door = new Image("res/Door.png");
        doorHitbox = new Rectangle(1000, floorThree.getYposition() - 110, 74, 110);
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        g.setColor(Color.black);

        // Render the background
        background.draw(0.0F, 0.0F);

        // Render the characters
        bucky.render(gc, g, sbg);
        draco.render(gc, g, sbg);
        beethoven.render(gc,g, sbg);
        alfred.render(gc,g,sbg);

        // Render the door
        door.draw(doorHitbox.getX(), doorHitbox.getY());

        // Render the key if it hasn't been picked
        if (!(Bucky.pickedKey())) {
            keyAnimation.draw(15.0F, levelBase.getYposition() - 85);
        }

        // Render level layout
        {
            levelBase.render(gc, g, sbg);
            floorOne.render(gc, g, sbg);
            floorTwo.render(gc, g, sbg);
            floorThree.render(gc, g, sbg);
        }
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        // Call character update methods
        bucky.update(gc,delta,sbg);
        draco.update(gc,delta,sbg);
        beethoven.update(gc, delta, sbg);
        alfred.update(gc, delta, sbg);

        // If bucky dies enter the lost state
        if (!(bucky.isAlive())){
            sbg.enterState(CONSTANT.PLAYER_LOST_STATE, new FadeOutTransition(Color.red, 1),
                                                         new FadeInTransition(Color.black, 2000));
        }

        // If bucky goes through the door back to level 1
        if (Bucky.getBuckyHitbox().intersects(doorHitbox) && sbg.getCurrentStateID() == CONSTANT.LEVEL_2_STATE){
            sbg.enterState(CONSTANT.LEVEL_1_STATE);
        }

        // Collision detection for bucky and the key
        if (Bucky.getBuckyHitbox().intersects(keyHitbox) && sbg.getCurrentStateID() == CONSTANT.LEVEL_2_STATE){
            if (!(Bucky.pickedKey()))
                if (!keyPicked.playing())
                    keyPicked.play();
            Bucky.setPickedKey(true);
        }
    }
}
