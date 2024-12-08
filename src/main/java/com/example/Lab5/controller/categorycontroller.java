package com.example.Lab5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.Lab5.service.categoryservice;
import org.springframework.ui.Model;
import com.example.Lab5.Model.Category;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping()
public class categorycontroller {

    @Autowired
    private  categoryservice categoryService;

    @GetMapping("/categorylist")
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getallcategory();
        model.addAttribute("categories", categories);
        return "categorylist";
    }

    // для создания новой категории
    @GetMapping("/newcategory")
    public String showCreateCategory(Model model) {
        model.addAttribute("category", new Category());
        return "newcategory";
    }
    @PostMapping("/newcategory")
    public String createCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        try {
            categoryService.newCategory(category);
            redirectAttributes.addFlashAttribute("success", "Category created successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/categorylist";
    }

    @GetMapping("/edit/{categoryid}")
    public String showEditCategoryForm(@PathVariable String categoryid, Model model, RedirectAttributes redirectAttributes) {
        Long id = Long.parseLong(categoryid);
        Category category = categoryService.getcategorybyid(id);
        if (category != null) {
            model.addAttribute("category", category);
            return "edit";
        } else {
            redirectAttributes.addFlashAttribute("error", "Category not found");
            return "redirect:/categorylist";
        }
    }


    @PostMapping("/update/{categoryid}")
    public String updateCategory(@PathVariable String categoryid, @ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        Long id=Long.parseLong(categoryid);
        try {
            categoryService.updateC(id,category);
            redirectAttributes.addFlashAttribute("success", "Category updated successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/categorylist";
    }


    @PostMapping("/delete/{categoryid}")
    public String deleteCategory(@PathVariable String categoryid, RedirectAttributes redirectAttributes) {
        Long id=Long.parseLong(categoryid);
        categoryService.deleteC(id);
        redirectAttributes.addFlashAttribute("success", "Category deleted successfully");
        return "redirect:/categorylist";
    }

}
