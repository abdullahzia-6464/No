package SineOfMadness;

// To store all of the constants
public class CONSTANT {

    // Default durations for animations
    public static final int[] ANIMATION_DURATION_1 = {150, 150};
    public static final int[] ANIMATION_DURATION_2 = {150};
    public static final int[] ANIMATION_DURATION_3 = {250, 150, 250, 150};
    public static final int[] ANIMATION_DURATION_1_FAST = {90, 90};
    public static final int[] ANIMATION_DURATION_3_FAST = {50, 50, 50, 50};
    public static final int[] BREATHING_ANIMATION_DURATION = {450, 450};

    public static final int PROJECTILE_LIFETIME = 1300;  // How long a projectile lasts
    public static final float GRAVITY = 0.9F;  // Simulate gravity

    // Window size
    public static final int DISPLAY_WIDTH = 1080;
    public static final int DISPLAY_HEIGHT = 720;
    public static final int DISPLAY_WIDTH_SMALL = 120;

    public static final String GAME_NAME = "SINE OF MADNESS";

    // Game states
    public static final int MENU_STATE = 0;
    public static final int LEVEL_1_STATE = 1;
    public static final int LEVEL_2_STATE = 2;
    public static final int LEVEL_3_STATE = 3;
    public static final int CREDITS_STATE = 4;
    public static final int INFO_STATE = 5;
    public static final int PLAYER_LOST_STATE = 6;

}
