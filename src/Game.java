import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
/**
 * The Game class is called by the launcher and loops infinitely until the display window is closed by the user.
 */
class Game implements Runnable{

    //Fonts
    public final Font font25,font30,font40,font50,font60,font100;

    // Passed from Launcher
    public final int width, height;
    private final String title;

    // The mouseManager is the object that keeps track of whether or not the mouse has been clicked.
    private final MouseManager mouseManager;
    private final KeyboardManager keyboardManager;

    //running is used for looping, and secondTime for tracking mouse clicks.
    private boolean running, secondTime, thirdTime;

    private Display display; // The frame and canvas that the game will be drawn on.
    private Graphics g; // The graphics object which will be used to draw anything we need onto the canvas.
    private Thread thread; // The thread that can infinitely run tick and render to create the game loop.
    private BufferStrategy bs;

    public int musicVolume;
    public double mediumConstantCounter;
    public double fastConstantCounter;
    public double veryFastConstantCounter;
    private final ArrayList<MatrixNums> bars; // Makes the numbers effect in the background
    private final ArrayList<MatrixNums> barsToRemove; // Clears out bars of numbers when they aren't needed anymore
    private final Cursor defaultCursor;
    private final Cursor handCursor;

    public int mouseX,mouseY,counter;

    /**
     * The constructor for Game just sets the fields for a new Game object.
     * @param title - Title given by Launcher.
     * @param width - Width given by Launcher.
     * @param height - Height given by Launcher.
     */
    public Game(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
        secondTime = false;
        thirdTime = false;
        mouseManager = new MouseManager();
        keyboardManager = new KeyboardManager();
        bars = new ArrayList<>();
        bs = null;
        barsToRemove = new ArrayList<>();
        musicVolume = -10;
        font25 = new Font("sansserif",Font.BOLD,25);
        font30 = new Font("sansserif",Font.BOLD,30);
        font40 = new Font("sansserif",Font.BOLD,40);
        font50 = new Font("sansserif",Font.BOLD,47);
        font60 = new Font("sansserif",Font.BOLD,60);
        font100 = new Font("sansserif",Font.BOLD,100);
        mediumConstantCounter = 11;
        fastConstantCounter = 111;
        veryFastConstantCounter = 1111;
        defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        handCursor = new Cursor(Cursor.HAND_CURSOR);
        counter = 0;
    }

    /**
     * The init method makes a new display for the game to appear on, and sets the initial state to the menu.
     */
    private void init(){
        display = new Display(title, width, height);
        display.getCanvas().addMouseListener(mouseManager);
        display.getCanvas().addKeyListener(keyboardManager);

        //Begins the game in the State_StartScreen where the user can choose what game to play.
        State startScreen = new State_StartScreen(this);
        State.setState(startScreen);
    }

    /**
     * The tick method ticks 20 times per second and updates all information each time.  This one updates the mouse
     * position, and also calls the tick method of a State if there is currently one in use.
     */
    private void tick() {
        if(State.getState() != null){ // Checks to see if there is currently a State in use.
            State.getState().tick(); // If so, runs the tick method of that State as well.
        }
        veryFastConstantCounter+=0.133;
        fastConstantCounter+=0.0875;
        mediumConstantCounter+=0.033;
        try{
            mouseX = (int)MouseInfo.getPointerInfo().getLocation().getX() - getDisplay().getFrameX()-5;
            mouseY = (int)MouseInfo.getPointerInfo().getLocation().getY() - getDisplay().getFrameY();
        }catch(Exception e){
            mouseX = -1;
            mouseY = -1;
        }
    }

    /**
     * The render method ticks 20 times per second and updates all images on the screen each time.  This one uses a
     * BufferStrategy in order to smooth transitions between image changes, and then sets the graphics object
     * using that BufferStrategy.  The render begins by clearing everything off of the screen, and then redraws
     * the background of the Card table and players.  Then, if there is a State currently in use, calls the render
     * method of that State.  Finally, displays all of this on the canvas and then gets rid of the graphics object.
     * Sets the mousePressed boolean to false just in case and then begins again.
     */
    private void render() {
        // The BufferStrategy makes for smoother drawing animations.
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(4);
            return;
        }

