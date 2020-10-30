import java.awt.*;
import java.util.ArrayList;

public class State_Settings extends State{

    private final ArrayList<Button> buttons;
    private int a;
    private final int w;
    private final int h;
    private boolean buttonsAdded,resetCursor;
    private final String userUsername;
    private final String userType;
    private final CurrentStudentData dataS;
    private final CurrentTeacherData dataT;
    private final CurrentAdminData dataA;


    /**
     * Initializes the settings state.
     * @param game Initial state in the settings screen
     */
    public State_Settings(Game game,String userUsername, String userType, CurrentAdminData dataA, CurrentTeacherData dataT, CurrentStudentData dataS) {
        super(game);

        this.a=0;
        this.w = game.width;
        this.h = game.height;
        this.resetCursor = true;
        this.userUsername = userUsername;
        this.userType = userType;
        this.dataA = dataA;
        this.dataT = dataT;
        this.dataS = dataS;
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
        resetCursor = !buttons.get(0).checkButtonHovered(game);
        if(buttons.get(1).checkButtonHovered(game)){
            resetCursor = false;
        }
        if(buttons.get(2).checkButtonHovered(game)){
            resetCursor = false;
        }
        if(buttons.get(0).checkButtonHovered(game)&&MouseManager.mousePressed){
            game.setCursor(true);
            PlayMusic.playSound(game,Assets.buttonClick);
            switch (userType) {
                case "A":
                    dataA.volume = (int) ((((double) game.musicVolume + 25.0) / 31.0) * 100.0);
                    Users.saveAdmin(dataA);
                    State.setState(new State_Summary_Admin(game, userUsername));
                    break;
                case "T":
                    dataT.volume = (int) ((((double) game.musicVolume + 25.0) / 31.0) * 100.0);
                    Users.saveTeacher(dataT);
                    State.setState(new State_Summary_Teacher(game, userUsername));
                    break;
                default:
                    dataS.volume = (int) ((((double) game.musicVolume + 25.0) / 31.0) * 100.0);
                    Users.saveStudent(dataS,-1);
                    State.setState(new State_MissionSelect(game, userUsername));
                    break;
            }
        }
        if(buttons.get(1).checkButtonHovered(game)&&MouseManager.mousePressed&&game.musicVolume<6){
            PlayMusic.playSound(game,Assets.buttonClick);
            game.musicVolume+=1;
        }
        if(buttons.get(2).checkButtonHovered(game)&&MouseManager.mousePressed &&game.musicVolume>-25){
            PlayMusic.playSound(game,Assets.buttonClick);
            game.musicVolume-=1;
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
            buttons.add(new Button(game,g,0,null,"Back",(int)(w*0.15),(int)(h*0.84),(int)(w*0.20),(int)(h*0.12),Colors.ColorAlpha(Colors.darkReds,255),
                    Colors.ColorAlpha(Colors.darkReds,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),65,15,false,true));
            buttons.add(new Button(game,g,0,null,"+",(int)(w*0.58),(int)(h*0.45),(int)(w*0.06),(int)(h*0.06),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),65,15,false,true));
            buttons.add(new Button(game,g,0,null,"-",(int)(w*0.58),(int)(h*0.57),(int)(w*0.06),(int)(h*0.06),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),65,15,false,true));
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
        g.setFont(game.font100);
        g.drawString("Settings",Display.centerString("Settings",g,game.font100,game),(int)(h*0.24));
        g.setFont(game.font60);
        g.drawString("Volume : ",(int)(w*0.22),(int)(h*0.50));
        g.setColor(Colors.ColorAlpha(Colors.lightBlues,a));
        g.setFont(game.font100);
        g.drawString(""+(int)((((double)game.musicVolume+25.0)/31.0)*100.0),(int)(w*0.45),(int)(h*0.50));
    }
}