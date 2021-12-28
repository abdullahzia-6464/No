// 
//  Source code recreated from a .class file by IntelliJ IDEA
//  (powered by Fernflower decompiler)
// 

package SineOfMadness;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

// The first state, the main menu
public class Menu extends BasicGameState {

    // Duration at which to update Menu screen
    private final int[] durationMenu = new int[]{250, 250};

    // Animations
    private Animation mainMenu;
    private Animation menuBucky;

    // SFX
    private Music mainMenuSoundtrack;

    // CONSTRUCTOR
    public Menu(int state) {
    }

    // GET_ID
    public int getID() {
        return CONSTANT.MENU_STATE;
    }

    // INITIALISATION METHOD
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

        // Initialising images and animations
        {
            Image mainMenu1 = new Image("res/Menu_Frame1.jpg");
            mainMenu1 = mainMenu1.getScaledCopy(CONSTANT.DISPLAY_WIDTH, CONSTANT.DISPLAY_HEIGHT + 80);

            Image mainMenu2 = new Image("res/Menu_Frame2.jpg");
            mainMenu2 = mainMenu2.getScaledCopy(CONSTANT.DISPLAY_WIDTH, CONSTANT.DISPLAY_HEIGHT + 80);

            Image menuBucky1 = new Image("res/MenuBuckyFrame1.png");
            menuBucky1 = menuBucky1.getScaledCopy(0.2F);

            Image menuBucky2 = new Image("res/MenuBuckyFrame2.png");
            menuBucky2 = menuBucky2.getScaledCopy(0.2F);

            Image[] background = new Image[]{mainMenu1, mainMenu2};
            mainMenu = new Animation(background, durationMenu, true);

            Image[] foreground = new Image[]{menuBucky1, menuBucky2};
            menuBucky = new Animation(foreground, CONSTANT.BREATHING_ANIMATION_DURATION, true);
        }

        // Initialising music
        mainMenuSoundtrack = new Music("res/MainMenuSoundtrack.ogg");

    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        // Render menu and bucky animation
        mainMenu.draw(0.0F, 0.0F);
        menuBucky.draw(114.0F, 478.0F);
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        // Make an Input object for mouse inputs
        Input input = gc.getInput();
        int xPosition = Mouse.getX();
        int yPosition = Mouse.getY();

        // Replay music if not playing
        if (!mainMenuSoundtrack.playing()) mainMenuSoundtrack.loop();

        // If user presses play button
        if (xPosition > 508 && xPosition < 588 && yPosition > 302 && yPosition < 366 && input.isMouseButtonDown(0)) {
            mainMenuSoundtrack.stop();
            sbg.getState(CONSTANT.LEVEL_1_STATE).init(gc, sbg);
            sbg.getState(CONSTANT.LEVEL_2_STATE).init(gc, sbg);
            sbg.getState(CONSTANT.LEVEL_3_STATE).init(gc, sbg);
            sbg.enterState(1, new FadeOutTransition(Color.black, 2000),
                                    new FadeInTransition(Color.white, 2000));
        }

        // If user presses info button
        if (xPosition > 508 && xPosition < 588 && yPosition > 212 && yPosition < 302 && input.isMouseButtonDown(0)) {
            sbg.enterState(CONSTANT.INFO_STATE);
        }

        // If user presses exit button
        if (xPosition > 508 && xPosition < 588 && yPosition > 150 && yPosition < 212 && input.isMouseButtonDown(0)) {
            System.exit(0);
        }
    }
}
