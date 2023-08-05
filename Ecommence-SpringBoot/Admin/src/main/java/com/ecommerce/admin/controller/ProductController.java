package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }

        List<ProductDto> productDtos = productService.findAll();
        model.addAttribute("productDtos", productDtos);
        model.addAttribute("size", productDtos.size());
        return "products";
    }

    @GetMapping("/products/{pageNo}")
    public String productsPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }

        Page<ProductDto> productPage = productService.pageProducts(pageNo);
        model.addAttribute("size", productPage.getSize());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("productDtos", productPage);

        return "products";
    }

    @GetMapping("/search-products/{pageNo}")
    public String searchProducts(@PathVariable("pageNo") int pageNo,@RequestParam("keyword")String keyword,  Model model, Principal principal){
        if(principal ==null){
            return "redirect:/login";
        }
        Page<ProductDto> products = productService.searchProducts(pageNo,keyword);


        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("productDtos", products);

        return "result-products";
    }



    @GetMapping("/add-product")
    public String addCategory(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", categoryService.findAllByActivated());
        return "add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("product")ProductDto productDto, @RequestParam("imageProduct") MultipartFile imageProduct
    ,RedirectAttributes redirectAttributes){
        try {

            productService.save(imageProduct,productDto);
            redirectAttributes.addFlashAttribute("success", "Add successfully !!!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("failed", "Failed to add new products !!!");
            e.printStackTrace();
        }

        return "redirect:/products/0";
    }

    @GetMapping("/update-product/{id}")
    public String updateProduct(Model model, Principal principal, @PathVariable("id") Long id){
        if(principal == null){
            return "redirect:/login";
        }
        ProductDto productDto = productService.findById(id);
        model.addAttribute("categories", categoryService.findAllByActivated());
        model.addAttribute("product", productDto);
        return "update-product";
    }

    @PostMapping("update-product")
    public String updateProduct(@ModelAttribute("product")ProductDto productDto, @RequestParam("imageProduct") MultipartFile imageProduct
            ,RedirectAttributes redirectAttributes){
        try {
            productService.update(imageProduct,productDto);
            redirectAttributes.addFlashAttribute("success", "Add successfully !!!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("failed", "Failed to add new products !!!");
            e.printStackTrace();
        }

        return "redirect:/products/0";
    }

    @RequestMapping(value = "/delete-product", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String deleteProduct(Long id, RedirectAttributes redirectAttributes){
        try{
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Delete Successfully");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("failed", "Error Server");
        }
        return "redirect:/products/0";
    }

    @RequestMapping(value = "/enable-product", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String enableProduct(Long id, RedirectAttributes redirectAttributes){
        try{
            productService.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Enable Successfully");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("failed", "Error Server");
        }
        return "redirect:/products/0";
    }


}
