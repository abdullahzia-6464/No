// 
//  Source code recreated from a .class file by IntelliJ IDEA
//  (powered by Fernflower decompiler)
// 

package SineOfMadness;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

// Initialise all of the states and start the game
public class Game extends StateBasedGame {

    // CONSTRUCTOR
    public Game() {
        super(CONSTANT.GAME_NAME);
        this.addState(new Menu(CONSTANT.MENU_STATE));
        this.addState(new Level1(CONSTANT.LEVEL_1_STATE));
        this.addState(new Level2(CONSTANT.LEVEL_2_STATE));
        this.addState(new Level3(CONSTANT.LEVEL_3_STATE));
        this.addState(new Credits(CONSTANT.CREDITS_STATE));
        this.addState(new Info(CONSTANT.INFO_STATE));
        this.addState(new PlayerLost(CONSTANT.PLAYER_LOST_STATE));
    }

    // INITIALISING ALL STATES
    public void initStatesList(GameContainer gc) throws SlickException {
        this.getState(CONSTANT.MENU_STATE).init(gc, this);
        this.getState(CONSTANT.LEVEL_1_STATE).init(gc, this);
        this.getState(CONSTANT.LEVEL_2_STATE).init(gc, this);
        this.getState(CONSTANT.LEVEL_3_STATE).init(gc, this);
        this.getState(CONSTANT.CREDITS_STATE).init(gc,this);
        this.getState(CONSTANT.INFO_STATE).init(gc, this);
        this.getState(CONSTANT.PLAYER_LOST_STATE).init(gc, this);
        this.enterState(CONSTANT.LEVEL_3_STATE);  // Game starts from this state
    }

    // MAIN METHOD
    public static void main(String... args) {
        try {
            AppGameContainer appGameContainer = new AppGameContainer(new Game());
            appGameContainer.setDisplayMode(CONSTANT.DISPLAY_WIDTH, CONSTANT.DISPLAY_HEIGHT, false);
            appGameContainer.start();  // Launch
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
