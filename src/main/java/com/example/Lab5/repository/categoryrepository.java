package com.example.Lab5.repository;

import com.example.Lab5.Model.Category;
import com.example.Lab5.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface categoryrepository extends JpaRepository<Category,Long> {
    Optional<Category> findByCategoryname(String categoryname);
    Optional<Category> findByCategoryid(Long categoryid);
    List<Category> findAll();
}
