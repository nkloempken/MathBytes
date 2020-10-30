import java.awt.*;
import java.util.ArrayList;

public class State_List_Teachers extends State{

    private final ArrayList<Button> buttons;
    private int a;
    private final int w;
    private final int h;
    private int listLocation;
    private boolean buttonsAdded,resetCursor;
    private final CurrentAdminData data;

    /**
     * Creates a new teacher list state
     * @param game Game object
     * @param data Admin data
     */
    public State_List_Teachers(Game game,CurrentAdminData data) {
        super(game);

        this.a=0;
        this.w = game.width;
        this.h = game.height;
        this.data = data;
        this.resetCursor = true;
        this.listLocation = 0;
        this.buttonsAdded = false;
        buttons = new ArrayList<>(); //All buttons
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
        for (Button button : buttons) {
            if (button.checkButtonHovered(game)) {
                resetCursor = false;
            }
        }
        if(buttons.get(0).checkButtonHovered(game)&&MouseManager.mousePressed){
            game.setCursor(true);
            PlayMusic.playSound(game,Assets.buttonClick);
            State.setState(new State_Summary_Admin(game,data.username));
        }
        if(buttons.get(1).checkButtonHovered(game)&&MouseManager.mousePressed&&listLocation>0){
            PlayMusic.playSound(game,Assets.buttonClick);
            listLocation--;
        }
        if(buttons.get(2).checkButtonHovered(game)&&MouseManager.mousePressed&&listLocation<(data.teachers.size()-4)){
            PlayMusic.playSound(game,Assets.buttonClick);
            listLocation++;
        }
        for(int i = 3; i < buttons.size();i++){
            if(buttons.get(i).checkButtonHovered(game)&&MouseManager.mousePressed){
                PlayMusic.playSound(game,Assets.buttonClick);
                game.setCursor(true);
                String userToDelete = data.teachers.get(i+listLocation-3).username;
                Users.removeUser(userToDelete,game,"T");
                data.teachers.remove(i+listLocation-3);
                if(this.data.teachers.size()<4){
                    buttons.remove(buttons.size()-1);
                }
                if(listLocation >0&&(listLocation == data.teachers.size()-3)){
                    listLocation--;
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
            buttons.add(new Button(game,g,0,null,"Back",(int)(w*0.76),(int)(h*0.21),(int)(w*0.10),(int)(h*0.08),Colors.ColorAlpha(Colors.darkReds,255),
                    Colors.ColorAlpha(Colors.darkReds,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),35,15,false,true));
            buttons.add(new Button(game,g,0,null,"^",(int)(w*0.75),(int)(h*0.42),(int)(w*0.08),(int)(h*0.08),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),65,15,false,true));
            buttons.add(new Button(game,g,0,null,"v",(int)(w*0.75),(int)(h*0.82),(int)(w*0.08),(int)(h*0.10),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),55,15,false,true));
            for(int i = 0; i < 4;i++){
                if(data.teachers.size()>i){
                    buttons.add(new Button(game,g,0,null,"REMOVE",(int)(w*0.13),(((int)(h*0.39))+(int)(h*0.15)*i),(int)(w*0.13),(int)(h*0.09),Colors.ColorAlpha(Colors.whites,255),
                            Colors.ColorAlpha(Colors.whites,255),Colors.ColorAlpha(Colors.darkReds,190),
                            Colors.ColorAlpha(Colors.darkReds,255),Colors.ColorAlpha(Colors.whites,190),
                            Colors.ColorAlpha(Colors.whites,255),35,15,false,true));
                }
            }
            buttonsAdded = true;
        }
        drawBackground(g);
        drawTeacherList(g,game);
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
        g.setFont(game.font100);
        g.drawString("Teacher List: ",Display.centerString("Teacher List: ",g,game.font100,game),(int)(h*0.23));
    }

    private void drawTeacherList(Graphics g, Game game){
        for(int i = 0; i < 4; i ++){
            g.setColor(Colors.ColorAlpha(Colors.offWhites,255));
            g.fillRect((int)(w*0.28),(int)(h*0.294)+(((int)(h*0.15))*i),(int)(w*0.45),(int)(h*0.1));
            g.setColor(Colors.ColorAlpha(Colors.darkReds,a));
            g.fillRect((int)(w*0.28),(int)(h*0.294)+(((int)(h*0.15))*i),(int)(w*0.45),(int)(h*0.005));
            g.fillRect((int)(w*0.28),(int)(h*0.394)+(((int)(h*0.15))*i),(int)(w*0.45),(int)(h*0.005));
        }
        g.fillRect((int)(w*0.728),(int)(h*0.296),(int)(w*0.0028),(int)(h*0.55));
        g.fillRect((int)(w*0.28),(int)(h*0.296),(int)(w*0.0028),(int)(h*0.55));

        g.setFont(game.font25);
        g.setColor(Colors.ColorAlpha(Colors.darkBlues,a));
        for(int i = 0 ; i < 4; i ++){
            if(data.teachers.size()>i){
                g.drawString(""+(i+1+listLocation)+".",(int)(w*0.29),(int)(h*0.36)+(i*(int)(h*0.15)));
                g.drawString(data.teachers.get(i+listLocation).username,(int)(w*0.33),(int)(h*0.36)+((int)(h*0.15)*i));
                g.drawString("# of Students: "+data.teachers.get(i+listLocation).students.size()+" / 100",(int)(w*0.54),(int)(h*0.38)+((int)(h*0.15)*i));
                g.drawString(data.teachers.get(i+listLocation).realName,(int)(w*0.54),(int)(h*0.34)+((int)(h*0.15)*i));
            }
        }
    }
}