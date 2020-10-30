import java.awt.*;
import java.util.ArrayList;

public class State_Add_Student extends State{

    private final ArrayList<Button> buttons;
    private int a;
    private final int w;
    private final int h;
    private boolean buttonsAdded;
    private final CurrentTeacherData data;
    private final TextBox userNameEntry;
    private final TextBox passWordEntry;
    private final TextBox fullNameEntry;
    private int errorMessage;

    /**
     * Initializes the game state needed to add new teachers.
     * @param game Current game state
     * @param data Admin data
     */
    public State_Add_Student(Game game,CurrentTeacherData data) {
        super(game);

        this.a=0;
        this.w = game.width;
        this.h = game.height;
        this.data = data;
        this.fullNameEntry = new TextBox((int)(w*0.35),(int)(h*0.6),(int)(w*0.35),(int)(h*0.06),20,35);
        this.userNameEntry = new TextBox((int)(w*0.35),(int)(h*0.4),(int)(w*0.35),(int)(h*0.06),20,35);
        this.passWordEntry = new TextBox((int)(w*0.35),(int)(h*0.5),(int)(w*0.35),(int)(h*0.06),20,35);
        this.errorMessage = 0;
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
        boolean resetCursor = true;
        if(userNameEntry.isHovered(game.mouseX,game.mouseY)){ // Handles mouse detection on the buttons
            resetCursor = false;
            game.setCursor(false);
            if(MouseManager.mousePressed){ // Allows admin to click on the box to set a teacher's username
                KeyboardManager.keyPressed = false;
                errorMessage = 0;
                PlayMusic.playSound(game,Assets.buttonClick);
                passWordEntry.setActive(false);
                fullNameEntry.setActive(false);
                userNameEntry.setActive(true);
            }

        }
        if(passWordEntry.isHovered(game.mouseX,game.mouseY)){
            resetCursor = false;
            game.setCursor(false);
            if(MouseManager.mousePressed){ // Allows admin to click on the box to set a teacher's password
                KeyboardManager.keyPressed = false;
                errorMessage = 0;
                PlayMusic.playSound(game,Assets.buttonClick);
                userNameEntry.setActive(false);
                fullNameEntry.setActive(false);
                passWordEntry.setActive(true);
            }
        }
        if(fullNameEntry.isHovered(game.mouseX,game.mouseY)){
            resetCursor = false;
            game.setCursor(false);
            if(MouseManager.mousePressed){ // Allows admin to click on the box to set a teacher's password
                KeyboardManager.keyPressed = false;
                errorMessage = 0;
                PlayMusic.playSound(game,Assets.buttonClick);
                userNameEntry.setActive(false);
                fullNameEntry.setActive(true);
                passWordEntry.setActive(false);
            }
        }
        if(userNameEntry.isHovered(game.mouseX,game.mouseY)||passWordEntry.isHovered(game.mouseX,game.mouseY)||fullNameEntry.isHovered(game.mouseX,game.mouseY)){
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

        if(buttons.get(0).checkButtonHovered(game)&&MouseManager.mousePressed){
            PlayMusic.playSound(game,Assets.buttonClick);
            if(userNameEntry.getInputString().length()<5){
                errorMessage = -4;
            }else if(passWordEntry.getInputString().length()<5){
                errorMessage = -3;
            }else if(fullNameEntry.getInputString().length()<5){
                errorMessage = -2;
            }else{
                boolean b = Users.makeNewStudent(userNameEntry.getInputString(),passWordEntry.getInputString(),fullNameEntry.getInputString(),game,data.username);
                data.students.add(Users.loadStudentData(userNameEntry.getInputString()));
                if (b){
                    errorMessage = 1; // It worked, new teacher added!
                }else{
                    errorMessage = -1; // This teacher already exists
                }
            }
            userNameEntry.setActive(false);
            fullNameEntry.setActive(false);
            passWordEntry.setActive(false);
        }

        if(buttons.get(1).checkButtonHovered(game)&&MouseManager.mousePressed){
            PlayMusic.playSound(game,Assets.buttonClick);
            State.setState(new State_Summary_Teacher(game,data.username));
        }
    }

    /**
     * Updated every frame which will update any drawings and check to see
     * what needs to be changed based on the user input.
     * @param g - Graphics2D
     */
    public void render(Graphics g){
        if(!buttonsAdded){
            buttons.add(new Button(game,g,0,null,"Create",(int)(w*0.65),(int)(h*0.84),(int)(w*0.20),(int)(h*0.12),Colors.ColorAlpha(Colors.darkBlues,255),
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
        Button.drawButtons(g,game,buttons,0,0);
        Button.drawButtonShine(g,game);
        fullNameEntry.drawTextBox(game,g);
        userNameEntry.drawTextBox(game,g);
        passWordEntry.drawTextBox(game,g);

        if(errorMessage == -1){
            g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
            g.setFont(game.font30);
            g.drawString("User already exists: "+userNameEntry.getInputString(),(int)(w*0.3),(int)(h*0.7));
        }else if(errorMessage == 1){
            g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
            g.setFont(game.font30);
            g.drawString("Successfully added: \""+userNameEntry.getInputString()+"\"",(int)(w*0.3),(int)(h*0.7));
        }else if(errorMessage == -4){
            g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
            g.setFont(game.font30);
            g.drawString("Username too short: \""+userNameEntry.getInputString()+"\"",(int)(w*0.3),(int)(h*0.7));
        }else if(errorMessage == -3){
            g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
            g.setFont(game.font30);
            g.drawString("Password too short: \""+passWordEntry.getInputString()+"\"",(int)(w*0.3),(int)(h*0.7));
        }else if(errorMessage == -2){
            g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
            g.setFont(game.font30);
            g.drawString("Full name too short: \""+fullNameEntry.getInputString()+"\"",(int)(w*0.3),(int)(h*0.7));
        }
    }

    /**
     * the drawBackground method draws the background to the screen. It fades
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
        g.drawString("Enter the student's full name",Display.centerString("Enter the student's full name",g,game.font60,game),(int)(h*0.22));
        g.drawString("and a username and password",Display.centerString("and a username and password",g,game.font60,game),(int)(h*0.32));

        g.setColor(Colors.ColorAlpha(Colors.lightBlues,a));
        g.setFont(game.font30);
        g.drawString("Name (EX. Nate K.): ",(int)(game.width*0.125),(int)(game.height*0.64));
        g.drawString("Username: ",(int)(game.width*0.23),(int)(game.height*0.44));
        g.drawString("Password: ",(int)(game.width*0.23),(int)(game.height*0.54));
    }
}