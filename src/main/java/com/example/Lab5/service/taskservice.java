package com.example.Lab5.service;

import com.example.Lab5.Model.Category;
import com.example.Lab5.Model.Task;
import com.example.Lab5.Model.User;
import com.example.Lab5.repository.categoryrepository;
import com.example.Lab5.repository.taskrepository;
import com.example.Lab5.repository.userrepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class taskservice {
    @Autowired
    private taskrepository tasksrepository;
    @Autowired
    private userrepository usersrepository;
    @Autowired
    private categoryrepository categorysrepository;

    public Page<Task> gettasksbytaskid(Long taskid,Pageable pageable) {
        return tasksrepository.findByTaskid(taskid,pageable);
    }

    public Task newtask(Task task, Long userid){
        User user=usersrepository.findByUserid(userid).orElseThrow(()->new RuntimeException("user not found"));
        task.setUser(user);
        validateDueDate(task.getDuedate());
        return tasksrepository.save(task);
    }

    public Page<Task>getAllTask(Pageable pageable){
        return tasksrepository.findAll(pageable);
    }
    public List<Task> getAlltaskuser(){
        return tasksrepository.findAll();
    }

    public Optional<Task>gettaskbyidforuser(Long taskid,User user){
        return tasksrepository.findByTaskidAndUser(taskid,user);
    }

    public Task gettaskbyid(Long task_id){
        return tasksrepository.findById(task_id).orElse(null);
    }
    public void savetask(Task tasks){
        tasksrepository.save(tasks);
    }

    @Transactional
    public Task updateT(Long taskid,Task updatedT,User user){
        Task existt=tasksrepository.findByTaskidAndUser(taskid,user).orElseThrow(()->new IllegalArgumentException("Task not found"));
            existt.setDescription(updatedT.getDescription());
            existt.setUser(updatedT.getUser());
            existt.setCategory(updatedT.getCategory());
            existt.setStatus(updatedT.getStatus());
            existt.setDuedate(updatedT.getDuedate());
            existt.setTitle(updatedT.getTitle());
            existt.setPriority(updatedT.getPriority());
            return tasksrepository.save(existt);
        }

    // Удаление задачи администратором
    public void deleteTaskAsAdmin(Long taskId) {
        Task task = tasksrepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        tasksrepository.delete(task);
    }

    public void deletT(Long taskid,User user){
        Task task=tasksrepository.findByTaskidAndUser(taskid,user).orElseThrow(()->new IllegalArgumentException("task not found"));
        tasksrepository.delete(task);
        }

    public Page<Task>sortbydata(User user,Pageable pageable){
        return tasksrepository.findByUserOrderByDuedateAsc(user,pageable);
    }
    public Page<Task> sortbystatus(User user,Pageable pageable) {
        return tasksrepository.findByUserOrderByStatus(user,pageable);
    }
    public Page<Task>filtrtaskbycategory(User user, Category category,Pageable pageable){
        return tasksrepository.findByUserAndCategory(user,category,pageable);
    }
    public Page<Task> filtrbystatus(User user, String status,Pageable pageable) {
        return tasksrepository.findByUserAndStatus(user, status,pageable);
    }

    public Page<Task> sortbydataadmin(LocalDate duedate, Pageable pageable){
        return tasksrepository.findAllOrderByDuedate(duedate,pageable);
    }
    public Page<Task> sortbystatusadmin(String status,Pageable pageable) {
        return tasksrepository.findAllOrderByStatus(status,pageable);
    }
    public Page<Task> filtrtaskbycategoryadmin(Category category,Pageable pageable){
        return tasksrepository.findByCategory(category,pageable);
    }
    public Page<Task> filtrbystatusadmin(String status,Pageable pageable) {
        return tasksrepository.findByStatus(status,pageable);
    }

    public Page<Task>searchTask(String keyword,Pageable pageable){
        return tasksrepository.findByTitleContainingIgnoreCaseOrPriorityContainingIgnoreCase(keyword,keyword,pageable);
    }


    private LocalDate validateDueDate(LocalDate duedate){
        if(duedate.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Due data can't past");
        }
        return duedate;
    }

    public Page<Task>getAlltaskuser(User user, Pageable pageable){
        return (Page<Task>) tasksrepository.findByuser(user,pageable);
    }


}
