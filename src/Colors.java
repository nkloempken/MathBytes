import java.awt.*;
import java.util.ArrayList;

/**
 * Class that stores Color objects so that they are created only once.
 */
abstract class Colors {

    public static final Color semiTransparentGreen,shineColor,greyedOut;
    public static final ArrayList<Color> lightBlues;
    public static final ArrayList<Color> lightReds;
    public static final ArrayList<Color> darkReds;
    public static final ArrayList<Color> darkBlues;
    public static final ArrayList<Color> yellows;
    public static final ArrayList<Color> whites;
    public static final ArrayList<Color> offWhites;
    public static final ArrayList<Color> blacks;
    public static final ArrayList<Color> greens;


    //The static block initializes all static variables.
    static {
        semiTransparentGreen = new Color(0,255,0,130);
        shineColor = new Color(255,255,255,50);
        greyedOut = new Color(120,120,120,180);
        lightReds = new ArrayList<>();
        lightBlues = new ArrayList<>();
        darkReds = new ArrayList<>();
        darkBlues = new ArrayList<>();
        whites = new ArrayList<>();
        blacks = new ArrayList<>();
        yellows = new ArrayList<>();
        offWhites = new ArrayList<>();
        greens = new ArrayList<>();

        //This is where a color with each possible alpha value is stored
        for(int i = 0; i < 256;i++){
            lightBlues.add(new Color(130,150,200,i));
            lightReds.add(new Color(230,100,100,i));
            whites.add(new Color(255,255,255,i));
            blacks.add(new Color(0,0,0,i));
            yellows.add(new Color(0,170,20,i));
            darkBlues.add(new Color(10,30,100,i));
            darkReds.add(new Color(100,10,10));
            offWhites.add(new Color(210,210,250,i));
            greens.add(new Color(255,255,0,i));
        }

    }

    /**
     * Returns the alpha version of any given color, which makes it some degree of transparent depending on the int
     * @param baseColor The original color
     * @param alphaDesired The desired alpha version
     * @return The alpha version of the color
     */
    public static Color ColorAlpha(ArrayList<Color> baseColor, int alphaDesired) {
        return baseColor.get(alphaDesired);
    }

}