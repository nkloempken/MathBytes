import java.awt.*;

/**
 * The State class is abstract in that it is never used alone, but all other State classes use it as a framework
 * for how they are structured.  They all will contain the tick and render methods, will take in a game object,
 * and will give the game object the ability to get and set the State.
 */
public abstract class State {

    // Class variables
    private static State currentState = null; // No State called when the game opens.


    //Fields
    final Game game;

    /**
     * Gives the game object and all other States the ability to change the current state at any time.
     * @param state - The State that will become the new State.
     */
    public static void setState(State state){
        currentState = state; // Changes the current State to the given one.
        MouseManager.mousePressed = false;
    }

    /**
     * Gives the game object and all other states the ability to see which State is the current one.
     * @return currentState - the State that the game is in when called.
     */
    public static State getState(){
        return currentState; // Returns the current State.
    }

    /**
     * The default constructor which takes in the current game object and sets the reference within the
     * States classes to that game.
     * @param game - Game
     */
    State(Game game){
        this.game = game;
    }

    /**
     * All States will contain a tick method updated every frame which will update any information and check to see
     * what needs to be changed based on the user input.
     */
    public abstract void tick();

    /**
     * All States will contain a render method updated every frame which will update any drawings and check to see
     * what needs to be changed based on the user input.
     * @param g - Graphics2D
     */
    public abstract void render(Graphics g);

}