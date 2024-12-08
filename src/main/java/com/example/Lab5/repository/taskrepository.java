package com.example.Lab5.repository;

import com.example.Lab5.Model.Category;
import com.example.Lab5.Model.Task;
import com.example.Lab5.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface taskrepository extends JpaRepository<Task,Long> {
    List<Task> findByUser(User user);
    List<Task>findByTitleContainingIgnoreCaseOrPriorityContainingIgnoreCase(String title,String priority);
    Page<Task>findAll(Pageable pageable);
    Page<Task>findByTaskid(Long taskid,Pageable pageable);
    Optional<Task> findByTaskidAndUser(Long taskid,User user);
    Page<Task> findByuser(User user,Pageable pageable);
    Page<Task> findByUserAndCategory(User user, Category category,Pageable pageable);
    Page<Task>findByUserAndStatus(User user,String status,Pageable pageable);
    Page<Task>findByUserOrderByDuedateAsc(User user,Pageable pageable);
    Page<Task>findByUserOrderByStatus(User user,Pageable pageable);
    Page<Task> findByCategory(Category category,Pageable pageable);
    Page<Task> findByStatus(String status,Pageable pageable);
    Page<Task> findAllOrderByDuedate(LocalDateTime duedate, Pageable pageable);
    Page<Task> findAllOrderByStatus(String status,Pageable pageable);

}
