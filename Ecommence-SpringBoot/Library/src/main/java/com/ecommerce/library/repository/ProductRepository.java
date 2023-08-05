package com.ecommerce.library.repository;

import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select p from  Product  p")
    Page<Product> pageProduct(Pageable pageable);

    @Query("select p from  Product  p where p.description like %?1% or p.name like %?1%")
    Page<Product> searchProducts(String  keywords, Pageable pageable);

    @Query("select p from  Product  p where p.description like %?1% or p.name like %?1%")
    List<Product> searchProductsList(String  keywords, Pageable pageable);

    /* Customer */
    @Query("select p from  Product  p where  p.is_deleted = false and p.is_activated = true")
    List<Product> getAllProducts();

    @Query(value = "select * from  products  p where  p.is_deleted = false and p.is_activated = true order by  rand() asc limit 4", nativeQuery = true)
    List<Product> listViewProduct();

    @Query("select p from Product p inner join  Category  c ON  c.id = p.category.id where  p.category.id = ?1 and p.is_activated = true and p.is_deleted = false ")
    List<Product> getRelateProductByCategoryId(Long categoryId);

    @Query(value = "select  p from Product p inner  join Category  c on c.id = p.category.id where  c.id = ?1 and p.is_deleted = false and  p.is_activated =true ")
    List<Product> getProductsInCategory( Long categoryId);

    @Query("select p from  Product  p where  p.is_deleted = false and p.is_activated = true order by  p.costPrice desc")
    List<Product> filterHighPrice();

    @Query("select p from  Product  p where  p.is_deleted = false and p.is_activated = true order by  p.costPrice asc")
    List<Product> filterLowPrice();
}
