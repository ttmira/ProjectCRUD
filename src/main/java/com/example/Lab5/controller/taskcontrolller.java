package com.example.Lab5.controller;

import com.example.Lab5.Model.Category;
import com.example.Lab5.Model.Task;
import com.example.Lab5.Model.User;
import com.example.Lab5.service.categoryservice;
import com.example.Lab5.service.taskservice;
import com.example.Lab5.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/tasklist")
public class taskcontrolller {

    @Autowired
    private taskservice tasksservice;
    @Autowired
    private userservice usersservice;
    @Autowired
    private categoryservice categorysservice;


    //списк задач для юзер
    @GetMapping("/user/tasks")
    @PreAuthorize("hasAuthority('USER')or hasAuthority('ADMIN')")
    public String listUserTasks(@RequestParam(value="category",required = false)Long categoryid,
                                @RequestParam(value = "status",required = false)String status,
                                @RequestParam(value = "sort",required = false)String sort, Model model, Principal principal, Pageable pageable){
        User currentUser=usersservice.getuserByUsername(principal.getName());
        Page<Task> tasks;
        if (categoryid != null) {
            Category category = categorysservice.getcategorybyid(categoryid);
            tasks = tasksservice.filtrtaskbycategory(currentUser, category,pageable);
        } else if (status != null) {
            Task statusTask = new Task();
            statusTask.setStatus(status);
            tasks = tasksservice.filtrbystatus(currentUser, status,pageable);
        } else if ("duedate".equals(sort)) {
            tasks = tasksservice.sortbydata(currentUser,pageable);
        } else if ("status".equals(sort)) {
            tasks = tasksservice.sortbystatus(currentUser,pageable);
        } else {
            tasks = tasksservice.sortbydata(currentUser,pageable);
        }
        model.addAttribute("tasks",tasks);
        model.addAttribute("category",categorysservice.getallcategory());
        return "tasklist";
    }

    @GetMapping("/admin/tasks")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String listAllTasks(
            @RequestParam(value = "category", required = false) Long categoryid,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "duedate", required = false) LocalDate duedate,
            @RequestParam(value = "sort", required = false) String sort,
            Model model, Pageable pageable){
        Page<Task> tasks;

            if (categoryid != null) {
                Category category = categorysservice.getcategorybyid(categoryid);
                tasks = tasksservice.filtrtaskbycategoryadmin(category, pageable);
            } else if (status != null) {
                tasks = tasksservice.filtrbystatusadmin(status, pageable);
            } else if ("duedate".equals(sort)) {
                tasks = tasksservice.sortbydataadmin(duedate,pageable);
            } else if ("status".equals(sort)) {
                tasks = tasksservice.sortbystatusadmin(status,pageable);
            } else {
                tasks = tasksservice.getAllTask(pageable);
            }

        model.addAttribute("tasks", tasks);
        model.addAttribute("category", categorysservice.getallcategory());
        return "tasklist-admin";
    }

    @GetMapping("/search")
    public String searchTask(@RequestParam("keyword")String keyword,Model model,Pageable pageable){
        Page<Task>tasks=tasksservice.searchTask(keyword,pageable);
        model.addAttribute("tasks",tasks);
        return "tasklist";
    }

    @GetMapping("/searchadmin")
    public String searchTaskUser(@RequestParam("keyword")String keyword,Model model,Pageable pageable){
        Page<Task>tasks=tasksservice.searchTask(keyword,pageable);
        List<User>users=usersservice.searchUser(keyword);
        model.addAttribute("users",users);
        model.addAttribute("tasks",tasks);
        return "tasklist-admin";
    }

    //форма для создания ню задач
    @GetMapping("/taskadd")
    @PreAuthorize("hasAuthority('USER')or hasAuthority('ADMIN')")
    public String showCreate(Model model){
        model.addAttribute("task",new Task());
        model.addAttribute("categories",categorysservice.getallcategory());
        model.addAttribute("users",usersservice.getAllUsers());
        return "taskadd";
    }
    //обработка
    @PostMapping("/taskadd")
    @PreAuthorize("hasAuthority('USER')or hasAuthority('ADMIN')")
    public String addnewTask(@ModelAttribute Task task,Principal principal, RedirectAttributes redirectAttributes) {
        User currentUser = usersservice.getuserByUsername(principal.getName());
        if (!currentUser.isAdmin()) {
            task.setUser(currentUser);
        }
        tasksservice.newtask(task,task.getUser().getUserid());
            redirectAttributes.addFlashAttribute("succes", "Task created success");
            return "redirect:/tasklist/admin/tasks";

    }

    @GetMapping("/taskadduser")
    @PreAuthorize("hasAuthority('USER')or hasAuthority('ADMIN')")
    public String showaddtaskForuser(Model model){
        model.addAttribute("task",new Task());
        model.addAttribute("categories",categorysservice.getallcategory());
        return "taskadduser";
    }
    @PostMapping("/taskadduser")
    @PreAuthorize("hasAuthority('USER')")
    public String addTaskAsUser(@ModelAttribute Task task, Principal principal, RedirectAttributes redirectAttributes) {
        User currentUser = usersservice.getuserByUsername(principal.getName());
        task.setUser(currentUser); // Задача всегда привязана к текущему пользователю
        tasksservice.newtask(task, currentUser.getUserid());
        redirectAttributes.addFlashAttribute("success", "Task created successfully");
        return "redirect:/tasklist/user/tasks";
    }

    //redacting
    @GetMapping("/taskedit/{taskid}")
    @PreAuthorize("hasAuthority('USER')or hasAuthority('ADMIN')")
    public String EditTask(@PathVariable String taskid,@AuthenticationPrincipal User user,Model model,RedirectAttributes redirectAttributes){
        Long id = Long.parseLong(taskid);
        Optional<Task> task=tasksservice.gettaskbyidforuser(id,user);
        if (task.isPresent()){
            model.addAttribute("task",task.get());
            return "taskedit";
        }else{
            redirectAttributes.addFlashAttribute("error","task not found");
            return "redirect:/tasklist/user/tasks";
        }
    }

    //obnov
    @PostMapping("/taskedit/{taskid}")
    @PreAuthorize("hasAuthority('USER')or hasAuthority('ADMIN')")
    public String update(@PathVariable String taskid,@ModelAttribute Task task,@AuthenticationPrincipal User user,RedirectAttributes redirectAttributes){
        Long id = Long.parseLong(taskid);
        try {
            tasksservice.updateT(id,task,user);
            redirectAttributes.addFlashAttribute("success","Task update success");
        }catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error","task not found");
        }
        return "redirect:/tasklist/user/tasks";
    }

    @GetMapping("/DELETE/{taskid}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public String deleteTasK(@PathVariable("taskid") String taskid,@AuthenticationPrincipal User user,Model model) {
        Long id = Long.parseLong(taskid);
        tasksservice.deletT(id,user);
        model.addAttribute("message", "task deleted successfully");
        return "user/tasks";
    }


    @PostMapping("/DELETE/{taskid}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public String deleteTask(
            @PathVariable String taskid,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes) {
        Long id = Long.parseLong(taskid);
        try {
            if (user.isAdmin()) {
                tasksservice.deleteTaskAsAdmin(id);
                redirectAttributes.addFlashAttribute("success", "Task deleted successfully by admin.");
            } else {
                tasksservice.deletT(id, user);
                redirectAttributes.addFlashAttribute("success", "Task deleted successfully.");
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Task not found.");
        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("error", "You are not authorized to delete this task.");
        }
        return user.isAdmin() ? "redirect:/tasklist/admin/tasks" : "redirect:/tasklist/user/tasks";
    }





}
