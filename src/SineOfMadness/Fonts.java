package SineOfMadness;

import org.newdawn.slick.TrueTypeFont;

// For fonts in the Credits, Info and playerLost classes (states)
public interface Fonts {

    java.awt.Font awtFont1 = new java.awt.Font("Comic Sans MS", java.awt.Font.BOLD, 40);
    TrueTypeFont ttfFont1 = new TrueTypeFont(awtFont1, true);

    java.awt.Font awtFont2 = new java.awt.Font("Comic Sans MS", java.awt.Font.BOLD, 22);
    TrueTypeFont ttfFont2 = new TrueTypeFont(awtFont2, true);

    java.awt.Font awtFont3 = new java.awt.Font("Comic Sans MS", java.awt.Font.PLAIN, 20);
    TrueTypeFont ttfFont3 = new TrueTypeFont(awtFont3, true);
}
