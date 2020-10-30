import java.util.ArrayList;
public class CurrentStudentData{

    // Fields
    public int volume;
    public final int numOfUnlockedMissions;
    public final String username;
    public final String password;
    public final String realName;
    public final int[] missionCompletion;
    public final ArrayList<Integer> IDS;
    public final ArrayList<ArrayList<String[]>> missionHistory;

    /**
     * Initializes the variables for the student
     * @param username Student's username
     * @param password Student's password
     * @param volume Student's volume settings
     * @param realName Student's actual name
     * @param missionCompletion Student's listing of completed missions
     * @param missionHistory Student's list of results from previous missions
     */
    public CurrentStudentData(String username, String password, int volume, String realName, int[] missionCompletion, ArrayList<ArrayList<String[]>> missionHistory, ArrayList<Integer> IDS){
        this.username = username;
        this.password = password;
        this.volume = volume;
        this.realName = realName;
        this.missionCompletion = missionCompletion;
        this.missionHistory = missionHistory;
        this.IDS = IDS;
        int num = 1;
        for (int aMissionCompletion : missionCompletion) {
            if (aMissionCompletion > 0) {
                num++;
            }
        }
        this.numOfUnlockedMissions = num;
    }
}