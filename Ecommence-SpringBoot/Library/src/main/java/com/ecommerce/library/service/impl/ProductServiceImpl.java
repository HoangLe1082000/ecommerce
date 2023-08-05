package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageUtils imageUtils;

    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        return transfer(products);
    }

    @Override
    public Product save(MultipartFile imageProduct, ProductDto productDto) {
        try{
            Product product = new Product();
            if(imageProduct == null){
                product.setImage(null);
            }else {
               if(imageUtils.uploadImage(imageProduct)){
                   System.out.println("Upload Successfully !!!");

               }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setCategory(productDto.getCategory());
            product.setDescription(productDto.getDescription());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCostPrice(productDto.getCostPrice());
            product.set_activated(true);
            product.set_deleted(false);

            return productRepository.save(product);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id).get();
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setImage(product.getImage());
        productDto.setCategory(product.getCategory());
        productDto.setActivated(product.is_activated());
        productDto.setDeleted(product.is_deleted());

        return productDto;
    }

    @Override
    public Product update(MultipartFile imageProduct, ProductDto productDto) {
        try{
            Product product = productRepository.findById(productDto.getId()).get();
            product.setId(productDto.getId());
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.set_activated(true);
            product.set_deleted(false);
            System.out.println(imageProduct.getOriginalFilename());
            if(imageProduct.getOriginalFilename().isEmpty()){
                product.setImage(product.getImage());
            }else {
                if(imageUtils.checkExitsFile(imageProduct) == false){

                    imageUtils.uploadImage(imageProduct);
                    System.out.println("Update image  Successfully !!!");
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }

            return productRepository.save(product);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.getById(id);
        product.set_deleted(true);
        product.set_activated(false);
        productRepository.save(product);
    }

    @Override
    public void enableById(Long id) {
        Product product = productRepository.getById(id);
        product.set_deleted(false);
        product.set_activated(true);
        productRepository.save(product);
    }

    @Override
    public Page<ProductDto> pageProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo,5);
        List<ProductDto> products = transfer(productRepository.findAll());
        Page<ProductDto> productDtos = toPage(products, pageable);
        return productDtos;
    }

    private Page toPage(List<ProductDto> list, Pageable pageable){
        if(pageable.getOffset() >= list.size()){
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size() : (int) (pageable.getOffset() + pageable.getPageSize());
        List sublist = list.subList(startIndex, endIndex);
        return  new PageImpl(sublist, pageable, list.size());
    }

    @Override
    public Page<ProductDto> searchProducts(int pageNo,String keywords) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<Product> products = productRepository.searchProductsList(keywords, pageable);
        Page<ProductDto> productDtos = toPage(transfer(products), pageable);
        return productDtos;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public List<Product> listViewProduct() {
        return productRepository.listViewProduct();
    }

    @Override
    public List<Product> getRelateProductByCategoryId(Long categoryId) {
        return productRepository.getRelateProductByCategoryId(categoryId);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.getById(id);
    }

    @Override
    public List<Product> getProductsInCategory(Long categoryId) {
        return productRepository.getProductsInCategory(categoryId);
    }

    @Override
    public List<Product> filterHighPrice() {
        return productRepository.filterHighPrice();
    }

    @Override
    public List<Product> filterLowPrice() {
        return productRepository.filterLowPrice();
    }

    @Override
    public List<Product> getAllProductsBySearch(String search) {
        List<Product> products = productRepository.getAllProducts();
        products = products.stream().filter(product -> product.getName().equals(search)).collect(Collectors.toList());
        return products;
    }

    private List<ProductDto> transfer(List<Product> products){
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product : products){
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setImage(product.getImage());
            productDto.setCategory(product.getCategory());
            productDto.setActivated(product.is_activated());
            productDto.setDeleted(product.is_deleted());

            productDtos.add(productDto);
        }
        return productDtos;
    }
}
