package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.MailService;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ThymeleafService;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class MailSenderImpl implements MailService {


    @Autowired
     JavaMailSender mailSender;

    @Autowired
    private OrderRepository orderRepository;

    @Value("${spring.mail.username}")
    private String email;

    @Autowired
    private ThymeleafService thymeleafService;

    @Override
    public void sendMail(Long orderId) {


        try{
            Order order = orderRepository.findById(orderId).get();
            Customer customer = order.getCustomer();
            String customerEmail = customer.getUsername();
            MimeMessage message = mailSender.createMimeMessage();

            System.out.println("Oder : " + order.getOderDetailsList().get(0).getProduct());

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                            StandardCharsets.UTF_8.name());

            helper.setFrom(email);
            helper.setTo(customerEmail);
            helper.setSubject("Order Successfull !!!");

            String[] bccObject = {"abc@gmail.com"};
            helper.setBcc(bccObject);

            List<OrderDetails> orderDetailsList =  order.getOderDetailsList();

            Context context = new Context();

            context.setVariable("orderDetailsList", orderDetailsList);
            context.setVariable("order_id", orderId);
            final String template = "custom-mail-template.html";

            helper.setText(thymeleafService.createContent(template,context),true);

            mailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
