package com.example.Lab5.controller;

import com.example.Lab5.Model.User;
import com.example.Lab5.repository.userrepository;
import com.example.Lab5.service.userservice;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Controller
public class usercontroller {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private userservice userService;
    @Autowired
    private userrepository usersrepository;

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user",new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.saveU(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showlogin(Model model){
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model,HttpSession session) {
        User user = userService.getuserByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            session.setAttribute("user", user);
            return user.isAdmin() ? "redirect:/adminhome" : "redirect:/userhome";
        } else {
            model.addAttribute("error", "invalid user or password");
            return "login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/search")
    public String searchUser(@RequestParam("keyword")String keyword,Model model){
        List<User>users=userService.searchUser(keyword);
        model.addAttribute("users",users);
        return "userlist";
    }


    @PostMapping("/upload/images/{userid}")
    public String uploadImages(
            @PathVariable("userid")String userid,
            @RequestParam("profileImage") MultipartFile file,
            RedirectAttributes redirectAttributes) {
            Long id = Long.parseLong(userid);
        try {
            String uploadDir = "C:\\Users\\Admin\\Downloads\\Lab5\\Lab5\\src\\main\\resources\\static\\images\\";
            if (!file.isEmpty()) {
                String file_name = file.getOriginalFilename();
                Path file_path = Paths.get(uploadDir + file_name);

                Files.copy(file.getInputStream(), file_path, StandardCopyOption.REPLACE_EXISTING);

                Optional<User> user = usersrepository.findById(id);
                if (user.isPresent()) {
                    User u = user.get();
                    u.setProfileImage("/images/" + file_name);
                    usersrepository.save(u);
                }
            }
            redirectAttributes.addFlashAttribute("message", "фото загрузилось!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "фото не загрузилось!");
        }
        return "redirect:/userhome/" + userid;
    }

    //USER
    @GetMapping("/userhome/{username}")
    @PreAuthorize("hasAuthority('USER')or hasAuthority('ADMIN')")
    public String homepage( Model model,Principal principal){
        String username=principal.getName();
       User user=userService.getuserByUsername(username);
        if(user==null){
            return "redirect:/login";
        }
        model.addAttribute("user",user);
        return "userhome";
    }

    @PostMapping("/user/update")
    @PreAuthorize("hasAuthority('USER')")
    public String updateProfile(@ModelAttribute User user, Principal principal, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getuserByUsername(principal.getName());
        if (currentUser.getUserid().equals(user.getUserid())) {
            userService.saveU(user);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action");
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/user/delete")
    @PreAuthorize("hasAuthority('USER')")
    public String deleteProfile(Principal principal, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getuserByUsername(principal.getName());
        if (currentUser != null) {
            userService.deletU(currentUser.getUserid());
            redirectAttributes.addFlashAttribute("success", "Account deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found");
        }
        return "redirect:/login";
    }








    //ADMIN
    @GetMapping("/adminhome/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminHome(Model model,Principal principal) {
        String username=principal.getName();
        User admin=userService.getuserByUsername(username);
        if(admin==null){
            return "redirect:/login";
        }
        model.addAttribute("admin",admin);
        return "adminhome";
    }
    //LIST ALL USERS
    @GetMapping("/userlist")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String listUsers(Model model){
        model.addAttribute("users",userService.getAllUsers());
        return "userlist";
    }


    //ADD NEW ADMIN
    @GetMapping("/newadmin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showNewAdminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "newadmin";
    }
    @PostMapping(value = "/newadmin",params = "userid")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String makeAdmin(@RequestParam("userid") String userid, RedirectAttributes redirectAttributes) {
        try {
            Long id = Long.parseLong(userid);
            if (userService.findByid(id) != null) {
                userService.makeAdmin(id);
                redirectAttributes.addFlashAttribute("success", "User is now an Admin");
            } else {
                redirectAttributes.addFlashAttribute("error", "User not found");
            }
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid user ID format");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
        }

        return "redirect:/newadmin";
    }



    @PostMapping(value = "/newadmin",params = "user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createNewAdmin(@ModelAttribute("newUser") User user, RedirectAttributes redirectAttributes) {
        if (userService.usernameChek(user.getUsername()) || userService.emailChek(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Username or email already exists");
            return "redirect:/newadmin";
        }
        userService.saveU(user);
        userService.makeAdmin(user.getUserid());
        redirectAttributes.addFlashAttribute("success", "User successfully created and assigned as Admin");
        return "redirect:/newadmin";
    }


    @GetMapping("/delete/{userid}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser(@PathVariable("userid") String userid, Model model) {
        Long id = Long.parseLong(userid);
        userService.deletU(id);
        model.addAttribute("message", "User deleted successfully");
        return "delete";
    }

    @PostMapping("/delete/{userid}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser(@PathVariable("userid") String userid, RedirectAttributes redirectAttributes) {
        Long id = Long.parseLong(userid);
        if (userService.findByid(id) != null) {
            userService.deletU(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found");
        }
        return "redirect:/userlist";
    }



@GetMapping("/useredit/{userid}")
public String showEditUserForm(@PathVariable("userid") String userid, Model model) {
    Long id = Long.parseLong(userid);
    User user = userService.findByid(id);
    if (user == null) {
        throw new RuntimeException("User not found with id: " + id);
    }
    model.addAttribute("user", user);
    model.addAttribute("roles", List.of("USER", "ADMIN")); // Добавьте список ролей
    return "useredit";
}

    @PostMapping("/useredit/{userid}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateUser(
            @PathVariable("userid") String userid,
            @ModelAttribute User user,
            RedirectAttributes redirectAttributes) {

        Long id = Long.parseLong(userid.trim());
        if (userService.usernameChek(user.getUsername()) || userService.emailChek(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Username or email already exists");
            return "redirect:/useredit/" + userid;
        }
        if ("ADMIN".equals(user.getRole())) {
            user.setAdmin(true);
        } else {
            user.setAdmin(false);
        }
        user.setUserid(id);
        userService.updateUser(user);
        redirectAttributes.addFlashAttribute("success", "User updated successfully");
        return "redirect:/userlist";
    }




}
