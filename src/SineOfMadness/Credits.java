package SineOfMadness;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;


// Display end game credits
public class Credits extends BasicGameState implements Fonts {

    private Image background;
    private Animation buckyStanding;

    private Music creditsSoundtrack;

    Credits(int state){
    }

    // INITIALISATION METHOD
    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

        background = new Image("res/CreditsBG.jpg");
        background = background.getScaledCopy(CONSTANT.DISPLAY_WIDTH, CONSTANT.DISPLAY_HEIGHT);

        Image[] standingRight = new Image[]{new Image("res/StandingRight1.png"), new Image("res/StandingRight2.png")};
        buckyStanding = new Animation(standingRight, CONSTANT.BREATHING_ANIMATION_DURATION, true);

        creditsSoundtrack = new Music("res/CreditsSoundtrack.ogg");
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        background.draw();
        g.setColor(Color.black);
        g.setFont(ttfFont1);  // Font for title
        g.drawString("THANK YOU FOR PLAYING!", 270,100);
        g.setFont(ttfFont2);  // Font for credits
        g.drawString("Saleha Zahrah Farooki     Level & Game Design", 285, 230);
        g.drawString("Talha Waheed              SFX & Collision", 285, 270);
        g.drawString("Abdullah Bin Omer Zia     Graphics & Optimization", 285, 310);
        g.drawString("Laraib Fatima               Graphics Consultant", 285, 350);
        g.drawString("Special thanks to Bucky Roberts from thenewboston for making this possible", 150, 390);
        g.setFont(ttfFont3);  // Font for message at the bottom
        g.drawString("Press ENTER to continue...", 20, 520);
        g.fillRect(0, 550, CONSTANT.DISPLAY_WIDTH, 242);

        buckyStanding.draw(300, 457);
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gc.getInput();

        if (!creditsSoundtrack.playing())
            creditsSoundtrack.loop();

        if (input.isKeyPressed(Input.KEY_ENTER)) {
            creditsSoundtrack.stop();
            sbg.enterState(CONSTANT.MENU_STATE, new FadeOutTransition(Color.black, 2000),
                                                new FadeInTransition(Color.black, 2000));
        }
    }

    public int getID() {
        return CONSTANT.CREDITS_STATE;
    }
}
