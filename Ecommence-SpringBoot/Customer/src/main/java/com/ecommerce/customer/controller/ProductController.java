package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String products(Model model){
        List<Category> categories = categoryService.findAll();
        List<Product> products = productService.getAllProducts();
        List<Product> viewProducts = productService.listViewProduct();
        List<CategoryDto> categoryDtos = categoryService.getCategoryAnyProduct();

        model.addAttribute("categories", categories);
        model.addAttribute("categoryDtos", categoryDtos);
        model.addAttribute("products", products);
        model.addAttribute("viewProducts", viewProducts);


        return "shop";
    }

    @GetMapping("/find-product/{id}")
    public String findProductById(@PathVariable("id") Long id, Model model){
        Product product = productService.getProductById(id);
        List<Product> productRelationList = productService.getRelateProductByCategoryId(product.getCategory().getId());

        model.addAttribute("product", product);
        model.addAttribute("productRelationList", productRelationList);
        System.out.println(productRelationList.size());

        return "product-detail";
    }

    @GetMapping("/products-in-category/{id}")
    public String getProductsInCategory(Model model, @PathVariable("id") Long id){
        List<Category> categories = categoryService.findAll();
        Category category = categoryService.findById(id);
        List<Product> products = productService.getProductsInCategory(id);
        List<CategoryDto> categoryDtos = categoryService.getCategoryAnyProduct();

        model.addAttribute("categoryDtos", categoryDtos);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);

        return "product-in-category";
    }

    @GetMapping("/products/high-price")
    public String filterHighPrice(Model model){
        List<Product> products = productService.filterHighPrice();
        List<Product> viewProducts = productService.listViewProduct();
        List<CategoryDto> categoryDtos = categoryService.getCategoryAnyProduct();
        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("categoryDtos", categoryDtos);
        model.addAttribute("products", products);
        model.addAttribute("viewProducts", viewProducts);


        return "filter-high-price";
    }


    @GetMapping("/products/low-price")
    public String filterLowPrice(Model model){
        List<Product> products = productService.filterLowPrice();
        List<Product> viewProducts = productService.listViewProduct();
        List<CategoryDto> categoryDtos = categoryService.getCategoryAnyProduct();
        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("categoryDtos", categoryDtos);
        model.addAttribute("products", products);
        model.addAttribute("viewProducts", viewProducts);


        return "filter-low-price";
    }

    @GetMapping("/search-products")
    public String searchProducts(@RequestParam(value = "search", required = false, defaultValue = "") String search,
                                     Model model){
        List<Category> categories = categoryService.findAll();
        List<Product> products = productService.getAllProductsBySearch(search);
        List<Product> viewProducts = productService.listViewProduct();
        List<CategoryDto> categoryDtos = categoryService.getCategoryAnyProduct();

        model.addAttribute("categories", categories);
        model.addAttribute("categoryDtos", categoryDtos);
        model.addAttribute("products", products);
        model.addAttribute("viewProducts", viewProducts);

        return "shop";
    }

}
