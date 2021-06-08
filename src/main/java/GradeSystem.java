import Entities.Subject;
import Utilities.Utilities;
import Utilities.Report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GradeSystem {
    public final static BufferedReader IN = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        boolean exitProgram = false;
        String welcomeMsg = "Welcome to the grade system";
        System.out.println(welcomeMsg);
        do{
            // Load subject
            Subject subject = getSubject();
            if (subject == null) continue; // If no valid subject is selected ask again

            //Display subject menu and return if the user wants to change subject or exit program
            exitProgram = subjectMenu(subject);

        }while(!exitProgram);

        // Close open streams and exit program
        System.out.println("Closing program");
        try {
            IN.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Loading subject
     */
    private static Subject getSubject() {
        String menuMsg = """
                Enter the number of the subject
                1.Mathematics
                2.Science
                3.English""";
        System.out.println(menuMsg);
        Subject subject;
        byte optionNumber = Utilities.getOptionNumber(IN);
        switch (optionNumber){
            case 1 -> subject = new Subject("Mathematics");
            case 2 -> subject = new Subject("Science");
            case 3 -> subject = new Subject("English");
            default -> {
                return null;
            }
        }
        try {
            Utilities.loadFile("src/main/resources/"+subject,subject);
        } catch (IOException e) {
            System.out.println("Couldn't locate the subject file");
            e.printStackTrace();
        }
        return subject;
    }

    /*
     * Display subject menu
     */
    private static boolean subjectMenu(Subject subject) {
        String menuMsg;
        byte optionNumber;
        // Enter Subject menu
        boolean exitProgram = false;
        boolean changeSubject = false; //Variable used for changing the subject
        do{
            menuMsg = "Subject: "+ subject +"\n" +
                    """
                    1. Add new student
                    2. Generate and send report
                    3. Change subject
                    4. Exit program""";
            System.out.println(menuMsg);
            optionNumber = Utilities.getOptionNumber(IN);
            switch (optionNumber) {
                case 1 -> Utilities.addStudentMenu(IN, subject);
                case 2 -> Report.generateReport(subject);
                case 3 -> {
                    System.out.println("Changing subject");
                    changeSubject = true;
                }
                case 4 -> exitProgram = true;
            }
        }while (!changeSubject && !exitProgram);
        return exitProgram;
    }
}
