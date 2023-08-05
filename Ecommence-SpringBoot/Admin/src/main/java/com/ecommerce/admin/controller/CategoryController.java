package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String categories(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories",categories);
        model.addAttribute("size",categories.size());
        model.addAttribute("categoryNew",new Category());
        return "categories";
    }

    @PostMapping("/add-category")
    public String addCategory(@ModelAttribute("categoryNew") Category category, RedirectAttributes redirectAttributes){
        try {
            category.set_activated(true);
            category.set_deleted(false);
            categoryService.save(category);
            redirectAttributes.addFlashAttribute("success", "Add successfully !!!");
        }catch (DataIntegrityViolationException e){
            redirectAttributes.addFlashAttribute("failed", "Name has duplicated !!!");
            e.printStackTrace();
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("failed", "Failed to add new category !!!");
            e.printStackTrace();
        }

        return "redirect:/categories";
    }


    @GetMapping(value = "/findById")
    @ResponseBody
    public Category findById(Long id){
        return categoryService.findById(id);
    }


    @GetMapping("/update-category")
    public String updateCategoryName(Category category, RedirectAttributes redirectAttributes){
        try{
            System.out.println(category.toString());
            categoryService.update(category);
            redirectAttributes.addFlashAttribute("success", "Update Successfully");
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "Failed to update because duplicate name");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("failed", "Error Server");
        }
        return "redirect:/categories";
    }

    @RequestMapping(value = "/delete-category", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String deleteCategory(Long id, RedirectAttributes redirectAttributes){
        try{
            categoryService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Delete Successfully");
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "Failed to Delete because duplicate name");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("failed", "Error Server");
        }
        return "redirect:/categories";
    }

    @RequestMapping(value = "/enable-category", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String enableCategory(Long id, RedirectAttributes redirectAttributes){
        try{
            categoryService.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Enable Successfully");
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("failed", "Failed to Enable because duplicate name");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("failed", "Error Server");
        }
        return "redirect:/categories";
    }
}
