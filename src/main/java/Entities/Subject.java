package Entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Subject {

    private List<Student> students;
    private String email;
    private String name;

    public Subject(String name){
        this.name = name;
    }

    @Override
    public String toString() { return name; }
}
