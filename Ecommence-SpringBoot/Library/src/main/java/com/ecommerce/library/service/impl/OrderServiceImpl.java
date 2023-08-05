package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.OrderDetailsRepository;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void saveOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setOderStatus("PENDING");
        order.setCustomer(shoppingCart.getCustomer());
        order.setTotalPrice(shoppingCart.getTotalPrice());
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        Set<CartItem> cartItemList = shoppingCart.getCartItem();

        for(CartItem cartItem : cartItemList){
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setQuantity(cartItem.getQuantity());
            orderDetails.setOrder(order);
            orderDetails.setTotalPrice(cartItem.getTotalPrice());
            orderDetails.setProduct(cartItem.getProduct());
            orderDetails.setUnitPrice(cartItem.getProduct().getCostPrice());
            orderDetailsList.add(orderDetails);
            orderDetailsRepository.save(orderDetails);

            cartItemRepository.deleteCartItemByShoppingCart(cartItem.getCart().getId());
        }


        order.setOderDetailsList(orderDetailsList);
        orderRepository.save(order);
        shoppingCart.setCartItem(new HashSet<>());
        shoppingCart.setTotalItems(0);
        shoppingCart.setTotalPrice(0);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void acceptOrder(Long id) {
        Order order = orderRepository.getById(id);
        order.setDeliveryDate(new Date());
        order.setOderStatus("SHIPPING");
        orderRepository.save(order);

    }

    @Override
    public void cancelOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void rejectOrder(Long id) {
        Order order = orderRepository.getById(id);
        order.setOderStatus("REJECT");
        orderRepository.save(order);
    }
}
