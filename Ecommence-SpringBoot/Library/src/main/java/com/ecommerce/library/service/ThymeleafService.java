package com.ecommerce.library.service;

import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Objects;

public interface ThymeleafService {

    String createContent(String template, Context context);
}
