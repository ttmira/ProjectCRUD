package com.example.Lab5.service;

import com.example.Lab5.Model.Category;
import com.example.Lab5.repository.categoryrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class categoryservice {
    @Autowired
    private categoryrepository categorysrepository;

    public void saveC(Category category){
        categorysrepository.save(category);
    }

    public List<Category>getallcategory(){
        return categorysrepository.findAll();
    }

    public Category getcategorybyid(Long categoryid){
        return categorysrepository.findByCategoryid(categoryid).orElse(null);
    }



    public Category newCategory(Category category){
        if (categorysrepository.findByCategoryname(category.getCategoryname()).isPresent()){
            throw new IllegalArgumentException("Category name already exists");
        }
        return categorysrepository.save(category);
    }

    public void updateC(Long categoryid,Category updateC){
        Category existc=getcategorybyid(categoryid);
        if (existc!=null){
            existc.setCategoryname(updateC.getCategoryname());
            existc.setDescreption(updateC.getDescreption());
            categorysrepository.save(existc);
        }
    }
    public void deleteC(Long categoryid){
        categorysrepository.deleteById(categoryid);
    }
}
