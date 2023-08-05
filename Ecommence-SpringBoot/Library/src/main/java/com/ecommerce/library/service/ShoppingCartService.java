package com.ecommerce.library.service;

import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;

public interface ShoppingCartService {

    ShoppingCart addItemToCart(Product product, int quantity, Customer customer);

    ShoppingCart updateItemToCart(Product product, int quantity, Customer customer);

    ShoppingCart deleteItemToCart(Product product, int quantity, Customer customer);
}
