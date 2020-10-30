import java.awt.*;
import java.util.ArrayList;

/**
 * Sounds like what it is.  The first screen that is opened when the game is run.  Allows the user to login as either
 * a student, a teacher, and an admin.
 */
public class State_StartScreen extends State {

    private final ArrayList<Button> buttons;
    private int a;
    private final int w;
    private final int h; // alpha for colors, width, and height of screen
    private boolean buttonsAdded,resetCursor; // The buttons for student, teacher, and admin logins.


    /**
     * Creates a new State_StartScreen. This is called as soon as the game is opened.
     * @param game - Game object
     */
    State_StartScreen(Game game) {
        super(game);
        a = 10; //Starting alpha for most screen components.  Increases as the screen fades in.
        w = game.width;
        h = game.height;
        buttonsAdded = false;
        resetCursor = true;
        buttons = new ArrayList<>();
    }

    /**
     * Updated every frame which will update any information and check to see
     * what needs to be changed based on the user input.
     */
    public void tick(){

        if(a < 240){ //This fades in the colors.
            a+=2;
        }

        resetCursor = true;
        //This checks each button in this state and advances to the next state if clicked.
        for(int i = 0; i < buttons.size();i++){
            if(buttons.get(i).checkButtonHovered(game)){
                resetCursor = false;
                if(MouseManager.mousePressed){
                    PlayMusic.playSound(game,Assets.buttonClick);
                    game.setCursor(true);
                    if(i==0){
                        State.setState(new State_Login_Student(game));
                    }else if(i==1){
                        State.setState(new State_Login_Teacher(game));
                    }else{
                        State.setState(new State_Login_Admin(game));
                    }
                }
            }
        }
        if(resetCursor){
            game.setCursor(true);
        }
    }

    /**
     * Updated every frame which will update any drawings and check to see
     * what needs to be changed based on the user input.
     * @param g - Graphics2D
     */
    public void render(Graphics g){
        //This section adds all of the buttons with the attributes listed here.
        if(!buttonsAdded){
            buttons.add(new Button(game,g,0,null,"Student Login",(int)(w*.38),(int)(h*.58),(int)(w*.24),(int)(h*.10),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),35,10,false,true));
            buttons.add(new Button(game,g,0,null,"Teacher Login",(int)(w*.38),(int)(h*.71),(int)(w*.24),(int)(h*.10),Colors.ColorAlpha(Colors.darkReds,255),
                    Colors.ColorAlpha(Colors.darkReds,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),35,10,false,true));
            buttons.add(new Button(game,g,0,null,"Admin Login",(int)(w*.38),(int)(h*.84),(int)(w*.24),(int)(h*.10),Colors.ColorAlpha(Colors.blacks,255),
                    Colors.ColorAlpha(Colors.blacks,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),35,10,false,true));
            buttonsAdded = true; //The buttons are all added.
        }

        drawWelcome(g); // draws the welcome message
        Button.drawButtons(g,game,buttons,0,0);// Draws the button
        Button.drawButtonShine(g,game);
        g.drawImage(Assets.stars.get(((Math.abs(game.counter))%29)+1),(int)(w*0.33)-50,(int)(h*0.535)-115,null);
        g.drawImage(Assets.stars.get(((Math.abs(game.counter))%29)+1),(int)(w*0.67)-50,(int)(h*0.535)-115,null);
    }

    /**
     * the drawWelcome method draws the welcome message onto the screen.  It just says welcome to the game.  It fades
     * in after a short delay.
     * @param g - Graphics object.
     */
    private void drawWelcome(Graphics g){
        g.setColor(Colors.ColorAlpha(Colors.whites,a));
        g.drawRoundRect((int)(w*0.10)-1,(int)(h*0.10)-1,(int)(w*0.8)+2,(int)(h*0.8)+2,w/50,w/50);
        g.setColor(Colors.ColorAlpha(Colors.blacks,a));
        g.fillRoundRect((int)(w*0.10),(int)(h*0.10),(int)(w*0.8),(int)(h*0.8),w/50,w/50);

        g.setFont(game.font100);
        g.setColor(Colors.ColorAlpha(Colors.lightBlues,a));
        g.drawString("Welcome to",Display.centerString("Welcome to",g,game.font100,game),(int)(h*0.15)+(int)(h*0.1));
        g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
        g.drawString("MATH BYTES",Display.centerString("MATH BYTES",g,game.font100,game),(int)(h*0.402)+(int)(15*Math.sin(game.fastConstantCounter)));
    }

}
