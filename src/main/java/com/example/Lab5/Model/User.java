package com.example.Lab5.Model;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name="users")

public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long userid;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(name="is_admin",nullable = false)
    private boolean isAdmin=false;
    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt=LocalDateTime.now();
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Task> tasks;
    public String getRole() {
        return isAdmin ? "ADMIN" : "USER";
    }
    public void setRole(String role) {
        this.isAdmin = "ADMIN".equals(role);
    }
    private String profileImage;

    public User() {
    }


    public User(Long userid, String username, String password, String email, boolean isAdmin,LocalDateTime createdAt, List<Task> tasks,String profileImage) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
        this.tasks = tasks;
        this.profileImage=profileImage;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Task> getTask() {
        return tasks;
    }

    public void setTask(List<Task> tasks) {
        this.tasks = tasks;
    }
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
