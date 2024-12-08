package com.example.Lab5.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="tasks")

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskid;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private LocalDate duedate;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private String priority;
    @ManyToOne
    @JoinColumn(name = "userid",nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "categoryid",nullable = false)
    private Category category;

    public Task(){}

    public Task(Long taskid, String description, String title, LocalDate duedate, String status, User user, Category category) {
        this.taskid = taskid;
        this.description = description;
        this.title = title;
        this.duedate = duedate;
        this.status = status;
        this.priority=priority;
        this.user = user;
        this.category = category;
    }

    public Long getTaskid() {
        return taskid;
    }
    public void setTaskid(Long taskid) {
        this.taskid = taskid;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public LocalDate getDuedate() {
        return duedate;
    }
    public void setDuedate(LocalDate duedate) {
        this.duedate = duedate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getPriority() {return priority;}
    public void setPriority(String priority) {this.priority = priority;}
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user= user;
    }
    public Category getCategory() {
        return  category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
}
