import java.util.ArrayList;
public class CurrentAdminData{

    // Fields
    public int volume;
    public final String username;
    public final String password;
    public final ArrayList<CurrentTeacherData> teachers;

    /**
     * This class initializes the variables for the admin
     * @param username Username field
     * @param password Password field
     * @param volume Volume settings field
     * @param teachers Field listing the admin's teachers
     */
    public CurrentAdminData(String username, String password, int volume, ArrayList<CurrentTeacherData> teachers){
        this.username = username;
        this.password = password;
        this.volume = volume;
        this.teachers = teachers;
    }

    @Override
    public String toString(){
        return ("A%U%"+username+"%U%%P%"+password+"%P%%V%"+volume+"%V%");
    }

}
