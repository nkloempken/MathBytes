import java.awt.*;
import java.util.ArrayList;

public class State_Mission_Summary extends State{

    private final ArrayList<Button> buttons;
    private int a;
    private final int w;
    private final int h;
    private int numRight;
    private boolean buttonsAdded,resetCursor;
    private final ArrayList<String[]> missionHistory;
    private final State destination;
    private final Color outOf;

    /**
     * Creates a new teacher list state
     * @param game Game object
     * @param missionHistory - Student's Mission History
     */
    public State_Mission_Summary(Game game,State returnState, ArrayList<String[]> missionHistory) {
        super(game);
        this.a=0;
        this.w = game.width;
        this.h = game.height;
        this.missionHistory = missionHistory;
        this.resetCursor = true;
        this.buttonsAdded = false;
        this.destination = returnState;
        buttons = new ArrayList<>(); //All buttons
        numRight = 0;
        for (String[] aMissionHistory : missionHistory) {
            if (aMissionHistory[1].equals(aMissionHistory[2])) {
                numRight++;
            }
        }
        outOf = Colors.ColorAlpha(Colors.greens,255);
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
        if(buttonsAdded&&buttons.get(0).checkButtonHovered(game)&&MouseManager.mousePressed){
            game.setCursor(true);
            PlayMusic.playSound(game,Assets.buttonClick);
            State.setState(destination);
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
            buttons.add(new Button(game,g,0,null,"Exit",(int)(w*0.76),(int)(h*0.11),(int)(w*0.10),(int)(h*0.08),Colors.ColorAlpha(Colors.darkReds,255),
                    Colors.ColorAlpha(Colors.darkReds,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),35,15,false,true));
            buttonsAdded = true;
        }
        drawBackground(g);
        Button.drawButtons(g,game,buttons,0,0);
        Button.drawButtonShine(g,game);
        drawProblemText(g);
    }

    /**
     * the drawBackground method draws the background to the screen. It fades
     * in after a short delay.
     * @param g - Graphics object.
     */
    private void drawBackground(Graphics g){
        g.setColor(Colors.ColorAlpha(Colors.whites,a));
        g.drawRoundRect((int)(w*0.10)-1,(int)(h*-0.10)-1,(int)(w*0.8)+2,(int)(h*1.2)+2,w/50,w/50);
        g.setColor(Colors.ColorAlpha(Colors.blacks,a));
        g.fillRoundRect((int)(w*0.10),(int)(h*-0.10),(int)(w*0.8),(int)(h*1.2),w/50,w/50);

        g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
        g.setFont(game.font60);
        g.drawString("Mission Summary: ",Display.centerString("Mission Summary: ",g,game.font60,game),(int)(h*0.10));

        g.setFont(game.font50);
        g.setColor(outOf);
        g.drawString(""+numRight+"  /  "+ missionHistory.size(),Display.centerString(""+numRight+"  /  "+ missionHistory.size(),g,game.font50,game),(int)(h*0.17));

        g.setFont(game.font30);
        g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
        g.drawString("(  Question   ---   Correct Answer   ---  Given Answer  )",Display.centerString("(  Question   ---   Correct Answer   ---  Given Answer  )",g,game.font30,game),(int)(h*0.24));
    }

    private void drawProblemText(Graphics g) {
        g.setFont(game.font30);
        int z;
        for(int i = 1; i < 3;i++){
            for(int j = 0; j<10;j++){
                z = ((i-1)*10)+j;
                if(z<missionHistory.size()){
                    if(missionHistory.get(z)[1].equals(missionHistory.get(z)[2])){
                        g.setColor(Colors.ColorAlpha(Colors.yellows,a));
                    }else{
                        g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
                    }

                    String str0 = missionHistory.get(z)[0];
                    String str1 = missionHistory.get(z)[1];
                    String str2 = missionHistory.get(z)[2];
                    if(str2.indexOf("0")==0&&str2.length()>1&&str1.length()>1){
                        str1 = str1.substring(1);
                        str2 = str2.substring(1);
                    }
                    if(str2.indexOf("0")==0&&str2.length()>1&&str1.length()>1){
                        str1 = str1.substring(1);
                        str2 = str2.substring(1);
                    }
                    g.drawString(str0+"  =      "+str2+"        "+str1,(int)(w*0.256)+(int)((i-1)*w*0.30),(int)(h*0.32)+(int)(h*j*0.065));
                }
            }
        }
    }
}
