package SineOfMadness;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

// To display info about the player and the enemies
public class Info extends BasicGameState implements Fonts{

    private Animation bucky;
    private Animation draco;
    private Animation beethoven;
    private Animation alfred;
    private Animation karen;

    private Image background;

    // CONSTRUCTOR
    public Info(int state){}

    // GET ID
    @Override
    public int getID() {// returns the ID of the state. For instructions, ID is 2
        return CONSTANT.INFO_STATE;
    }

    // INITIALISATION METHOD
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

        background = new Image("res/CreditsBG.jpg");
        background = background.getScaledCopy(CONSTANT.DISPLAY_WIDTH, CONSTANT.DISPLAY_HEIGHT);

        // Initialising all of the animations you see on the info screen
        Image[] standingRight = new Image[]{new Image("res/StandingRight1.png"), new Image("res/StandingRight2.png")};
        bucky = new Animation(standingRight, CONSTANT.BREATHING_ANIMATION_DURATION, true);

        Image[] dashR = new Image[]{new Image("res/DracoRight1.png"), new Image("res/DracoRightSmoke.png")};
        draco = new Animation(dashR, CONSTANT.ANIMATION_DURATION_1, true);

        Image[] flyingR = new Image[]{new Image("res/WaspRight1.png"), new Image("res/WaspRight2.png")};
        beethoven = new Animation(flyingR, CONSTANT.ANIMATION_DURATION_1, true);

        Image[] walkR = new Image[]{new Image("res/AlfredRight1.png"), new Image("res/AlfredRight2.png"),
                new Image("res/AlfredRight1.png"), new Image("res/AlfredRight3.png")};
        alfred = new Animation(walkR,CONSTANT.ANIMATION_DURATION_3, true);

        Image[] movingUp = new Image[]{new Image("res/ArachnidUp1.png"), new Image("res/ArachnidUp2.png"),
                new Image("res/ArachnidUp1.png"), new Image("res/ArachnidUp3.png")};
        karen = new Animation(movingUp, CONSTANT.ANIMATION_DURATION_3_FAST, true);
    }

    // RENDER METHOD (RUNS IN GAME LOOP)
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        background.draw();

        g.setFont(ttfFont1);
        g.setColor(Color.black);
        g.drawString("INFO ", 500,10);

        g.setFont(ttfFont2);
        {  // Render bucky
            bucky.draw(100, 100);
            g.drawString("Left and right arrow keys to move.", 170, 100);
            g.drawString("Up arrow to jump.", 170, 125);
            g.drawString("Spacebar to use Arc.", 170, 150);
        }

        {  // Render Draco
            draco.draw(65, 320);
            g.drawString("Health : 20", 170, 330);
            g.drawString("Special Ability: Don't talk me I angy", 170, 350);
        }

        {  // Render Beethoven
            beethoven.draw(75, 540);
            g.drawString("Health : 12", 170, 550);
            g.drawString("Special Ability: Get off my platform", 170, 570);
        }

        {  // Render Alfred
            alfred.draw(550, 100);
            g.drawString("Health : 25", 700, 105);
            g.drawString("Special Ability: Eat my Lead", 700, 125);
        }

        {  // Render Karen
            karen.draw(530, 360);
            g.drawString("Health : 50", 670, 360);
            g.drawString("Special Ability: Triple triple integrals", 670, 385);
        }

        g.drawString("BACKSPACE to go back to the menu", 675, 670);
    }

    // UPDATE METHOD (RUNS IN GAME LOOP)
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {  // responsible to updates the images on the screen

        Input input = gc.getInput();

        // To go back to the main menu
        if (input.isKeyPressed(input.KEY_BACK))
            sbg.enterState(CONSTANT.MENU_STATE, new FadeOutTransition(Color.black, 1000),
                                                new FadeInTransition(Color.black, 1000));

    }
}
