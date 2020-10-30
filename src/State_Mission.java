import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class State_Mission extends State{

    private final ArrayList<Button> buttons;
    private final ArrayList<Button> arrowButtons;

    private int a;
    private final int a2;
    private final int w;
    private final int h;
    private int equalsX;
    private final int missionNumber;
    private int currentProblem;
    private int answerCorrectness;
    private int misses;
    private int timer;
    private boolean buttonsAdded;
    private boolean arrowButtonsAdded;
    private boolean resetCursor;
    private boolean paused;
    private boolean buttonSwap;
    private boolean value;
    private final boolean timed;
    private final CurrentStudentData data;
    private final InputNum[] inputs;
    private final ArrayList<String[]> results;

    /**
     * Initializes the main game.
     * @param game Main game state
     * @param data - The student's data
     */
    public State_Mission(Game game,CurrentStudentData data,int missionNumber) {
        super(game);

        this.a=0;
        this.a2 = 0;
        this.w = game.width;
        this.h = game.height;
        this.data = data;
        this.resetCursor = true;
        this.missionNumber = missionNumber-1;
        this.arrowButtonsAdded = false;
        this.answerCorrectness = 0;
        this.misses = 0;
        this.timer = 120;
        game.musicVolume = (int)((((double) Objects.requireNonNull(data).volume/100.0)*31)-25);
        PlayMusic.gainControl.setValue((int)((((double)data.volume/100.0)*31)-25)-8);
        buttons = new ArrayList<>();
        arrowButtons = new ArrayList<>();
        results = new ArrayList<>();
        paused = false;
        buttonSwap = false;
        value = false;
        inputs = new InputNum[3];
        this.timed = (missionNumber-1)%3 ==1;
        if(data.missionCompletion[missionNumber-1]==0){
            data.missionCompletion[missionNumber-1]=-1;
        }
        if(Missions.missions[missionNumber-1].operation.equals("+")){
            this.inputs[0] = new InputNum(10000,(int)(w*0.60),(int)(h*0.75));
            equalsX = (int)(w*0.45);
            if(Missions.missions[missionNumber-1].digits == 1){
                if(Missions.missions[missionNumber-1].carry){
                    this.inputs[1] = new InputNum(10000,(int)(w*0.45),(int)(h*0.75));
                    equalsX = (int)(w*0.3);
                }
            }else{
                this.inputs[1] = new InputNum(10000,(int)(w*0.45),(int)(h*0.75));
                equalsX = (int)(w*0.3);
                if(Missions.missions[missionNumber-1].carry){
                    this.inputs[2] = new InputNum(10000,(int)(w*0.30),(int)(h*0.75));
                    equalsX = (int)(w*0.15);
                }
            }
        }else{
            this.inputs[0] = new InputNum(10000,(int)(w*0.60),(int)(h*0.75));
            equalsX = (int)(w*0.45);
            if(Missions.missions[missionNumber-1].digits == 2){
                this.inputs[1] = new InputNum(10000,(int)(w*0.45),(int)(h*0.75));
                equalsX = (int)(w*0.30);
            }
        }
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
        for (Button arrowButton : arrowButtons) {
            if (arrowButton.checkButtonHovered(game)) {
                resetCursor = false;
            }
        }
        if(resetCursor) {
            game.setCursor(true);
        }
        if(paused){
            if(buttons.size()>0&&buttons.get(0).checkButtonHovered(game)&&MouseManager.mousePressed){
                PlayMusic.playSound(game,Assets.buttonClick);
                if(this.currentProblem!=0){
                    data.missionHistory.add(results);
                    Users.saveStudent(data,missionNumber);
                }
                State.setState(new State_MissionSelect(game,data.username));
                Missions.missions[missionNumber].newProblems();
            }else if(buttons.size()>0&&buttons.get(1).checkButtonHovered(game)&&MouseManager.mousePressed){
                PlayMusic.playSound(game,Assets.buttonClick);
                paused = false;
                buttonSwap = true;
                value = false;
            }

        }else{
            if(buttons.size()>0&&buttons.get(0).checkButtonHovered(game)&&MouseManager.mousePressed){
                PlayMusic.playSound(game,Assets.buttonClick);
                paused = true;
                buttonSwap = true;
                value = true;
            }
            if(buttons.size()>1&& MouseManager.mousePressed&&buttons.get(1).checkButtonHovered(game)){
                if(buttons.get(1).label.equals("Check")){
                    PlayMusic.playSound(game,Assets.buttonClick);
                    recordInput();
                    checkAnswer();
                }else{
                    PlayMusic.playSound(game,Assets.buttonClick);
                    if(currentProblem<(Missions.missions[missionNumber].problems.size()-1)) {
                        if(misses>2){
                            Missions.missions[missionNumber].newProblems();
                            data.missionHistory.add(results);
                            Users.saveStudent(data,missionNumber);
                            State.setState(new State_Mission_Summary(game,new State_MissionSelect(game,data.username),results));
                        }
                        for (InputNum input : inputs) {
                            if (input != null) {
                                input.setProblemResult(0);
                            }
                        }
                        currentProblem++;
                        buttons.remove(1);
                        buttons.remove(0);
                        buttonsAdded = false;
                    }else{
                        if (misses==0){
                            data.missionCompletion[missionNumber] = 2;
                        }else if (!(data.missionCompletion[missionNumber] == 2)){
                            data.missionCompletion[missionNumber] = 1;
                        }
                        data.missionHistory.add(results);
                        Missions.missions[missionNumber].newProblems();
                        Users.saveStudent(data,missionNumber);
                        State.setState(new State_Mission_Summary(game,new State_MissionSelect(game,data.username),results));
                    }
                }

            }
            for (int i = 0; i < arrowButtons.size();i++){
                if(arrowButtons.get(i).checkButtonHovered(game)&&MouseManager.mousePressed&&buttons.get(1).label.equals("Check")){
                    boolean b = i%2==0;
                    inputs[(i/2)].incrementValue(b,Missions.missions[missionNumber].operation.equals("-"));
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
        if(!buttonsAdded){
            buttons.add(new Button(game,g,0,null,"Pause",(int)(w*0.80),(int)(h*0.16),(int)(w*0.16),(int)(h*0.12),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),60,15,false,true));
            buttons.add(new Button(game,g,0,null,"Check",(int)(w*0.80),(int)(h*0.76),(int)(w*0.16),(int)(h*0.12),Colors.ColorAlpha(Colors.darkBlues,255),
                    Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                    Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                    Colors.ColorAlpha(Colors.whites,255),60,15,false,true));
            buttonsAdded = true;
        }
        if(buttonSwap){
            if(value){
                buttons.remove(0);
                buttons.remove(0);
                buttons.add(new Button(game,g,0,null,"Exit Level",(int)(w*0.40),(int)(h*0.72),(int)(w*0.2),(int)(h*0.12),Colors.ColorAlpha(Colors.darkBlues,255),
                        Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                        Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                        Colors.ColorAlpha(Colors.whites,255),40,15,false,true));
                buttons.add(new Button(game,g,0,null,"Back to Game",(int)(w*0.40),(int)(h*0.52),(int)(w*0.2),(int)(h*0.12),Colors.ColorAlpha(Colors.darkBlues,255),
                        Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                        Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                        Colors.ColorAlpha(Colors.whites,255),40,15,false,true));
            }else{
                buttons.remove(0);
                buttons.remove(0);
                buttons.add(new Button(game,g,0,null,"Pause",(int)(w*0.80),(int)(h*0.16),(int)(w*0.16),(int)(h*0.12),Colors.ColorAlpha(Colors.darkBlues,255),
                        Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                        Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                        Colors.ColorAlpha(Colors.whites,255),60,15,false,true));
                if(inputs[0].image==Assets.yellowNums.get(inputs[0].value%10)){
                    buttons.add(new Button(game,g,0,null,"Check",(int)(w*0.80),(int)(h*0.76),(int)(w*0.16),(int)(h*0.12),Colors.ColorAlpha(Colors.darkBlues,255),
                            Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                            Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                            Colors.ColorAlpha(Colors.whites,255),60,15,false,true));
                }else{
                    buttons.add(new Button(game,g,0,null,"Okay",(int)(w*0.80),(int)(h*0.56),(int)(w*0.16),(int)(h*0.12),Colors.ColorAlpha(Colors.darkBlues,255),
                            Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                            Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                            Colors.ColorAlpha(Colors.whites,255),60,15,false,true));
                }
            }
            buttonSwap = false;
        }
        drawBackground(g);
        Button.drawButtons(g,game,buttons,0,0);
        if(paused) {
            drawPauseBox(g);
            Button.drawButtonShine(g,game);
        }else {
            if(Missions.missions[missionNumber].operation.equals("+")){
                g.drawImage(Assets.yellowOthers.get(1),(int)(w*0.17),(int)(h*0.06),null);
            }else{
                g.drawImage(Assets.yellowOthers.get(2),(int)(w*0.17),(int)(h*0.06),null);
            }
            if(buttons.get(1).label.equals("Check")){
                g.setFont(game.font40);
                g.setColor(Colors.ColorAlpha(Colors.lightBlues,a));
                g.drawString("Problem # "+(currentProblem+1)+"  |  Score:  "+(currentProblem-misses)+"  /  "+currentProblem,(int)(w*0.01)+10,(int)(h*0.15)+10);
            }else{
                g.setFont(game.font40);
                g.setColor(Colors.ColorAlpha(Colors.lightBlues,a));
                g.drawString("Problem # "+(currentProblem+1)+"  |  Score:  "+(currentProblem-misses+1)+"  /  "+(currentProblem+1),(int)(w*0.01)+10,(int)(h*0.15)+10);
            }
            if(timed){
                if(game.counter%60 == 0){
                    timer--;
                }
                if(timer>40){
                    g.setColor(Colors.ColorAlpha(Colors.yellows,a));
                }else if(timer>10){
                    g.setColor(Colors.ColorAlpha(Colors.greens,a));
                }else if(timer < 1){
                    if(this.currentProblem!=0){
                        data.missionHistory.add(results);
                        Users.saveStudent(data,missionNumber);
                    }
                    State.setState(new State_Mission_Summary(game,new State_MissionSelect(game,data.username),results));
                }else{
                    g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
                }
                g.setFont(game.font60);
                g.drawString("Time :  "+timer,(int)(w*0.01)+10,(int)(h*0.06)+10);
            }
            if(answerCorrectness == 1){
                answerCorrectness = 0;
                for (InputNum input : inputs) {
                    if (input != null) {
                        input.setProblemResult(1);
                    }
                }
                buttons.remove(buttons.size()-1);
                buttons.add(new Button(game,g,0,null,"Okay",(int)(w*0.80),(int)(h*0.56),(int)(w*0.16),(int)(h*0.12),Colors.ColorAlpha(Colors.darkBlues,255),
                        Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                        Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                        Colors.ColorAlpha(Colors.whites,255),60,15,false,true));
            }else if(answerCorrectness == -1){
                answerCorrectness = 0;
                for (InputNum input : inputs) {
                    if (input != null) {
                        input.setProblemResult(-1);
                    }
                }
                buttons.remove(buttons.size()-1);
                buttons.add(new Button(game,g,0,null,"Okay",(int)(w*0.80),(int)(h*0.56),(int)(w*0.16),(int)(h*0.12),Colors.ColorAlpha(Colors.darkBlues,255),
                        Colors.ColorAlpha(Colors.darkBlues,255),Colors.ColorAlpha(Colors.yellows,190),
                        Colors.ColorAlpha(Colors.yellows,255),Colors.ColorAlpha(Colors.whites,190),
                        Colors.ColorAlpha(Colors.whites,255),60,15,false,true));
            }
            for (InputNum input1 : inputs) {
                if (input1 != null) {
                    input1.drawInputNum(g);
                }
            }
            g.setColor(Colors.ColorAlpha(Colors.greens,a));
            g.fillRect((int)(w*0.07),(int)(h*0.50),(int)(w*0.64),(int)(h*0.015));
            if(!arrowButtonsAdded){
                for (InputNum input : inputs) {
                    if (input != null) {
                        arrowButtons.add(new Button(game, g, Assets.upArrow, input.xLoc - 50, input.yLoc - 190, 100, 100, true));
                        arrowButtons.add(new Button(game, g, Assets.downArrow, input.xLoc - 50, input.yLoc + 90, 100, 100, true));
                    }
                }
                arrowButtonsAdded = true;
            }
            Button.drawButtons(g,game,arrowButtons,0,0);
            g.drawImage(Assets.yellowOthers.get(4),equalsX-250,(int)(h*0.75)-250,null);
            for (int j = 0; j < 2;j++){
                for(int i = 0; i<(Missions.missions[missionNumber].digits);i++){
                    if(Missions.missions[missionNumber].digits==1){
                        try{
                            g.drawImage(Missions.missions[missionNumber].problemImages.get(currentProblem).get(j).get(i+1),(int)(w*0.45)+((int)(w*0.15))-250,(int)(h*0.13)+((int)(h*0.22)*j)-250,null);
                        }catch(Exception e){
                            g.drawImage(Assets.blueNums.get(0),(int)(w*0.45)+((int)(w*0.15)*i)-250,(int)(h*0.13)+((int)(h*0.22)*j)-250,null);
                        }
                    }else{
                        try{
                            g.drawImage(Missions.missions[missionNumber].problemImages.get(currentProblem).get(j).get(i),(int)(w*0.45)+((int)(w*0.15)*i)-250,(int)(h*0.13)+((int)(h*0.22)*j)-250,null);
                        }catch(Exception e){
                            g.drawImage(Assets.blueNums.get(0),(int)(w*0.45)+((int)(w*0.15)*i)-250,(int)(h*0.13)+((int)(h*0.22)*j)-250,null);
                        }
                    }
                }
            }
            g.setColor(Colors.ColorAlpha(Colors.lightReds,a));
            g.setFont(game.font60);
            g.drawString("Misses:",(int)(w*0.78),(int)(h*0.25));
            for(int i = 0;i<misses;i++){
                g.drawImage(Assets.redX.get((game.counter%30)),(int)(w*0.84)+(105*i)-100,(int)(h*0.4)-100,null);
            }
            Button.drawButtonShine(g,game);
        }
    }

    /**
     * the drawBackground method draws the background to the screen. It fades
     * in after a short delay.
     * @param g - Graphics object.
     */
    private void drawBackground(Graphics g){
        g.setColor(Colors.ColorAlpha(Colors.blacks,a));
        g.fillRect(0,0,w,h);
    }

    private void drawPauseBox(Graphics g){
        g.setColor(Colors.ColorAlpha(Colors.blacks,a2));
        g.fillRoundRect((int)(w*0.2),(int)(h*0.2),(int)(w*0.6),h,5,5);
        g.setColor(Colors.ColorAlpha(Colors.whites,a2));
        g.drawRoundRect((int)(w*0.2),(int)(h*0.2),(int)(w*0.6),h,5,5);
    }

    private void recordInput(){
        String[] strA = new String[3];
        strA[0] = Missions.missions[missionNumber].problems.get(currentProblem)[0];
        strA[2] = Missions.missions[missionNumber].problems.get(currentProblem)[1];

        StringBuilder answer = new StringBuilder();
        for(int i = this.inputs.length; i>0;i--){
            if(inputs[i-1]!=null){
                answer.append(inputs[i - 1].value % 10);
            }
        }
        if(answer.length()<3){
            answer.insert(0, "" + 0);
        }
        if(answer.length()<3){
            answer.insert(0, "" + 0);
        }
        strA[1] = answer.toString();
        results.add(strA);
    }

    private void checkAnswer(){
        String answer = results.get(results.size()-1)[1];
        if((answer.equals(results.get(results.size()-1)[2]))||(answer.equals(("0"+results.get(results.size()-1)[2])))){
            answerCorrectness = 1;
        }else{
            answerCorrectness = -1;
            misses++;
        }
    }
}
