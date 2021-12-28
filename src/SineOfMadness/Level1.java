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

// Setup level 1
public class Level1 extends BasicGameState {
    // Objects for all characters
    private static Bucky bucky;
    private Character draco;
    private Character beethoven;
    private Character alfred;

    // BG image
    private Image background;

    // Images and hitboxes for doors
    private Image door, lockedDoor;
    private Rectangle door1, door2;
    private boolean renderLockedMessage;

    // Platform objects
    private Platform levelBase;
    private Platform floorOne;
    private Platform floorTwo;
    private Platform floorThree;

    // Music to play in the background
    private Music soundtrack;

    // CONSTRUCTOR
    public Level1(int state) {
    }

    // GET THE STATE'S ID
    public int getID() {
        return CONSTANT.LEVEL_1_STATE;
    }

    // INITIALISATION METHOD
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

        Image platform = new Image("res/platform.png");

        // Calling constructors for all characters
        bucky = new Bucky(levelBase, floorOne, floorTwo, floorThree);
        draco = new Draco(bucky, 100, 80, 94, 700, 200, 500, 0.1F);
        beethoven = new Beethoven(bucky, 205,310, 380, 200, 0.1F, true);
        alfred = new Alfred(bucky, 230, 190, 230, 600, 0.1F);

        // Initialising soundtracks
        soundtrack = new Music("res/GameplaySoundtrack.ogg");

        // Location to spawn bucky
        bucky.setXposition(0.0F);
        bucky.setYposition(600.0F);

        // Call all character's initialisation methods
        bucky.init(gc);
        draco.init(gc);
        beethoven.init(gc);
        alfred.init(gc);

        // Initialise background
        background = new Image("res/level1BG.jpg");
        background = background.getScaledCopy(CONSTANT.DISPLAY_WIDTH, CONSTANT.DISPLAY_HEIGHT);

        // Initialize the platforms
        {
            levelBase = new Platform(platform,0, 693, CONSTANT.DISPLAY_WIDTH, 20);
            levelBase.init(gc);

            floorOne = new Platform(platform,0, 520, CONSTANT.DISPLAY_WIDTH - 280, 9);
            floorOne.init(gc);

            floorTwo = new Platform(platform,280, 347, CONSTANT.DISPLAY_WIDTH - 280, 9);
            floorTwo.init(gc);

            floorThree = new Platform(platform,0, 174, CONSTANT.DISPLAY_WIDTH - 280, 9);
            floorThree.init(gc);
        }

        // Initialise door images
        door = new Image("res/Door.png");
        lockedDoor = new Image("res/DoorLocked.png");

        // Initialise door hitboxes
        door1 = new Rectangle(1000F, levelBase.getYposition() - 110, door.getWidth(), door.getHeight());
        door2 = new Rectangle(20F, floorThree.getYposition() - 110, door.getWidth(), door.getHeight());
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.setColor(Color.black);
        background.draw(0.0F, 0.0F);
        draco.render(gc, g, sbg);
        beethoven.render(gc,g, sbg);
        if (Bucky.pickedKey()) alfred.render(gc, g, sbg);  // Render alfred only once bucky returns to level 1 with the key

        // Render level layout
        {
            levelBase.render(gc, g, sbg);
            floorOne.render(gc, g, sbg);
            floorTwo.render(gc, g, sbg);
            floorThree.render(gc, g, sbg);
        }

        // Render doors and bucky
        door.draw(door2.getX(), door2.getY());
        lockedDoor.draw(door1.getX(), door1.getY());

        bucky.render(gc, g, sbg);

        // If bucky tries to go through locked door without the key
        if (renderLockedMessage){
            g.drawString("THIS DOOR IS LOCKED.\n     FIND A KEY.",
                    floorOne.getXRange() + 35, floorOne.getYposition() + 8);
        }

        if (alfred.isAlive() && Bucky.pickedKey() && bucky.getXposition() < 350){
            g.drawString("Here we go again", bucky.getXposition(), bucky.getYposition() -20);
        }
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        // Update character objects
        bucky.update(gc, delta, sbg);
        draco.update(gc,delta, sbg);
        beethoven.update(gc, delta, sbg);

        // Enter losing state if bucky dies
        if (!(bucky.isAlive())) {
            sbg.enterState(CONSTANT.PLAYER_LOST_STATE, new FadeOutTransition(Color.red, 1),
                                                         new FadeInTransition(Color.black, 2000));
        }

        // Only update alfred once bucky has picked the key from level 2
        if (Bucky.pickedKey()) alfred.update(gc, delta, sbg);

        // Soundtrack for level 1 playing in loop
        if (!soundtrack.playing()) soundtrack.loop();

        // TO WALK THROUGH DOOR2 AND ENTER LEVEL 2
        if (Bucky.getBuckyHitbox().intersects(door2)
                && sbg.getCurrentStateID() == CONSTANT.LEVEL_1_STATE && !(bucky.getXposition() == 0)){
            sbg.enterState(CONSTANT.LEVEL_2_STATE);
        }

        // TO WALK THROUGH DOOR1 AND ENTER LEVEL 3
        if (Bucky.getBuckyHitbox().intersects(door1) && sbg.getCurrentStateID() == CONSTANT.LEVEL_1_STATE){
            if (Bucky.pickedKey()) {  // Only walk through if bucky has picked the key
                if (soundtrack.playing())
                    soundtrack.stop();
                sbg.enterState(CONSTANT.LEVEL_3_STATE, new FadeOutTransition(Color.black, 1500),
                                                        new FadeInTransition(Color.black, 1500));
            }
            else
                renderLockedMessage = true;
        } else
            renderLockedMessage = false;
    }
}
