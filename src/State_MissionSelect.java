import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class State_MissionSelect extends State{

    private ArrayList<Button> buttons;
    private final ArrayList<Button> missionButtons;

    private int a1;
    private int a2;
    private final int w;
    private final int h;
    private int screenOffsetX;
    private int screenOffsetY;
    private int currentMission;
    private int moveToNextMission;
    private int currentWire;
    private int amtMoved;
    private int adjust;
    private boolean buttonsAdded, resetCursor,gameSaved,moving,reAddButtons,goButtonAdded;
    private final CurrentStudentData data;
    private final Color wireColorU;
    private final Color wireColorL;
    private final int[] wiresX;
    private final int[] wiresY;
    private final int[] wireLengths;
    private final int[] wireHeights;
    private final int[] wireDir;

    /**
     * Initializes the main game.
     * @param game Main game state
     * @param username Username of current student player
     */
    public State_MissionSelect(Game game,String username) {
        super(game);

        this.a1=0;
        this.a2=0;
        this.w = game.width;
        this.h = game.height;
        this.data = Users.loadStudentData(username);
        this.resetCursor = true;
        game.musicVolume = (int)((((double) Objects.requireNonNull(data).volume/100.0)*31)-25);
        PlayMusic.gainControl.setValue((int)((((double)data.volume/100.0)*31)-25)-8);
        buttons = new ArrayList<>();
        missionButtons = new ArrayList<>();
        wireColorU = new Color(50,200,40,100);
        wireColorL = new Color(180,50,40,100);
        wiresX = new int[Missions.NUMBER_OF_MISSIONS*3];
        wiresY = new int[Missions.NUMBER_OF_MISSIONS*3];
        wireLengths = new int[Missions.NUMBER_OF_MISSIONS*3];
        wireHeights = new int[Missions.NUMBER_OF_MISSIONS*3];
        wireDir = new int[Missions.NUMBER_OF_MISSIONS*3];
        moving = false;
        amtMoved = 0;
        adjust = 0;
        goButtonAdded = false;
        reAddButtons = false;

        currentMission = 1;
        currentWire = 0;
        makeWires();
        int offX = 0;
        int offY = 0;
        for(int i = 0; i < data.missionCompletion.length;i++){
            if(data.missionCompletion[i]>0){
                for(int j = 0; j < 3;j++){
                    if(wireDir[(3*i)+j]==1){
                        offX+=wireLengths[(3*i)+j];
                    }else if(wireDir[(3*i)+j]==2){
                        offY+=wireHeights[(3*i)+j];
                    }else if(wireDir[(3*i)+j]==3){
                        offX+=wireLengths[(3*i)+j];
                    }else if(wireDir[(3*i)+j]==4){
                        offY+=wireHeights[(3*i)+j];
                    }
                    currentWire++;
                }
                currentMission++;
            }
        }
        screenOffsetX -= offX;
        screenOffsetY -= offY;
    }

    /**
     * Updated every frame which will update any information and check to see
     * what needs to be changed based on the user input.
     */
    public void tick() {
        if(a1 < 240){ //This fades in the colors.
            a1+=4;
        }
        if(a2 < 250){ //This fades in the colors.
            a2+=3;
        }


        resetCursor = true;
        for (Button button : buttons) {
            if (button.checkButtonHovered(game)) {
                resetCursor = false;
            }
        }
        for (Button missionButton : missionButtons) {
            if (missionButton.checkButtonHovered(game)) {
                resetCursor = false;
            }
        }

        if(resetCursor) {
            game.setCursor(true);
        }

        if(buttons.size()>0&&buttons.get(0).checkButtonHovered(game)&&MouseManager.mousePressed){
            PlayMusic.playSound(game,Assets.buttonClick);
            State.setState(new State_StartScreen(game));
        }

        if(currentMission == 1&&buttons.size()>2){
            buttons.get(2).setClickable(false);
        }else if(buttons.size()>2){
            buttons.get(2).setClickable(true);
        }
        if((currentMission==data.numOfUnlockedMissions||currentMission == Missions.NUMBER_OF_MISSIONS)&&buttons.size()>1){
            buttons.get(1).setClickable(false);
        }else if(buttons.size()>1){
            buttons.get(1).setClickable(true);
        }

        if(buttons.size()>1&&buttons.get(1).checkButtonHovered(game)&&MouseManager.mousePressed){
            if(currentMission<Missions.NUMBER_OF_MISSIONS&&currentMission<data.numOfUnlockedMissions){
                buttons = new ArrayList<>();
                moveToNextMission = 1;
                moving = true;
                PlayMusic.playSound(game,Assets.buttonClick);
            }
        }
        if(buttons.size()>2&&buttons.get(2).checkButtonHovered(game)&&MouseManager.mousePressed){
            if(currentMission>1){
                buttons = new ArrayList<>();
                moveToNextMission = -1;
                moving = true;
                PlayMusic.playSound(game,Assets.buttonClick);
            }
        }
        if(buttons.size()>3 && MouseManager.mousePressed && buttons.get(3).checkButtonHovered(game)){
            PlayMusic.playSound(game,Assets.buttonClick);
            State.setState(new State_Mission(game,data,currentMission));
        }
        if(moving){
            MouseManager.mousePressed = false;
            KeyboardManager.keyPressed = false;
            if(moveToNextMission == -1) {
                if((currentWire-1+adjust)%3!=0) {
                    if (wireDir[currentWire-1] == 1) {
                        if(amtMoved < wireLengths[currentWire-1]){
                            screenOffsetX+=35;
                            amtMoved+=35;
                        }else{
                            amtMoved = 0;
                            currentWire--;
                            adjust = 1;
                        }
                    } else if (wireDir[currentWire-1] == 2) {
                        if(amtMoved < -1*wireHeights[currentWire-1]){
                            screenOffsetY-=35;
                            amtMoved+=35;
                        }else{
                            amtMoved = 0;
                            currentWire--;
                            adjust = 1;
                        }
                    } else if (wireDir[currentWire-1] == 3) {
                        if(amtMoved < -1* wireLengths[currentWire-1]){
                            screenOffsetX-=35;
                            amtMoved+=35;
                        }else{
                            currentWire--;
                            amtMoved = 0;
                            adjust = 1;
                        }
                    } else {
                        if(amtMoved < wireHeights[currentWire-1]){
                            screenOffsetY+=35;
                            amtMoved+=35;
                        }else{
                            adjust = 1;
                            currentWire--;
                            amtMoved=0;
                        }
                    }
                }else{
                    currentMission--;
                    moving = false;
                    reAddButtons = true;
                    moveToNextMission = 0;
                    adjust = 0;
                }
            }else{
                if((currentWire+1-adjust)%3!=0) {
                    if (wireDir[currentWire] == 1) {
                        if(amtMoved < wireLengths[currentWire]){
                            screenOffsetX-=35;
                            amtMoved+=35;
                        }else{
                            amtMoved = 0;
                            currentWire++;
                            adjust = 1;
                        }
                    } else if (wireDir[currentWire] == 2) {
                        if(amtMoved < -1*wireHeights[currentWire]){
                            screenOffsetY+=35;
                            amtMoved+=35;
                        }else{
                            amtMoved = 0;
                            currentWire++;
                            adjust = 1;
                        }
                    } else if (wireDir[currentWire] == 3) {
                        if(amtMoved < -1* wireLengths[currentWire]){
                            screenOffsetX+=35;
                            amtMoved+=35;
                        }else{
                            currentWire++;
                            amtMoved = 0;
                            adjust = 1;
                        }
                    } else {
                        if(amtMoved < wireHeights[currentWire]){
                            screenOffsetY-=35;
                            amtMoved+=35;
                        }else{
                            adjust = 1;
                            currentWire++;
                            amtMoved=0;
                        }
                    }
                }else{
                    currentMission++;
                    moving = false;
                    reAddButtons = true;
                    moveToNextMission = 0;
                    adjust = 0;
                }
            }
        }
    }

    /**
     * Updated every frame which will update any drawings and check to see
     * what needs to be changed based on the user input.
     * @param g - Graphics2D
     */
    public void render(Graphics g){
        if(!goButtonAdded&&a2>250){
            buttons.add(new Button(game,g,0,null,"START",(int)(w*0.40),(int)(h*0.93),(int)(w*0.2),(int)(h*0.14),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),75,15,false,true));
            goButtonAdded = true;
        }
        if(!buttonsAdded){
            for(int i = 1; i < Missions.NUMBER_OF_MISSIONS+1;i++){
                if(data.missionCompletion[i-1]==-1){
                    missionButtons.add(new Button(game,g,Assets.redExclamationPoint,wiresX[(i-1)*3]-50,wiresY[(i-1)*3]-110,100,220,false));
                }else if(data.missionCompletion[i-1]==0){
                    missionButtons.add(new Button(game,g,Assets.exclamationPoint,wiresX[(i-1)*3]-50,wiresY[(i-1)*3]-110,100,220,false));
                }else if(data.missionCompletion[i-1]==1){
                    missionButtons.add(new Button(game,g,Assets.checkMark,wiresX[(i-1)*3]-80,wiresY[(i-1)*3]-110,100,220,false));
                }else{
                    missionButtons.add(new Button(game,g,Assets.stars.get(0),wiresX[(i-1)*3]-50,wiresY[(i-1)*3]-110,100,220,false));
                }
            }
            buttons.add(new Button(game,g,0,null,"Log Out",(int)(w*0.03),(int)(h*0.11),(int)(w*0.14),(int)(h*0.08),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),40,15,false,true));
            buttons.add(new Button(game,g,0,null,">",(int)(w*0.79),(int)(h*0.70),(int)(w*0.10),(int)(h*0.08),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),180,15,false,true));
            buttons.add(new Button(game,g,0,null,"<",(int)(w*0.11),(int)(h*0.70),(int)(w*0.10),(int)(h*0.08),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),180,15,false,true));
            buttonsAdded = true;
        }
        if(reAddButtons){
            buttons.add(new Button(game,g,0,null,"Log Out",(int)(w*0.03),(int)(h*0.11),(int)(w*0.14),(int)(h*0.08),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),40,15,false,true));
            buttons.add(new Button(game,g,0,null,">",(int)(w*0.79),(int)(h*0.70),(int)(w*0.10),(int)(h*0.08),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),180,15,false,true));
            buttons.add(new Button(game,g,0,null,"<",(int)(w*0.11),(int)(h*0.70),(int)(w*0.10),(int)(h*0.08),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),180,15,false,true));
            goButtonAdded = false;
            reAddButtons = false;
        }
        drawBackground(g);
        if(!moving){
            drawMissionBox(g);
            missionButtons.get(currentMission-1).drawButton(g,game,screenOffsetX,screenOffsetY);
            g.setColor(Colors.ColorAlpha(Colors.lightReds,a1));
            g.setFont(game.font60);
            g.drawString("Please Select a Mission",Display.centerString
                    ("Please Select a Mission",g,game.font60,game),(int)(h*0.12));
            Button.drawButtons(g,game,buttons,0,0);
            Button.drawButtonShine(g,game);
            g.setColor(Colors.ColorAlpha(Colors.lightReds,a1));
            g.setFont(game.font50);
            g.drawString("Mission "+currentMission+": "+Missions.missions[currentMission-1].Type,Display.centerString
                    ("Mission "+currentMission+": "+Missions.missions[currentMission-1].Type,g,game.font50,game),(int)(h*0.33));
            g.drawString(Missions.missions[currentMission-1].toString(),Display.centerString
                    (Missions.missions[currentMission-1].toString(),g,game.font50,game),(int)(h*0.73));
        }else{
            Button.drawButtons(g,game,missionButtons,screenOffsetX,screenOffsetY);
            MouseManager.mousePressed = false;
            KeyboardManager.keyPressed = false;
            a2=0;
        }
    }

    /**
     * the drawBackground method draws the background to the screen. It fades
     * in after a short delay.
     * @param g - Graphics object.
     */
    private void drawBackground(Graphics g){
        g.setColor(Colors.ColorAlpha(Colors.blacks,a1));
        g.fillRect(0,0,w,h);
        if(moving){
            g.drawImage(Assets.burst.get((int)((game.counter%8)*0.625)),(int)(w*0.472),(int)(h*0.455),null);
            g.drawImage(Assets.sparks.get(((game.counter%20)/2)),(int)(w*0.465),(int)(h*0.345),null);
        }
        drawWires(g);
    }

    private void drawMissionBox(Graphics g){
        g.setColor(Colors.ColorAlpha(Colors.blacks,a2));
        g.fillRoundRect((int)(w*0.22),(int)(h*0.2),(int)(w*0.56),h,5,5);
        g.setColor(Colors.ColorAlpha(Colors.whites,a2));
        g.drawRoundRect((int)(w*0.22),(int)(h*0.2),(int)(w*0.56),h,5,5);
    }

    private void makeWires(){
        boolean directionX = true;
        boolean directionY = false;
        int currentX = (int)(w*0.5);
        int currentY = (int)(h*0.5);
        for(int i = 0; i < (Missions.NUMBER_OF_MISSIONS)*3;i++){
            double rand = Math.random();
            int length = (int)((rand*((2*w)/3))+w/2)-((int)((rand*((2*w)/3))+w/2)%35);
             if(!directionY){
                 if(!directionX){
                     wiresX[i] = currentX;
                     wiresY[i] = currentY;
                     wireLengths[i] = -1*length;
                     currentX -= length;
                     wireHeights[i] = -10;
                     wireDir[i] = 3;
                 }else{
                     wiresX[i] = currentX;
                     wiresY[i] = currentY;
                     wireLengths[i] = length;
                     currentX += length;
                     wireHeights[i] = 10;
                     wireDir[i] = 1;
                 }
             }else{
                 if(!directionX){
                     wiresX[i] = currentX;
                     wiresY[i] = currentY;
                     wireHeights[i] = -1*length;
                     currentY -= length;
                     wireLengths[i] = -10;
                     wireDir[i] = 2;
                 }else{
                     wiresX[i] = currentX;
                     wiresY[i] = currentY;
                     wireHeights[i] = length;
                     currentY += length;
                     wireLengths[i] = 10;
                     wireDir[i] = 4;
                 }
             }
             directionX = Math.round(Math.random())==0;
             directionY = !directionY;
        }
    }

    private void drawWires(Graphics g){
        for(int i = 0; i < wiresX.length;i++){
            if((i/3)+1<data.numOfUnlockedMissions){
                g.setColor(wireColorU);
            }else{
                g.setColor(wireColorL);
            }
            g.fillRect(wiresX[i]+screenOffsetX,wiresY[i]+screenOffsetY,wireLengths[i],wireHeights[i]);
        }
    }
}