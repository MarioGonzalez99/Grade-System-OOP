package Utilities;

import Entities.Student;
import Entities.Subject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utilities {

    /*
     * Validates if the user input is a valid number
     */
    public static byte getOptionNumber(BufferedReader reader) {
        byte optionNumber = 0;

        try {
            optionNumber = Byte.parseByte(reader.readLine());
        } catch (NumberFormatException | IOException ex) {
            String errorMsg = "Not a valid number";
            System.out.println(errorMsg);
        }

        return optionNumber;
    }

    /*
     * Loads the initial file according to the subject chosen in the menu
     */
    public static void loadFile(String path, Subject subject) throws IOException {
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        subject.setEmail(reader.readLine()); //First line of the file
        subject.setStudents(getStudentsFromFile(reader));

        System.out.println("Subject loaded successfully");
        reader.close();
    }

    /*
     * Displays a second menu after selecting to introduce a new student
     */
    public static void addStudentMenu(BufferedReader reader, Subject subject) {
        try {
            String menuMsg = """
                    Add New Student:
                    1. Using keyboard
                    2. Through file""";
            System.out.println(menuMsg);
            byte optionNumber = getOptionNumber(reader);
            if (optionNumber == 1) {
                createNewStudent(reader, subject.getStudents());
            } else if (optionNumber == 2) {
                System.out.println("Enter the file path");
                String path = reader.readLine();
                createNewStudent(path, subject.getStudents());
            } else {
                System.out.println("Didn't select a valid option");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Process the initial file of the subject and retrieves the current students in the file
     */
    private static List<Student> getStudentsFromFile(BufferedReader reader) throws IOException {
        List<Student> students = new ArrayList<>();
        String currentStr;
        while((currentStr = reader.readLine())!=null){
            String[] currentStudent = currentStr.split(",");
            String name = currentStudent[0];
            String grade = currentStudent[1];
            Student student = new Student(name, Float.parseFloat(grade));
            students.add(student);
        }
        return students;
    }

    /*
     * Instantiates and add a new student using the data specified in a file
     */
    private static void createNewStudent(String path, List<Student> students) throws IOException {
        File file = new File(path);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("The file couldn't be located, please make sure to have entered a valid path");
            return;
        }

        String currentStr;
        while((currentStr = reader.readLine())!=null){
            String[] currentStudent = currentStr.split(",");
            String name = currentStudent[0];
            Float grade = Float.parseFloat(currentStudent[1]);
            Student student = new Student(name, grade);
            students.add(student);
            System.out.println("Student "+student.getName()+" added successfully");
        }
    }

    /*
     * Instantiates and add a new student using the user input through keyboard
     */
    private static void createNewStudent(BufferedReader reader, List<Student> students){
        // Add name
        String name = null;
        boolean validInput = false;
        do{
            System.out.println("Enter the student name: ");
            try {
                name = reader.readLine().trim();
                if(name.isEmpty()) throw new IOException();
                validInput = true; //Exit loop if valid input
            } catch (IOException e) {
                System.out.println("The name is not a valid, please enter a valid input");
            }
        }while(!validInput);

        // Add grade
        Float grade = null;
        validInput = false;
        do{
            System.out.println("Enter the student grade: ");
            try {
                grade = Float.parseFloat(reader.readLine());
                if(grade <0 || grade > 10) throw new IOException(); // Throw exception if grade is not between a valid range
                validInput = true; //Exit loop if valid input
            } catch (NumberFormatException | IOException e) {
                System.out.println("The grade is not a valid number (0-10 inclusive), please enter a valid number");
            }
        }while(!validInput);
        // Instantiate a new student with the name and grade provided by the user
        Student student = new Student(name, grade);
        students.add(student);
        System.out.println("Student "+student.getName()+" added successfully");
    }
}
