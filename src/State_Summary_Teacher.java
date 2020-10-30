import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class State_Summary_Teacher extends State{

    private final ArrayList<Button> buttons; //All buttons
    private int a;
    private final int w;
    private final int h;
    private boolean buttonsAdded, resetCursor,gameSaved;
    private final CurrentTeacherData data;

    /**
     * Initializes the main game.
     * @param game Main game state
     * @param username Username of current student player
     */
    public State_Summary_Teacher(Game game,String username) {
        super(game);

        this.a=0;
        this.w = game.width;
        this.h = game.height;
        this.data = Users.loadTeacherData(username);
        this.resetCursor = true;
        game.musicVolume = (int)((((double) Objects.requireNonNull(data).volume/100.0)*31)-25);
        PlayMusic.gainControl.setValue((int)((((double)data.volume/100.0)*31)-25)-8);
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
        if(!gameSaved){
            Users.saveTeacher(data);
            gameSaved = true;
        }

        resetCursor = true;
        for(int i = 0; i < buttons.size();i++){
            if(buttons.get(i).checkButtonHovered(game)){
                resetCursor = false;
                if(MouseManager.mousePressed){
                    PlayMusic.playSound(game,Assets.buttonClick);
                    game.setCursor(true);
                    if(i==0){
                        State.setState(new State_List_Students(game,data));
                    }else if(i==1){
                        State.setState(new State_Add_Student(game,data));
                    }else if (i==2){
                        State.setState(new State_Settings(game,data.username,"T",null,data,null));
                    }else if(i==3){
                        State.setState(new State_StartScreen(game));
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
        if(!buttonsAdded){
            buttons.add(new Button(game,g,0,null,"Student List",(int)(w*.38),(int)(h*0.45),(int)(w*.24),(int)(h*.10),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),35,10,false,true));
            buttons.add(new Button(game,g,0,null,"Add New Student",(int)(w*.38),(int)(h*.58),(int)(w*.24),(int)(h*.10),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),35,10,false,true));
            buttons.add(new Button(game,g,0,null,"Settings",(int)(w*.38),(int)(h*.71),(int)(w*.24),(int)(h*.10),Colors.ColorAlpha(Colors.darkReds,255),
                    Colors.ColorAlpha(Colors.darkReds,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),35,10,false,true));
            buttons.add(new Button(game,g,0,null,"Log Out",(int)(w*.38),(int)(h*.84),(int)(w*.24),(int)(h*.10),Colors.ColorAlpha(Colors.blacks,255),
                    Colors.ColorAlpha(Colors.blacks,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),35,10,false,true));
            buttonsAdded = true;
        }

        drawBackground(g);
        Button.drawButtons(g,game,buttons,0,0);
        Button.drawButtonShine(g,game);
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
        g.drawString("Welcome: "+data.username,Display.centerString
                ("Welcome: "+data.username,g,game.font60,game),(int)(h*0.24));
    }
}