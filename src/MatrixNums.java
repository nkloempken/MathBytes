import java.awt.*;

/**
 * The MatrixNums class is used to create objects that will be drawn the the screen.  These objects are arrays of
 * numbers which can either be 0 or 1.  These string of numbers are drawn on the screen as though they are falling down.
 */
class MatrixNums {

    private final int xLoc;
    private int yLoc;
    private final int speed; // X and Y location on screen (Top left corner of top number)
    private int vertOffset;
    private int speedInc;

    private final int[] numbers; // The array that holds the 0 and 1 values for however long it is.

    /**
     * The constructor makes a new MatrixNums object which will be drawn to the screen by the Game object.
     * @param game - game
     * @param g - Graphics object that the game is using to draw
     */
    public MatrixNums(Game game, Graphics g ){
        numbers = new int[(int)(Math.random()*15)+4]; // New array of 1s and 0s with random length from 4 to 19
        xLoc = (int)((Math.random()*(game.width-20))+5); // X location is anywhere from far left to far right of the screen.
        yLoc = -1*(g.getFontMetrics().getHeight()*numbers.length)-20; //Y location is just off-screen to start.
        speed = (int)(Math.random()*2)+4;
        vertOffset = g.getFontMetrics().getHeight();
        speedInc = speed/3;
        //Generate the 0s and 1s
        for(int i = 0; i < numbers.length;i++){
            numbers[i] = (int)(Math.random()*2);
        }
    }

    /**
     * The draw method is used to actually draw a given MatrixNums to the screen.  The color and font must be set by
     * the game, and then this method needs to be called for each instance of a MatrixNums object every tick.
     * @param game - game
     * @param g - Graphics object the game is using.
     * @return - boolean: true if is not yet offscreen.  False if offscreen - game will need to dispose of it.
     */
    public boolean draw(Game game, Graphics g){
        //Draw the numbers to the screen for a given MatrixNums.
        for(int i = 0; i < numbers.length;i++){
            g.drawString(""+numbers[i],xLoc,yLoc+i*vertOffset);
        }
        yLoc+=(speedInc); //Move the text down slightly every tick.
        //Returns false if the numbers are all offscreen in a given MatrixNums.
        return yLoc < (game.height + 11);
    }

}