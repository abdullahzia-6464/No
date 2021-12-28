package SineOfMadness;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

// If the player loses then this state appears
public class PlayerLost extends BasicGameState implements Fonts{

    // Background image
    private Image background;

    // Music
    private Music playerLostSoundtrack;

    // CONSTRUCTOR
    PlayerLost(int state){
    }

    // INITIALISATION METHOD
    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

        // Initialise background
        background = new Image("res/CreditsBG.jpg");
        background = background.getScaledCopy(CONSTANT.DISPLAY_WIDTH, CONSTANT.DISPLAY_HEIGHT);

        // Initialise soundtrack
        playerLostSoundtrack = new Music("res/PlayerLostSoundtrack.ogg");
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        background.draw();
        g.setColor(Color.black);
        g.setFont(ttfFont1);
        g.drawString("CONTINUE... ", 400,150);
        g.setFont(ttfFont2);
        g.drawString("To go to the main menu: PRESS BACKSPACE", 320, 270);
        g.drawString("To go to level 1: PRESS ENTER", 320, 320);
        g.fillRect(0, 550, CONSTANT.DISPLAY_WIDTH, 242);
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        // Creating Input object to get user inputs
        Input input = gc.getInput();

        // Play soundtrack if not playing
        if (!playerLostSoundtrack.playing()) playerLostSoundtrack.loop();

        // If user restarts game, enter level 1
        if (input.isKeyPressed(Input.KEY_ENTER)){
            playerLostSoundtrack.stop();
            sbg.getState(1).init(gc,sbg);
            sbg.getState(2).init(gc,sbg);
            sbg.getState(3).init(gc,sbg);
            sbg.enterState(CONSTANT.LEVEL_1_STATE, new FadeOutTransition(Color.black, 1000),
                                                    new FadeInTransition(Color.white, 1000));
        }

        // If user wants to quit to main menu, enter menu state
        if (input.isKeyPressed(Input.KEY_BACK)) {
            playerLostSoundtrack.stop();
            sbg.enterState(CONSTANT.MENU_STATE, new FadeOutTransition(Color.black, 1000),
                                                 new FadeInTransition(Color.white, 1000));
        }
    }

    // GET ID
    public int getID() {
        return CONSTANT.PLAYER_LOST_STATE;
    }
}
