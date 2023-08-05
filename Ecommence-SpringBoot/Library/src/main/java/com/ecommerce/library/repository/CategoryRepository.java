package com.ecommerce.library.repository;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("Select c from Category c WHERE  c.is_activated = true and c.is_deleted = false")
    List<Category> findAllByActivated();

    /* Customer */
    @Query("select new com.ecommerce.library.dto.CategoryDto(c.id, c.name, count(c.id))  from Category  c inner  join  Product  p on p.category.id = c.id " +
            "where c.is_activated = true and c.is_deleted = false GROUP BY c.id, c.name")
    List<CategoryDto> getCategoryAnyProduct();

}
