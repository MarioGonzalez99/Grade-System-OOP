package Entities;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Science implements Subject{

    private List<Student> students;
    private String email;

    @Override
    public String toString(){
        return "Science";
    }
}