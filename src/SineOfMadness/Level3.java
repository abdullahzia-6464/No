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

// Setup level 3 (boss battle)
public class Level3 extends BasicGameState {
    // Characters
    private Bucky bucky;
    private Karen karen;

    // Platforms
    private Platform levelBase;
    private Platform floorOne;
    private Platform floorTwo;
    private Platform floorThree;

    // Images
    private Image background;
    private Image doorLocked;
    private Image endGameDoor;

    // SFX
    private Music bossBattleSoundtrack;

    // Hitboxes
    private Rectangle doorHitbox;
    private Rectangle endGameDoorHitbox;

    // Whether or not to draw the door leading to winning state
    private boolean drawEndGameDoor;

    // CONSTRUCTOR
    public Level3(int state) {
    }

    // GET_ID
    public int getID() {
        return CONSTANT.LEVEL_3_STATE;
    }

    // INITIALISATION METHOD
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

        Image platformSmall = new Image("res/BreakablePlatform.png");
        Image platform = new Image("res/platform.png");

        bossBattleSoundtrack = new Music("res/BossBattleSoundtrack.ogg");

        bucky = new Bucky(levelBase, floorOne, floorTwo, floorThree);
        bucky.init(gc);

        // Setting bucky's spawn coordinates
        bucky.setRight(false);
        bucky.setXposition(940F);
        bucky.setYposition(600F);

        // Initialising Karen
        karen = new Karen(bucky, 10, 400, 600, 20, 0.2F);
        karen.init(gc);

        // Initialising background Image
        background = new Image("res/level3BG.jpg");
        background = background.getScaledCopy(CONSTANT.DISPLAY_WIDTH, CONSTANT.DISPLAY_HEIGHT);

        // Initialize the platforms
        {
            levelBase = new Platform(platform,200, 693, CONSTANT.DISPLAY_WIDTH, 20);
            levelBase.init(gc);

            floorOne = new Platform(platformSmall,700, 520, CONSTANT.DISPLAY_WIDTH_SMALL, 9);
            floorOne.init(gc);

            floorTwo = new Platform(platformSmall,500, 347, CONSTANT.DISPLAY_WIDTH_SMALL, 9);
            floorTwo.init(gc);

            floorThree = new Platform(platformSmall,0, 174, 0, 0);
            floorThree.init(gc);
        }

        // Initialising doors
        {
            doorLocked = new Image("res/DoorLocked.png");
            doorHitbox = new Rectangle(1000F, levelBase.getYposition(), 74, 110);

            endGameDoor = new Image("res/Door.png");
            endGameDoorHitbox = new Rectangle(505F, floorTwo.getYposition() - 110, 74, 110);
        }

        drawEndGameDoor = false;
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        // Set graphics object colour to black
        g.setColor(Color.black);

        // Render the background
        background.draw(0.0F, 0.0F);

        //  Render level layout
        {
            levelBase.render(gc, g, sbg);
            floorOne.render(gc, g, sbg);
            floorTwo.render(gc, g, sbg);
            floorThree.render(gc, g, sbg);
        }

        // Print message to convey bucky can't go back from where he came from
        if (Bucky.getBuckyHitbox().intersects(doorHitbox) && sbg.getCurrentStateID() == CONSTANT.LEVEL_3_STATE){
            g.drawString("DON'T BE A SCAREDY CAT. \n    FIGHT THE BOSS.",
                    doorHitbox.getX() - 155, doorHitbox.getY() - 200);
        }

        // Render the locked door
        doorLocked.draw(1000F, levelBase.getYposition() - 110);

        // Render bucky and karen
        bucky.render(gc, g, sbg);
        karen.render(gc, g, sbg);

        // To draw final door when bucky isn't colliding with it and karen is dead
        if (bucky.getYposition() > 400 && !karen.isAlive()) {
            drawEndGameDoor = true;
        }

        // This could have been added in the above if condition but this one makes sure that the string stays once drawn
        if (drawEndGameDoor && !karen.isAlive())
            g.drawString("It's time for the SNS lab let's go", 400, 200);

        // Render final door
        if (!karen.isAlive() && drawEndGameDoor) {
            endGameDoor.draw(505, floorTwo.getYposition() - 110);
        }
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

        // Play Music
        if (!bossBattleSoundtrack.playing()) bossBattleSoundtrack.loop();

        // Update bucky and karen
        bucky.update(gc, delta, sbg);
        karen.update(gc,delta,sbg);

        // If karen dies, allow bucky to exit the level
        if (!karen.isAlive()) {
            bossBattleSoundtrack.stop();
            if (Bucky.getBuckyHitbox().intersects(endGameDoorHitbox) && drawEndGameDoor) {
                sbg.enterState(CONSTANT.CREDITS_STATE, new FadeOutTransition(Color.black, 1000),
                                                         new FadeInTransition(Color.white, 2000));
            }
        }

        // If bucky dies enter lost game state
        if (!bucky.isAlive()){
            bossBattleSoundtrack.stop();
            sbg.enterState(CONSTANT.PLAYER_LOST_STATE, new FadeOutTransition(Color.black, 1),
                                                         new FadeInTransition(Color.black, 2000));
        }
    }
}
