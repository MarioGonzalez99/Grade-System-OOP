package Utilities;

import Entities.Student;
import Entities.Subject;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

public class Report {
    /*
     * A method to generate the report and send it to the email address specified in the first line of the file
     */
    public static void generateReport(Subject subject){
        // Create the strings used for the report
        List<Student> students = subject.getStudents();
        String listOfStudents = getListOfStudents(students);
        String min = getMinimumGradeString(students);
        String max = getMaximumGradeString(students);
        String mostRepeated = getMostRepeatedString(students);
        String leastRepeated = getLeastRepeatedString(students);
        String average = getAverageString(students);

        System.out.print(listOfStudents);
        System.out.print(min);
        System.out.print(max);
        System.out.print(mostRepeated);
        System.out.print(leastRepeated);
        System.out.print(average);

        // Creation of the TXT
        generateTextFile(subject.toString(), listOfStudents, min, max, mostRepeated, leastRepeated, average);
        System.out.print("The txt file was created successfully\n");

        // Creation of the PDF
        generatePDF(subject.toString(), listOfStudents, min, max, mostRepeated, leastRepeated, average);
        System.out.print("The pdf file was created successfully\n");

        // Send email to address specified in the first line of the file
        sendEmail(subject.getEmail(), subject.toString());
        System.out.print("The email was sent to "+subject.getEmail()+" successfully\n\n");
    }

    /*
     * A method to generate the string that displays the list of students of the subject
     */
    private static String getListOfStudents(List<Student> students){
        StringBuilder table = new StringBuilder();
        students.sort(Comparator.comparing(Student::getGrade).reversed());
        table.append("Student, Grade\n");
        for (Student student: students) {
            table.append(student.getName()).append(", ").append(student.getGrade()).append("\n");
        }

        return table.append("\n").toString();
    }

    /*
     * A method to generate the string that displays the minimum grade of the subject
     */
    private static String getMinimumGradeString(List<Student> students){
        List<Student> studentsWithMinimumGrade = Statistics.getMinimumGrade(students);
        StringBuilder sb = new StringBuilder();
        sb.append("The minimum grade is: ").append(studentsWithMinimumGrade.get(0).getGrade()).append("\n")
        .append("Student(s) with minimum grade: ");
        for(Student student : studentsWithMinimumGrade){
            sb.append(student.getName()).append(", ");
        }
        sb.replace(sb.length()-2, sb.length()-1, "\n");
        return sb.append("\n").toString();
    }

    /*
     * A method to generate the string that displays the maximum grade of the subject
     */
    private static String getMaximumGradeString(List<Student> students){
        List<Student> studentsWithMaximumGrade = Statistics.getMaximumGrade(students);
        StringBuilder sb = new StringBuilder();
        sb.append("The maximum grade is: ").append(studentsWithMaximumGrade.get(0).getGrade()).append("\n")
                .append("Student(s) with maximum grade: ");
        for(Student student : studentsWithMaximumGrade){
            sb.append(student.getName()).append(", ");
        }
        sb.replace(sb.length()-2, sb.length()-1, "\n");
        return sb.append("\n").toString();
    }

    /*
     * A method to generate the string that displays the most repeated grade of the subject
     */
    private static String getMostRepeatedString(List<Student> students){
        List<Student> subList = Statistics.getMostRepeated(students);
        subList.sort(Comparator.comparing(Student::getGrade).reversed());
        StringBuilder sb = new StringBuilder();
        sb.append("The most repeated grade(s) with a frequency of ").append(Statistics.getMostRepeatedFreq(students)).append(": \n");
        for(Student student: subList){
            sb.append(" Student: ").append(student.getName()).append(", Grade: ").append(student.getGrade()).append("\n");
        }
        return sb.append("\n").toString();
    }

    /*
     * A method to generate the string that displays the least repeated grade of the subject
     */
    private static String getLeastRepeatedString(List<Student> students){
        List<Student> subList = Statistics.getLeastRepeated(students);
        subList.sort(Comparator.comparing(Student::getGrade).reversed());
        StringBuilder sb = new StringBuilder();
        sb.append("The least repeated grade(s) with a frequency of ").append(Statistics.getLeastRepeatedFreq(students)).append(": \n");
        for(Student student: subList){
            sb.append(" Student: ").append(student.getName()).append(", Grade: ").append(student.getGrade()).append("\n");
        }
        return sb.append("\n").toString();
    }

    /*
     * A method to generate the string that displays the average grade of the subject
     */
    private static String getAverageString(List<Student> students){
        float average = Statistics.getAverage(students);
        return String.format("The average is: %.2f\n",average);
    }

    /*
     * A method to generate a pdf report using the strings about the statistic modules of the subject
     */
    private static void generatePDF(String subject, String listOfStudents, String min, String max,
                                    String mostRepeated, String leastRepeated, String average ){
        // Step 1: creation of a document-object
        Document document = new Document();
        try {
            /* Step 2:
             * we create a writer that listens to the document
             * and directs a PDF-stream to a file
             */
            PdfWriter.getInstance(document,
                    new FileOutputStream(subject+".pdf"));

            // Step 3: we open the document
            document.open();
            // Step 4: we add a paragraph to the document
            document.add(new Paragraph(listOfStudents));
            document.add(new Paragraph(min));
            document.add(new Paragraph(max));
            document.add(new Paragraph(mostRepeated));
            document.add(new Paragraph(leastRepeated));
            document.add(new Paragraph(average));
        } catch (DocumentException | IOException de) {
            System.err.println(de.getMessage());
        }

        // Step 5: we close the document
        document.close();
    }

    /*
     * A method to generate a txt report using the strings about the statistic modules of the subject
     */
    private static void generateTextFile(String subject, String listOfStudents, String min, String max,
                                         String mostRepeated, String leastRepeated, String average){
        File file = new File(subject+".txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(listOfStudents);
            fr.write(min);
            fr.write(max);
            fr.write(mostRepeated);
            fr.write(leastRepeated);
            fr.write(average);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * A method to send the report towards an specified address
    */
    private static void sendEmail(String email, String subject) {
        try(InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject + " Grades Report");


            // Text content of the email
            String msg = "The report for the subject "+subject+" is attached to this email in pdf format";
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            // Attach the file
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(new File(subject+".pdf"));
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}
