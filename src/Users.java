import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * @author - Nate Kloempken
 *
 * - Version 1
 *
 * NOTE:  WE PLAN TO NOT LET USERS USE ANY SPECIAL CHARACTERS IN USER NAMES OR PASSWORDS SO WE CAN USE THEM FOR FILE WRITING.
 *
 * NOTE:  CHECKING LENGTH OF USERNAME AND PASSWORD MUST IS ***NOT*** DONE HERE.  MUST BE DONE IN GAME!
 *
 * NOTE:  THIS WILL LIKELY BE THE CLASS THAT READS AND WRITES DATA TO STORE OTHER USER STATS LIKE LEVELS COMPLETED.
 * THIS FUNCTIONALITY WILL HAVE TO BE IMPLEMENTED LATER.
 *
 * The Users Class will be used both to add or remove users, and also to check when a user tries to sign in.
 * @noinspection ResultOfMethodCallIgnored
 */
abstract class Users {

    /**
     * This function makes a new user given a username and password input from the user.
     * @param username - entered by user
     * @param password- entered by user
     * @return - false if username exists(or if could not find file) and true if successfully made a new User.
     */
    public static boolean makeNewStudent(String username, String password,String fullName,Game game, String teacherUsername){

        if(Users.signIn(username, password, "S")> -1){ // This uses the other method to check if user exists already
            return false;
        }
        if(Users.signIn(username, password, "T")> -1){ // This uses the other method to check if user exists already
            return false;
        }
        if(Users.signIn(username, password, "A")> -1){ // This uses the other method to check if user exists already
            return false;
        }

        try {
            File tempFile = new File("tempFile.txt");
            File userDataFile = new File("masterFile.txt");         // The process copies all other users into
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile)); // a new file and then renames it.
            Scanner sc = new Scanner(userDataFile); // This will read the file.

