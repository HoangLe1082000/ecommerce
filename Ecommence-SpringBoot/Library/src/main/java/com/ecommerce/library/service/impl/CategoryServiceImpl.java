package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.repository.CategoryRepository;
import com.ecommerce.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {
        Category categorySave = new Category(category.getName());
        return categoryRepository.save(categorySave);
    }

    @Override
    public Category findById(Long id) {
        return  categoryRepository.findById(id).get();
    }

    @Override
    public Category update(Category category) {

        Category categoryUpdate =  new Category();
        try{
            categoryUpdate.setId(category.getId());
            categoryUpdate.setName(category.getName());
            categoryUpdate.set_activated(true);
            categoryUpdate.set_deleted(false);
        }catch (Exception e){
            return null;
        }
        return categoryRepository.save(categoryUpdate);
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.getById(id);
        category.set_deleted(true);
        category.set_activated(false);
        categoryRepository.save(category);
    }

    @Override
    public void enableById(Long id) {
        Category category = categoryRepository.getById(id);
        category.set_deleted(false);
        category.set_activated(true);
        categoryRepository.save(category);
    }

    @Override
    public List<Category> findAllByActivated() {
        return categoryRepository.findAllByActivated();
    }

    @Override
    public List<CategoryDto> getCategoryAnyProduct() {
        return categoryRepository.getCategoryAnyProduct();
    }
}