        g = bs.getDrawGraphics(); // Sets the graphics object to use the newly set BufferStrategy.
        // Clears the entire canvas
        g.clearRect(0, 0, width, height);

        // Draw background present in all states.
        drawBackground();

        //If in a state, draw whatever should be drawn in that state.
        if(State.getState() != null){
            State.getState().render(g);
        }

        // Displays the drawn objects on front buffer to the screen.
        bs.show();
        g.dispose();
    }

    /**
     * This method draws the background.
     */
    private void drawBackground() {
        //This sets the base background as black and chooses a font style.
        g.setColor(Color.black);
        g.fillRect(0,0,width,height);
        g.setFont(font30);
        //This removal system seems wasteful and may be, but it was necessary to prevent flickering.
        if(barsToRemove.size()>0){
            for(int i = 0; i < barsToRemove.size();i++){
                bars.remove(barsToRemove.get(i));
                barsToRemove.remove(barsToRemove.get(i));
            }
        }
        //The decimal here is the chance that a new line appears each tick.
        if(counter<<26==0){
            bars.add(new MatrixNums(this,g));
        }

        //This draws the bars and also removes them if they are offscreen.
        g.setColor(Colors.semiTransparentGreen);
        for (MatrixNums bar : bars) {
            if (!(bar.draw(this, g))) {
                barsToRemove.add(bar);
            }
        }

    }

    /**
     * The start method is called in the main method, and begins the infinite thread loop.
     */
    public synchronized void start() {
        if (running) { // If the game is already running do nothing.
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Just in case this needs to be accessed from outside of this class.
     * @return - The display.
     */
    public Display getDisplay(){
        return this.display;
    }

    public void setCursor(boolean defaultC){
        if(defaultC){
            this.getDisplay().getCanvas().setCursor(defaultCursor);
        }else{
            this.getDisplay().getCanvas().setCursor(handCursor);
        }
    }

    /**
     * The stop method, when called, stops the thread from running and sets the running boolean to false;
     */
    private synchronized void stop() {
        if (!running) { // Checks to make sure the thread is even running.
            return;
        }
        running = false;
        try {
            thread.join(); // Kills the thread.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * The run method is the meat of the Game object.  It runs until the window is closed by the User, looping at a
     * given fps.
     */
    public void run() {
        init(); // Initializes the States and and display.
        int fps = 45; // Sets the default frame rate to 60 fps.

        double timePerTick = 1000000000.0/fps; // Converts the fps to nano-seconds
        double delta = 0; // Used to keep track of whether or not enough time has passed to loop again.

        long now;
        long lastTime = System.nanoTime();

        while (running) {
            now = System.nanoTime();
            delta += (now-lastTime)/timePerTick;
            lastTime = now;

            // All of the weird second time / third time stuff is where I fixed the mouse clicking issue.  Gives the cpu
            // 3 chances to notice a click before the variable is reset

            if(delta >= 1){ // If enough time has passed since the last time the game has looped.
                if(MouseManager.mousePressed){
                    if(!secondTime){
                        secondTime = true;
                    }else{
                        if(!thirdTime){
                            thirdTime = true;
                        }
                    }
                }
                tick();
                if(MouseManager.mousePressed){
                    if(!secondTime){
                        secondTime = true;
                    }else{
                        if(!thirdTime){
                            thirdTime = true;
                        }
                    }
                }
                render();
                counter++;
                delta -=1; // resets delta to 0 ish;
                if(MouseManager.mousePressed){
                    if(!secondTime){
                        secondTime = true;
                    }else if (!thirdTime) {
                        thirdTime = true;
                    }
                    if(thirdTime){
                        MouseManager.mousePressed = false;
                        secondTime = false;
                        thirdTime = false;
                    }
                }
            }
        }
        stop(); // stops the thread once the game is no longer running.
    }
}
