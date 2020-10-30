import java.awt.*;
import java.util.ArrayList;

public class State_Login_Admin extends State{

    private final ArrayList<Button> buttons;
    private int a;
    private final int w;
    private final int h;
    private final TextBox usernameBox;
    private final TextBox passwordBox;
    private boolean resetCursor,buttonsAdded;
    private int errorMessage;

    /**
     * Initializes admin login state
     * @param game Game object
     */
    public State_Login_Admin(Game game) {
        super(game);
        buttonsAdded = false;

        this.usernameBox = new TextBox((int)(game.width*0.29),(int)(game.height*0.43),(int)(game.width*0.42),(int)(game.height*.06),20,30);
        this.passwordBox = new TextBox((int)(game.width*0.29),(int)(game.height*0.58),(int)(game.width*0.42),(int)(game.height*.06),20,30);
        usernameBox.setActive(true);

        this.a=0;
        this.w = game.width;
        this.h = game.height;
        this.resetCursor = true;
        errorMessage = 1;
        buttons = new ArrayList<>();
    }

    /**
     * Updated every frame which will update any information and check to see
     * what needs to be changed based on the user input.
     */
    public void tick() {
        if(a < 240){ //This fades in the colors.
            a+=4;
        }
        resetCursor = true;
        if(usernameBox.isHovered(game.mouseX,game.mouseY)){
            resetCursor = false;
            game.setCursor(false);
            if(MouseManager.mousePressed){
                PlayMusic.playSound(game,Assets.buttonClick);
                passwordBox.setActive(false);
                usernameBox.setActive(true);
            }

        }
        if(passwordBox.isHovered(game.mouseX,game.mouseY)){
            resetCursor = false;
            game.setCursor(false);
            if(MouseManager.mousePressed){
                PlayMusic.playSound(game,Assets.buttonClick);
                usernameBox.setActive(false);
                passwordBox.setActive(true);
            }
        }
        if(usernameBox.isHovered(game.mouseX,game.mouseY)||passwordBox.isHovered(game.mouseX,game.mouseY)){
            resetCursor = false;
        }
        if(buttons.get(0).checkButtonHovered(game)){
            resetCursor = false;
        }
        if(buttons.get(1).checkButtonHovered(game)){
            resetCursor = false;
        }
        if(resetCursor){
            game.setCursor(true);
        }

        if((buttons.get(0).checkButtonHovered(game)&&MouseManager.mousePressed)) {
            PlayMusic.playSound(game,Assets.buttonClick);
            int result = Users.signIn(usernameBox.getInputString(),passwordBox.getInputString(), "A");
            if(result==1){
                State.setState(new State_Summary_Admin(game,usernameBox.getInputString()));
                game.setCursor(true);
            }else if (result == 0){
                errorMessage = 0;
            }else{
                errorMessage = -1;
            }

        }
        if(buttons.get(1).checkButtonHovered(game)&&MouseManager.mousePressed){
            PlayMusic.playSound(game,Assets.buttonClick);
            game.setCursor(true);
            State.setState(new State_StartScreen(game));
        }
    }

    /**
     * Updated every frame which will update any drawings and check to see
     * what needs to be changed based on the user input.
     * @param g - Graphics2D
     */
    public void render(Graphics g){
        if(!buttonsAdded){
            buttons.add(new Button(game,g,0,null,"Login",(int)(w*0.52),(int)(h*0.84),(int)(w*0.20),(int)(h*0.14),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),65,15,false,true));
            buttons.add(new Button(game,g,0,null,"Back",(int)(w*0.15),(int)(h*0.84),(int)(w*0.20),(int)(h*0.12),Colors.ColorAlpha(Colors.darkReds,255),
                    Colors.ColorAlpha(Colors.darkReds,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),65,15,false,true));
            buttonsAdded = true;
        }
        drawBackground(g);
        usernameBox.drawTextBox(game,g);
        passwordBox.drawTextBox(game,g);
        Button.drawButtons(g,game,buttons,0,0);
        Button.drawButtonShine(g,game);
        if(errorMessage == 0){
            g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
            g.setFont(game.font30);
            g.drawString("Incorrect password",(int)(w*0.16),(int)(h*0.69)+(int)(12*Math.sin(game.fastConstantCounter)));
        }else if(errorMessage == -1){
            g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
            g.setFont(game.font30);
            g.drawString("User does not exist",(int)(w*0.16),(int)(h*0.69)+(int)(12*Math.sin(game.fastConstantCounter)));
        }
    }

    /**
     * The drawBackground method draws the background onto the screen.  It fades
     * in after a short delay.
     * @param g - Graphics object.
     */
    private void drawBackground(Graphics g){
        g.setColor(Colors.ColorAlpha(Colors.whites,a));
        g.drawRoundRect((int)(w*0.10)-1,(int)(h*0.10)-1,(int)(w*0.8)+2,(int)(h*0.8)+2,w/50,w/50);
        g.setColor(Colors.ColorAlpha(Colors.blacks,a));
        g.fillRoundRect((int)(w*0.10),(int)(h*0.10),(int)(w*0.8),(int)(h*0.8),w/50,w/50);

        g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
        g.setFont(game.font60);
        g.drawString("Please enter your username",Display.centerString("Please enter your username",g,game.font60,game),(int)(game.height*0.24));
        g.drawString("and password",Display.centerString("and password",g,game.font60,game),(int)(game.height*0.34));

        g.setColor(Colors.ColorAlpha(Colors.lightBlues,a));
        g.setFont(game.font30);
        g.drawString("Username: ",(int)(game.width*0.16),(int)(game.height*0.47));
        g.drawString("Password: ",(int)(game.width*0.16),(int)(game.height*0.62));
    }
}