            while (sc.hasNext()) {
                String userData = sc.nextLine(); // Raw data to be copied back in if necessary.
                String userName = userData.substring(userData.indexOf("%U%")+3,userData.lastIndexOf("%U%"));

                if (!(teacherUsername.equals(userName))) { // Fires if not found on file
                    writer.write(userData); // Copies un-deleted data over.  Skips if is the username to delete.
                    writer.newLine();
                }else{
                    writer.write(userData);
                    writer.newLine();
                    StringBuilder missionCompletion = new StringBuilder("0");
                    for(int i = 1; i < Missions.NUMBER_OF_MISSIONS;i++){
                        missionCompletion.append(",0");
                    }
                    writer.write("S%U%"+username+"%U%%P%"+Users.hashPassword(password)+"%P%%RN%"+fullName+"%RN%%V%50%V%%MC%"+missionCompletion+"%MC%%HD%%HD%%ID%0%ID%");
                    writer.newLine();
                }
            }
            sc.close();
            writer.close();
            userDataFile.delete(); // Deletes the old file
            return tempFile.renameTo(userDataFile); // Renames the new file to take its place.

        } catch (IOException e) {
            return false;
        }
    }

    /**
     * This function is used to make a new teacher.
     * @param username Stores the username
     * @param password Stores the password
     * @param fullName Stores the teacher's actual name
     * @param game Game object
     * @return Checks if the new teacher wrote into the file successfully. False if the user exists or the file was not found, true if successful.
     */
    public static boolean makeNewTeacher(String username, String password,String fullName,Game game,String adminUsername){
        if(Users.signIn(username, password, "S")> -1){ // This uses the other method to check if user exists already
            return false;
        }
        if(Users.signIn(username, password, "T")> -1){ // This uses the other method to check if user exists already
            return false;
        }
        if(Users.signIn(username, password, "A")> -1){ // This uses the other method to check if user exists already
            return false;
        }

        try {
            File tempFile = new File("tempFile.txt");
            File userDataFile = new File("masterFile.txt");         // The process copies all other users into
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile)); // a new file and then renames it.
            Scanner sc = new Scanner(userDataFile); // This will read the file.

            while (sc.hasNext()) {
                String userData = sc.nextLine(); // Raw data to be copied back in if necessary.
                String userName = userData.substring(userData.indexOf("%U%")+3,userData.lastIndexOf("%U%"));

                if (!(adminUsername.equals(userName))) { // Fires if not found on file
                    writer.write(userData); // Copies un-deleted data over.  Skips if is the username to delete.
                    writer.newLine();
                }else{
                    writer.write(userData);
                    writer.newLine();
                    writer.write("T%U%"+username+"%U%%P%"+Users.hashPassword(password)+"%P%%RN%"+fullName+"%RN%%V%50%V%");
                    writer.newLine();
                }
            }
            sc.close();
            writer.close();
            userDataFile.delete(); // Deletes the old file
            return tempFile.renameTo(userDataFile); // Renames the new file to take its place.

        } catch (IOException e) {
            return false;
        }
    }

    /**
     * The signIn function checks to make sure that 1: the input username exists on record, and 2: that the password
     * input matches that of given username.
     * @param username - Attempted username entered by user.
     * @param password - Attempted password entered by user.
     * @return - int : -2 for file not found, -1 for user does not exist, 0 for incorrect password, 1 for successful sign in.
     */
    public static int signIn(String username, String password, String userType){

        try{
            Scanner sc = new Scanner(new File("masterFile.txt")); // This will read the file.
            while(sc.hasNext()){
                String userData = sc.nextLine();
                if(userData.substring(0,1).equals(userType)){
                    String userName = userData.substring(userData.indexOf("%U%")+3,userData.lastIndexOf("%U%"));
                    String userPassword = userData.substring(userData.indexOf("%P%")+3,userData.lastIndexOf("%P%"));

                    if(username.equals(userName)){ // True if username is found on file
                        if(Users.hashPassword(password).equals(userPassword)){ // True if password matches.
                            sc.close();
                            return 1;
                        }
                        sc.close(); // Happens if the username matches but the password does not
                        return 0;
                    }
                }

            }
            sc.close(); // Happens if the user does not exist
            return -1;

        }catch(FileNotFoundException e){
            return -2;
        }

    }

    /**
     * This function is used to remove a user from the file.  AUTHORIZATION MUST BE CHECKED BY THE GAME.  IT IS NOT DONE HERE.
     * @param username - Username of user to be removed
     * @noinspection StatementWithEmptyBody
     */
    public static void removeUser(String username, Game game, String userType) {
        if (Users.signIn(username, ":", userType) !=0) { // This uses the other method to check if user exists already
            return;
        }

        try {

            File tempFile = new File("tempFile.txt");
            File userDataFile = new File("masterFile.txt");         // The process copies all other users into
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile)); // a new file and then renames it.
            Scanner sc = new Scanner(userDataFile); // This will read the file.

            boolean delete = false;
            boolean teacherAcc = userType.equals("T");

            while (sc.hasNext()) {
                String userData = sc.nextLine(); // Raw data to be copied back in if necessary.
                String userName = userData.substring(userData.indexOf("%U%")+3,userData.lastIndexOf("%U%"));

                if(!(userData.substring(0,1).equals("S"))){
                    delete = false;
                }

                if ((username.equals(userName))) { // Fires if not found on file
                    if(teacherAcc){
                        delete = true;
                    }
                }else //noinspection StatementWithEmptyBody,StatementWithEmptyBody,StatementWithEmptyBody
                    if(delete) {

                }else {
                    writer.write(userData); // Copies un-deleted data over.  Skips if is the username to delete.
                    writer.newLine();
                }
            }

            sc.close();
            writer.close();
            userDataFile.delete(); // Deletes the old file
            tempFile.renameTo(userDataFile);

        } catch (IOException ignored) {
        }
    }

    /**
     * Loads admin data from the user file.
     * @param username The username entered by the admin
     * @return Returns admin data if the file is successfully read and matches the username, null if not.
     */
    public static CurrentAdminData loadAdminData(String username){
        try{
            Scanner sc = new Scanner(new File("masterFile.txt")); // This will read the file.
            while(sc.hasNext()){
                String data = sc.nextLine();
                if(data.substring(0,1).equals("A")){
                    String userName = data.substring(data.indexOf("%U%")+3,data.lastIndexOf("%U%"));
                    if(userName.equals(username)){
                        String password = data.substring(data.indexOf("%P%")+3,data.lastIndexOf("%P%"));
                        int volume = Integer.parseInt(data.substring(data.indexOf("%V%")+3,data.lastIndexOf("%V%")));
                        String teacherData = "X";
                        ArrayList<CurrentTeacherData> teachers = new ArrayList<>();
                        while(sc.hasNext()&&!teacherData.substring(0,1).equals("A")){
                            teacherData = sc.nextLine();
                            if(teacherData.substring(0,1).equals("T")){
                                teachers.add(loadTeacherData(teacherData.substring(teacherData.indexOf("%U%")+3,teacherData.lastIndexOf("%U%"))));
                            }
                        }
                        sc.close();
                        return new CurrentAdminData(username,password,volume,teachers);
                    }
                }
            }
            sc.close();
            return null;

        }catch(Exception e){
            return null;
        }

    }

    /**
     * Loads teacher data from the user file.
     * @param username The username entered by the teacher
     * @return Returns teacher data if the file is successfully read and matches the username, null if not.
     */
    public static CurrentTeacherData loadTeacherData(String username){
        try{
            Scanner sc = new Scanner(new File("masterFile.txt")); // This will read the file.
            while(sc.hasNext()){
                String data = sc.nextLine();
                if(data.substring(0,1).equals("T")){
                    String userName = data.substring(data.indexOf("%U%")+3,data.lastIndexOf("%U%"));
                    if(userName.equals(username)){
                        String password = data.substring(data.indexOf("%P%")+3,data.lastIndexOf("%P%"));
                        int volume = Integer.parseInt(data.substring(data.indexOf("%V%")+3,data.lastIndexOf("%V%")));
                        String realName = data.substring(data.indexOf("%RN%")+4,data.lastIndexOf("%RN%"));
                        String studentData = "X";
                        ArrayList<CurrentStudentData> students = new ArrayList<>();
                        while(sc.hasNext()&&!studentData.substring(0,1).equals("T")){
                            studentData = sc.nextLine();
                            if(studentData.substring(0,1).equals("S")){
                                students.add(loadStudentData(studentData.substring(studentData.indexOf("%U%") + 3, studentData.lastIndexOf("%U%"))));
                            }
                        }
                        sc.close();
                        return new CurrentTeacherData(username,password,realName,volume,students);
                    }
                }
            }
            sc.close();
            return null;

        }catch(Exception e){
            return null;
        }
    }

    /**
     * Loads teacher data from the user file.
     * @param username The username entered by the student
     * @return Returns teacher data if the file is successfully read and matches the username, null if not.
     */
    public static CurrentStudentData loadStudentData(String username){
        try{
            Scanner sc = new Scanner(new File("masterFile.txt")); // This will read the file.
            while(sc.hasNext()){
                String data = sc.nextLine();
                if(data.substring(0,1).equals("S")){
                    String userName = data.substring(data.indexOf("%U%")+3,data.lastIndexOf("%U%"));
                    if(userName.equals(username)){
                        String password = data.substring(data.indexOf("%P%")+3,data.lastIndexOf("%P%"));
                        int volume = Integer.parseInt(data.substring(data.indexOf("%V%")+3,data.lastIndexOf("%V%")));
                        String realName = data.substring(data.indexOf("%RN%")+4,data.lastIndexOf("%RN%"));

                        String[] missionCompletionString = data.substring(data.indexOf("%MC%")+4,data.lastIndexOf("%MC%")).split(",");
                        int[] missionCompletion = new int[missionCompletionString.length];
                        for(int i = 0; i < missionCompletionString.length;i++){
                            missionCompletion[i] = Integer.parseInt(missionCompletionString[i]);
                        }

                        ArrayList<Integer> IDS = new ArrayList<>();
                        String[] IDString = data.substring(data.indexOf("%ID%")+4,data.lastIndexOf("%ID%")).split(",");
                        for (String aIDString : IDString) {
                            IDS.add(Integer.parseInt(aIDString));
                        }

                        ArrayList<ArrayList<String[]>> completeHistory = new ArrayList<>();
                        String[] historyData;
                        try{
                            historyData = data.substring(data.indexOf("%HD%")+4,data.lastIndexOf("%HD%")).split("@");
                        }catch(ArrayIndexOutOfBoundsException e){
                            historyData = new String[0];
                        }
                        for (String aHistoryData : historyData) {
                            ArrayList<String[]> missionData = new ArrayList<>();
                            String[] historyDataLVL2 = aHistoryData.split("#");
                            for (String aHistoryDataLVL2 : historyDataLVL2) {
                                String[] historyDataLVL3 = aHistoryDataLVL2.split(",");
                                missionData.add(historyDataLVL3);
                            }
                            completeHistory.add(missionData);
                        }

                        sc.close();
                        completeHistory.remove(0);
                        return new CurrentStudentData(username,password,volume,realName,missionCompletion,completeHistory,IDS);
                    }
                }
            }
            sc.close();
            return null;

        }catch(Exception e){
            return null;
        }
    }

    public static void saveAdmin(CurrentAdminData data){

        try {
            File tempFile = new File("tempFile.txt");
            File userDataFile = new File("masterFile.txt");         // The process copies all other users into
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile)); // a new file and then renames it.
            Scanner sc = new Scanner(userDataFile); // This will read the file.
            while (sc.hasNext()) {
                String userData = sc.nextLine(); // Raw data to be copied back in if necessary.
                String userName = userData.substring(userData.indexOf("%U%")+3,userData.lastIndexOf("%U%"));

                if (!(data.username.equals(userName))) {
                    writer.write(userData); // Copies un-deleted data over.  Skips if is the username to delete
                    writer.newLine();
                }else{
                    writer.write("A%U%"+data.username+"%U%%P%"+data.password+"%P%%V%"+data.volume+"%V%");
                    writer.newLine();
                }
            }
            sc.close();
            writer.close();
            userDataFile.delete(); // Deletes the old file
            tempFile.renameTo(userDataFile); // Renames the new file to take its place.

        } catch (IOException e) {
            System.exit(1);
        }
    }
    public static void saveTeacher(CurrentTeacherData data){
        try {
            File tempFile = new File("tempFile.txt");
            File userDataFile = new File("masterFile.txt");         // The process copies all other users into
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile)); // a new file and then renames it.
            Scanner sc = new Scanner(userDataFile); // This will read the file.
            while (sc.hasNext()) {
                String userData = sc.nextLine(); // Raw data to be copied back in if necessary.
                String userName = userData.substring(userData.indexOf("%U%")+3,userData.lastIndexOf("%U%"));

                if (!(data.username.equals(userName))) {
                    writer.write(userData); // Copies un-deleted data over.  Skips if is the username to delete
                    writer.newLine();
                }else{
                    writer.write("T%U%"+data.username+"%U%%P%"+data.password+"%P%%RN%"+data.realName+"%RN%%V%"+data.volume+"%V%");
                    writer.newLine();
                }
            }
            sc.close();
            writer.close();
            userDataFile.delete(); // Deletes the old file
            tempFile.renameTo(userDataFile); // Renames the new file to take its place.

        } catch (IOException e) {
            System.exit(1);
        }
    }
    public static void saveStudent(CurrentStudentData data,int missionNumber){
        try {
            File tempFile = new File("tempFile.txt");
            File userDataFile = new File("masterFile.txt");         // The process copies all other users into
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile)); // a new file and then renames it.
            Scanner sc = new Scanner(userDataFile); // This will read the file.
            while (sc.hasNext()) {
                String userData = sc.nextLine(); // Raw data to be copied back in if necessary.
                String userName = userData.substring(userData.indexOf("%U%")+3,userData.lastIndexOf("%U%"));

                if (!(data.username.equals(userName))) {
                    writer.write(userData); // Copies un-deleted data over.  Skips if is the username to delete
                    writer.newLine();
                }else{
                    StringBuilder MC = new StringBuilder();
                    if(data.missionCompletion.length>0){
                        MC.append(data.missionCompletion[0]);
                    }
                    for(int i = 1; i < data.missionCompletion.length;i++){
                        MC.append(",");
                        MC.append(data.missionCompletion[i]);
                    }

                    if(missionNumber > -1){
                        data.IDS.add(missionNumber+1);
                    }
                    StringBuilder IDS = new StringBuilder();
                    if(data.IDS.size()>0){
                        IDS.append(data.IDS.get(0));
                    }
                    for(int i = 1; i < data.IDS.size();i++){
                        IDS.append(",");
                        IDS.append(data.IDS.get(i));
                    }

                    StringBuilder MH = new StringBuilder("@");
                    for(int i = 0;i<data.missionHistory.size();i++){
                        for (int j = 0; j< data.missionHistory.get(i).size();j++){
                            for(int z = 0; z < data.missionHistory.get(i).get(j).length;z++){
                                MH.append(data.missionHistory.get(i).get(j)[z]);
                                if(z != data.missionHistory.get(i).get(j).length-1){
                                    MH.append(",");
                                }
                            }
                            if(j != data.missionHistory.get(i).size()-1){
                                MH.append("#");
                            }
                        }
                        if(i != data.missionHistory.size()-1){
                            MH.append("@");
                        }
                    }
                    writer.write("S%U%"+data.username+"%U%%P%"+data.password+"%P%%RN%"+data.realName+"%RN%%V%"+data.volume+"%V%%MC%"+MC+"%MC%%HD%"+MH+"%HD%%ID%"+IDS+"%ID%");
                    writer.newLine();
                }
            }
            sc.close();
            writer.close();
            userDataFile.delete(); // Deletes the old file
            tempFile.renameTo(userDataFile); // Renames the new file to take its place.

        } catch (IOException e) {
            System.exit(1);
        }
    }

    private static String hashPassword(String passwordEntered){
        byte[] encodedHash;
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            encodedHash = digest.digest(passwordEntered.getBytes(StandardCharsets.UTF_8));
        }catch(Exception e){
            System.out.println("Oh no");
            encodedHash = null;
            System.exit(1);
        }
        return Users.bytesToHex(encodedHash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte aHash : hash) {
            String hex = Integer.toHexString(0xff & aHash);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}