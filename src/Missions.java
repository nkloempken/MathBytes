import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Missions {

    public static final int NUMBER_OF_MISSIONS = 25;

    public static final Missions[] missions;

    static{
        missions = new Missions[NUMBER_OF_MISSIONS];
        String operation;
        int digits;
        int index = 0;
        for(int i = 0; i < 2;i++){
            if(i==0){
                operation = "+";
                for(int j = 0; j<2;j++){
                    if(j==0){
                        digits = 1;
                        for(int z = 0; z<2;z++){
                            if(z==0){
                                missions[index] = new Missions(index+1,15,digits,operation,false);
                                missions[index+1] = new Missions(index+2,10,digits,operation,false);
                                missions[index+2] = new Missions(index+3,20,digits,operation,false);
                                index+=3;
                            }else{
                                missions[index] = new Missions(index+1,15,digits,operation,true);
                                missions[index+1] = new Missions(index+2,10,digits,operation,true);
                                missions[index+2] = new Missions(index+3,20,digits,operation,true);
                                index+=3;
                            }
                        }
                    }else{
                        digits = 2;
                        for(int z = 0; z<2;z++){
                            if(z==0){
                                missions[index] = new Missions(index+1,15,digits,operation,false);
                                missions[index+1] = new Missions(index+2,10,digits,operation,false);
                                missions[index+2] = new Missions(index+3,20,digits,operation,false);
                                index+=3;
                            }else{
                                missions[index] = new Missions(index+1,15,digits,operation,true);
                                missions[index+1] = new Missions(index+2,10,digits,operation,true);
                                missions[index+2] = new Missions(index+3,20,digits,operation,true);
                                index+=3;
                            }
                        }
                    }
                }
            }else{
                operation = "-";
                for(int j = 0; j<2;j++){
                    if(j==0){
                        digits = 1;
                        for(int z = 0; z<2;z++){
                            if(z==0){
                                missions[index] = new Missions(index+1,15,digits,operation,false);
                                missions[index+1] = new Missions(index+2,10,digits,operation,false);
                                missions[index+2] = new Missions(index+3,20,digits,operation,false);
                                index+=3;
                            }else{
                                missions[index] = new Missions(index+1,15,digits,operation,true);
                                missions[index+1] = new Missions(index+2,10,digits,operation,true);
                                missions[index+2] = new Missions(index+3,20,digits,operation,true);
                                index+=3;
                            }
                        }
                    }else{
                        digits = 2;
                        for(int z = 0; z<2;z++){
                            if(z==0){
                                missions[index] = new Missions(index+1,15,digits,operation,false);
                                missions[index+1] = new Missions(index+2,10,digits,operation,false);
                                missions[index+2] = new Missions(index+3,20,digits,operation,false);
                                index+=3;
                            }else{
                                missions[index] = new Missions(index+1,15,digits,operation,true);
                                missions[index+1] = new Missions(index+2,10,digits,operation,true);
                                missions[index+2] = new Missions(index+3,20,digits,operation,true);
                                index+=3;
                            }
                        }
                    }
                }
            }
        }
        while (index<NUMBER_OF_MISSIONS){
            missions[index] = new Missions(index,20,2,"-",true);
            index++;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<String[]> problems;
    public ArrayList<ArrayList<ArrayList<BufferedImage>>> problemImages;
    private int numProblems;
    public int digits;
    public String operation;
    public String Type;
    public boolean carry;
    private int missionNumber;


    private Missions(int missionNumber, int numProblems, int digits, String operation, boolean carry){
        this.missionNumber = missionNumber;
        this.digits = digits;
        this.operation = operation;
        this.carry = carry;
        this.numProblems = numProblems;
        this.problems = new ArrayList<>();
        this.problemImages = new ArrayList<>();
        if(missionNumber%3 == 0){
            this.Type = "Practice";
        }else if(missionNumber%3 == 1){
            this.Type = "Learn";
        }else if(missionNumber%3 == 2){
            this.Type = "Speed";
        }
        newProblems();
        for(int j = 0; j <this.numProblems;j++){
            ArrayList<ArrayList<BufferedImage>> a = new ArrayList<>();
            for (int z=1; z < 3;z++){
                ArrayList<BufferedImage> b = new ArrayList<>();
                for(int i = 0; i < 2;i++){
                    if(z==1){
                        int index = Integer.parseInt(problems.get(j)[0].substring(i,i+1));
                        b.add(Assets.blueNums.get(index));
                    }else{
                        int index = Integer.parseInt(problems.get(j)[0].substring(i+3,i+4));
                        b.add(Assets.blueNums.get(index));
                    }
                }
                a.add(b);
            }
            problemImages.add(a);
        }
    }

    /**
     * Generates a problem using random numbers with a range specified by the inputs, then returns the problem.
     * @param digits How many digits the numbers in the problem can be.
     * @param operation Addition or subtraction.
     * @param carry Check for whether numbers that would force a carry into the hundreds place are allowed.
     * @return The problem that has been generated.
     */
    private static String[] generateProblem(int digits, String operation, boolean carry){
        int firstNumOnes = 0;
        int firstNumTens = 0;
        int secondNumOnes = 0;
        int secondNumTens = 0;
        int answerNum = 0;
        int answerNumOnes;
        int answerNumTens;
        int answerNumHundreds;

        if(operation.equals("+")){
            if(digits == 1){
                if(!carry){
                    answerNum = (int) (Math.random() * 10);
                    firstNumOnes = (int) (Math.random() * answerNum);
                    secondNumOnes = answerNum-firstNumOnes;
                }else{
                    answerNum = (int) (Math.random() * 19);
                    if (answerNum < 10){
                        firstNumOnes = (int) (Math.random() * answerNum);
                        secondNumOnes = answerNum-firstNumOnes;
                    }
                    else {
                        firstNumOnes = (answerNum - 9) + (int) (Math.random() * (19-answerNum));
                        secondNumOnes = answerNum-firstNumOnes;
                    }
                }
            }else if(digits == 2){
                if(!carry){
                    answerNum = (int) (Math.random() * 100);
                    answerNumTens = answerNum/10;
                    answerNumOnes = answerNum%10;
                    firstNumOnes = (int) (Math.random() * answerNumOnes);
                    firstNumTens = (int) (Math.random() * answerNumTens);
                    secondNumOnes = answerNumOnes - firstNumOnes;
                    secondNumTens = answerNumTens - firstNumTens;
                }else{
                    firstNumOnes = (int) (Math.random() * 10);
                    firstNumTens = (int) (Math.random() * 10);
                    secondNumOnes = (int) (Math.random() * 10);
                    secondNumTens = (int) (Math.random() * 10);
                    answerNum = (10*(firstNumTens+secondNumTens))+firstNumOnes+secondNumOnes;
                }
            }
        }else if(operation.equals("-")){
            if(digits == 1){
                answerNum = (int) (Math.random() * 10);
                secondNumOnes = (int) (Math.random() * (10 - answerNum));
                firstNumOnes = answerNum + secondNumOnes;
            }else if(digits == 2){
                if(!carry){
                    answerNum = (int)(Math.random() * 100);
                    answerNumTens = answerNum/10;
                    answerNumOnes = answerNum%10;
                    secondNumOnes = (int) (Math.random() * (10 - answerNumOnes));
                    secondNumTens = (int) (Math.random() * (10 - answerNumTens));
                    firstNumOnes = answerNumOnes + secondNumOnes;
                    firstNumTens = answerNumTens + secondNumTens;
                }else{
                    firstNumOnes = (int)(Math.random() * 10);
                    firstNumTens = (int)(Math.random() * 10);
                    int firstNum = (10*firstNumTens)+firstNumOnes;
                    int secondNum = (int)(Math.random() * firstNum);
                    secondNumOnes = secondNum%10;
                    secondNumTens = secondNum/10;
                    answerNum = firstNum - secondNum;
                }
            }
        }

        // Puts the problem and answer into strings, then puts those strings into the array for return
        String problem = ""+firstNumTens;
        problem += firstNumOnes;
        problem += operation;
        problem += secondNumTens;
        problem += secondNumOnes;

        answerNumHundreds = answerNum/100;
        if(answerNumHundreds!=0){
            answerNumTens = (answerNum/10)%10;
        }else{
            answerNumTens = answerNum/10;
        }
        answerNumOnes = answerNum%10;
        String answer = ""+answerNumHundreds+answerNumTens+answerNumOnes;
        return new String[]{problem,answer};
    }

    public void newProblems(){
        problems = new ArrayList<>();
        for(int i = 0; i < numProblems;i++){
            problems.add(Missions.generateProblem(digits,operation,carry));
        }

        problemImages = new ArrayList<>();
        for(int j = 0; j <this.numProblems;j++){
            ArrayList<ArrayList<BufferedImage>> a = new ArrayList<>();
            for (int z=1; z < 3;z++){
                ArrayList<BufferedImage> b = new ArrayList<>();
                for(int i = 0; i < 2;i++){
                    if(z==1){
                        int index = Integer.parseInt(problems.get(j)[0].substring(i,i+1));
                        b.add(Assets.blueNums.get(index));
                    }else{
                        int index = Integer.parseInt(problems.get(j)[0].substring(i+3,i+4));
                        b.add(Assets.blueNums.get(index));
                    }
                }
                a.add(b);
            }
            problemImages.add(a);
        }
    }

    @Override
    public String toString(){
        String str = "";
        str+=digits+" digit";
        if(operation.equals("+")){
            str+= " addition ";
        }else{
            str+= " subtraction ";
        }
        if(carry&&operation.equals("-")){
            if(missionNumber==16||missionNumber==17||missionNumber==18){
                str+=" Again :)";
            }else{
                str+= "with borrowing";
            }
        }else if(carry){
            str+= "with carrying";
        }else if(operation.equals("-")){
            str+= "without borrowing";
        }else{
            str+= "without carrying";
        }
        return str;
    }
}
