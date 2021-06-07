package Utilities;

import Entities.Student;

import java.util.*;
import java.util.stream.Collectors;

public class Statistics {

    /***
     * A method that returns the minimum grade of a list
     * @param students List of students from the subject
     * @return List of students that have the minimum grade
     */
    public static List<Student> getMinimumGrade(List<Student> students){
        Float minGrade = students.stream()
                        .min(Comparator.comparing(Student::getGrade))
                        .orElse(new Student("default", 0f)).getGrade(); //Minimum grade

        return getStudentsWithGrade(students, minGrade);
    }

    /***
     * A method that returns the maximum grade of a list
     * @param students List of students from the subject
     * @return List of students that have the maximum grade
     */
    public static List<Student> getMaximumGrade(List<Student> students){
        Float maxGrade = students.stream()
                .max(Comparator.comparing(Student::getGrade))
                .orElse(new Student("default", 0f)).getGrade(); //Minimum grade

        return getStudentsWithGrade(students, maxGrade);
    }


    /***
     * A method that returns the average grade of a list
     * @param students List of students from the subject
     * @return Float representing the average grade of the subject
     */
    public static float getAverage(List<Student> students){
        return (float) students.stream()
                .mapToDouble(Student::getGrade)
                .summaryStatistics()
                .getAverage();
    }


    /***
     * A method that returns a list of grades with the most frequency
     * @param students List of students from the subject
     * @return List of students with the most repeated grade
     */
    public static List<Student> getMostRepeated(List<Student> students){
        Map<Float, Long> histogram = getHistogram(students);
        Long maxFrequency = getMostRepeatedFreq(students);
        List<Float> subList = getGrades(histogram, maxFrequency);

        // Return list of students that have most repeated grade
        return students.stream()
                .filter(student -> subList.contains(student.getGrade()))
                .collect(Collectors.toList());
    }

    /***
     * A method that returns a list of grades with the least frequency
     * @param students List of students from the subject
     * @return List of students with the least repeated grade
     */
    public static List<Student> getLeastRepeated(List<Student> students){
        Map<Float, Long> histogram = getHistogram(students);
        Long maxFrequency = getLeastRepeatedFreq(students);
        List<Float> subList = getGrades(histogram, maxFrequency);

        // Return list of students that have most repeated grade
        return students.stream()
                .filter(student -> subList.contains(student.getGrade()))
                .collect(Collectors.toList());
    }

    /***
     * A method that returns the max frequency
     * @param students List of students from the subject
     * @return Long representing the maximum frequency
     */
    public static Long getMostRepeatedFreq(List<Student> students){
        return students.stream()
                // Get frequency of grades
                .collect(Collectors.groupingBy(Student::getGrade, Collectors.counting())).entrySet()
                .stream()
                // Get maximum frequency
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getValue();
    }

    /***
     * A method that returns the min frequency
     * @param students List of students from the subject
     * @return Long representing the minimum frequency
     */
    public static Long getLeastRepeatedFreq(List<Student> students){
        return students.stream()
                // Get frequency of grades
                .collect(Collectors.groupingBy(Student::getGrade, Collectors.counting())).entrySet()
                .stream()
                // Get maximum frequency
                .min(Map.Entry.comparingByValue())
                .orElseThrow()
                .getValue();
    }

    /* Helper methods */

    private static Map<Float, Long> getHistogram(List<Student> students){
        return students.stream()
                // Get frequency of grades
                .collect(Collectors.groupingBy(Student::getGrade, Collectors.counting()));
    }

    private static List<Float> getGrades(Map<Float, Long> histogram, Long frequency){
        List<Float> subList = new ArrayList<>();
        histogram.forEach((aFloat, aLong) -> {
            if(aLong.equals(frequency)){
                subList.add(aFloat);
            }
        });
        return subList;
    }

    private static List<Student> getStudentsWithGrade(List<Student> students, Float grade){
        return students.stream().filter(student -> student.getGrade().equals(grade)).collect(Collectors.toList());
    }
}
