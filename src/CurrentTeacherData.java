import java.util.ArrayList;

public class CurrentTeacherData{

    // Fields
    public int volume;
    public final String username;
    public final String password;
    public final String realName;
    public final ArrayList<CurrentStudentData> students;

    /**
     * Initializes the variables for the teacher
     * @param username Teacher's username
     * @param password Teacher's password
     * @param realName Teacher's actual name
     * @param volume Teacher's volume settings
     * @param students Teacher's list of students
     */
    public CurrentTeacherData(String username, String password, String realName, int volume, ArrayList<CurrentStudentData> students){
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.volume = volume;
        this.students = students;
    }

}
