package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShoppingCartServiceImpl  implements ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public ShoppingCart addItemToCart(Product product, int quantity, Customer customer) {
        ShoppingCart shoppingCart = customer.getShoppingCart();

        if(shoppingCart == null){
            shoppingCart = new ShoppingCart();
        }

        Set<CartItem> cartItems = shoppingCart.getCartItem();
        CartItem cartItem = findCartItem(cartItems, product.getId());
        if(cartItems == null){
            cartItems = new HashSet<>();
            if(cartItem == null){
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setTotalPrice(quantity * product.getCostPrice());
                cartItem.setQuantity(quantity);
                cartItem.setCart(shoppingCart);
                cartItems.add(cartItem);
                cartItemRepository.save(cartItem);
            }
        }else {
            if(cartItem == null){
                if(cartItem == null){
                    cartItem = new CartItem();
                    cartItem.setProduct(product);
                    cartItem.setTotalPrice(quantity * product.getCostPrice());
                    cartItem.setQuantity(quantity);
                    cartItem.setCart(shoppingCart);
                    cartItemRepository.save(cartItem);
                    cartItems.add(cartItem);
                }
            }else {
                cartItem.setTotalPrice(cartItem.getTotalPrice() + (quantity * product.getCostPrice()));
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemRepository.save(cartItem);
            }

        }
        shoppingCart.setCartItem(cartItems);

        int totalItem  = totalItems(shoppingCart.getCartItem());
        double totalPrice = totalPrice(shoppingCart.getCartItem());

        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        shoppingCart.setCustomer(customer);

        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart updateItemToCart(Product product, int quantity, Customer customer) {
        ShoppingCart shoppingCart = customer.getShoppingCart();

        Set<CartItem> cartItems = shoppingCart.getCartItem();

        CartItem cartItem = findCartItem(cartItems, product.getId());

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity * product.getCostPrice());
        cartItemRepository.save(cartItem);

        int totalItems = totalItems(cartItems);
        double totalPrice = totalPrice(cartItems);

        shoppingCart.setTotalItems(totalItems);
        shoppingCart.setTotalPrice(totalPrice);

        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart deleteItemToCart(Product product, int quantity, Customer customer) {
        ShoppingCart shoppingCart = customer.getShoppingCart();

        Set<CartItem> cartItems = shoppingCart.getCartItem();

        CartItem cartItem = findCartItem(cartItems, product.getId());

        cartItems.remove(cartItem);

        cartItemRepository.delete(cartItem);

        int totalItems = totalItems(cartItems);
        double totalPrice = totalPrice(cartItems);

        shoppingCart.setTotalItems(totalItems);
        shoppingCart.setTotalPrice(totalPrice);

        return shoppingCartRepository.save(shoppingCart);
    }

    private CartItem findCartItem(Set<CartItem> cartItems, Long productId){
        if(cartItems == null)   {
            return null;
        }
        CartItem cartItem = null;

        for(CartItem item : cartItems){
            if(item.getProduct().getId() == productId){
                cartItem = item;
            }
        }

        return cartItem;
    }

    private int totalItems(Set<CartItem> cartItems){
        int totalItems = 0;
        for(CartItem cartItem : cartItems){
            totalItems += cartItem.getQuantity();
        }
        return totalItems;
    }

    private double totalPrice(Set<CartItem> cartItems){
        double totalPrice = 0.0;
        for(CartItem cartItem : cartItems){
            totalPrice += cartItem.getTotalPrice();
        }

        return totalPrice;
    }
}
