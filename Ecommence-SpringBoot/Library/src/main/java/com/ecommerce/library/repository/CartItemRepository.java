package com.ecommerce.library.repository;

import com.ecommerce.library.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Modifying
    @Query("delete FROM CartItem  c where  c.cart.id = :id")
    void deleteCartItemByShoppingCart(@Param("id") Long shoppingCartId);

}
