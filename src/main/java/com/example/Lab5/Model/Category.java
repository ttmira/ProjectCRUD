package com.example.Lab5.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categorys")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryid;
    @Column(nullable = false)
    private String categoryname;
    @Column(nullable = false)
    private String descreption;
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Task>tasks;

    public Category() {
    }
    public Category(Long categoryid,String categoryname,String descreption,List<Task>tasks){
        this.categoryid=categoryid;
        this.categoryname=categoryname;
        this.descreption=descreption;
        this.tasks=tasks;
    }
    public Long getCategoryid(){
        return categoryid;
    }
    public void setCategoryid(Long categoryid){
        this.categoryid=categoryid;
    }
    public String getCategoryname(){
        return categoryname;
    }
    public void setCategoryname(String categoryname){
        this.categoryname=categoryname;
    }
    public String getDescreption(){
        return descreption;
    }
    public void setDescreption(String descreption){
        this.descreption=descreption;
    }
    public List<Task> getTasks() {return tasks;}
    public void setTask(List<Task> tasks) {this.tasks = tasks;
    }
}
