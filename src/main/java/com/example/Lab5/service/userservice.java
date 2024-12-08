package com.example.Lab5.service;

import com.example.Lab5.Model.User;
import com.example.Lab5.repository.userrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class userservice  {
    @Autowired
    private userrepository usersrepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public void saveU(User user){
        if (usersrepository.count()==0){
           user.setAdmin(true);
        }else {
            user.setAdmin(false);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersrepository.save(user);
    }


    public  User findByid(Long userid){
        return usersrepository.findByUserid(userid).orElseThrow(()->new RuntimeException("User id not found"));
    }
    public Page<User> findAll(Pageable pageable){
        return usersrepository.findAll(pageable);
    }
    public List<User> getAllUsers(){
        return usersrepository.findAll();
    }
    public User getuserByUsername(String username){
        return usersrepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("user not found"));
    }
    public void makeAdmin(Long userid){
       User user=usersrepository.findByUserid(userid).orElseThrow(()->new UsernameNotFoundException("User not found"));
       user.setAdmin(true);
       usersrepository.save(user);
    }

    public  List<User>searchUser(String keyword){
        return usersrepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword,keyword);
    }
    public void deletU(Long userid){
        usersrepository.deleteById(userid);
    }
    public boolean usernameChek(String username){
        return usersrepository.findByUsername(username).isPresent();
    }
    public boolean emailChek(String email){
        return usersrepository.findByEmail(email).isPresent();
    }
    public void updateUser(User user) {
        Optional<User> existingUser = usersrepository.findById(user.getUserid());
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User currentUser = existingUser.get();
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            currentUser.setUsername(user.getUsername());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            currentUser.setEmail(user.getEmail());
        }
        if (user.getRole() != null) {
            currentUser.setRole(user.getRole()); // Обновляем роль
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            currentUser.setPassword(encodedPassword);
        }
        usersrepository.save(currentUser);
    }



}
