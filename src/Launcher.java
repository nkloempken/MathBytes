import java.awt.*;

/**
 * The Launcher class holds the main method which is what makes a new Game object and begins the loop. Sets the width,
 * height and title of the game (Although currently, the window is not resizable other than auto fitting to screen size)
 *
 * @author Nathan Kloempken
 * @author Jack
 * @author Julian
 *
 * @version 0.10
 */
class Launcher {

    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int WINDOW_WIDTH = (int)(screenSize.getWidth()/1.25);
    private static final int WINDOW_HEIGHT = (int)(screenSize.getHeight()/1.25);

    /**
     * The main method just makes and starts a new Game.
     * @param args - main
     */
    public static void main(String[] args){
        Game game = new Game("Math Game",WINDOW_WIDTH,WINDOW_HEIGHT);
        game.start();
    }

}