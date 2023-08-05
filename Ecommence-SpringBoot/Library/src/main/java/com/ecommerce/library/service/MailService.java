package com.ecommerce.library.service;

import com.ecommerce.library.model.Order;

public interface MailService {

    void sendMail(Long orderId);
}
