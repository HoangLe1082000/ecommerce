package com.ecommerce.library.service;


import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    /* Admin */
    List<ProductDto> findAll();

    Product save(MultipartFile imageProduct,ProductDto productDto);
    ProductDto findById(Long id);
    Product update(MultipartFile imageProduct,ProductDto productDto);
    void deleteById(Long id);
    void enableById(Long id);

    Page<ProductDto> pageProducts(int pageNo);

    Page<ProductDto> searchProducts(int pageNo, String  keywords);

    /* Customer*/

    List<Product> getAllProducts();

    List<Product> listViewProduct();

    List<Product> getRelateProductByCategoryId(Long categoryId);


    Product getProductById(Long id);

    List<Product> getProductsInCategory( Long categoryId);

    List<Product> filterHighPrice();

    List<Product> filterLowPrice();

    List<Product> getAllProductsBySearch(String search);
}
