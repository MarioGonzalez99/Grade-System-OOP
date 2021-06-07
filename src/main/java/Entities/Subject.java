package Entities;

import java.util.List;

public interface Subject {

    List<Student> getStudents();

    String getEmail();

    void setStudents(List<Student> students);

    void setEmail(String email);

    String toString();

}
