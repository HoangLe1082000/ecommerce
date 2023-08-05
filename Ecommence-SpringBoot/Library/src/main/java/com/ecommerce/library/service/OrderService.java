package com.ecommerce.library.service;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;

import java.util.List;

public interface OrderService {
    List<Order> getAll();

    void saveOrder(ShoppingCart shoppingCart);

    void acceptOrder(Long id);

    void cancelOrder(Long id);

    void rejectOrder(Long id);
}
