package model.service;

import model.entities.Department;

import java.util.Arrays;
import java.util.List;

public class DepartmentService {
    public List<Department> findAll(){
        Department books=new Department(1,"Livros");
        Department computers=new Department(2,"Computadores");
        Department electronics=new Department(3,"Eletrônicos");
        return Arrays.asList(books,computers,electronics);
    }
}
