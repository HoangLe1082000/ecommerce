package com.ecommerce.library.service.impl;

import com.ecommerce.library.service.ThymeleafService;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Map;
import java.util.Objects;

@Service
public class ThymeleafServiceImpl implements ThymeleafService {

    private final static String MAIL_TEMPLATE_BASE_NAME = "mail/MailMessage";

    private final static  String MAIL_TEMPLATE_PREFIX = "/templates/";

    private final static  String MAIL_TEMPLATE_SUFFIX = ".html";

    private final static  String UTF_8 = "UTF-8";

    private static TemplateEngine templateEngine;

    static {
        templateEngine = emailTemplateEngine();
    }

    private static TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine  templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(httpTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());

        return templateEngine;
    }

    private static ITemplateResolver httpTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(MAIL_TEMPLATE_PREFIX);
        templateResolver.setSuffix(MAIL_TEMPLATE_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(UTF_8);
        templateResolver.setCacheable(false);

        return templateResolver;
    }

    private static ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(MAIL_TEMPLATE_BASE_NAME);
        return messageSource;
    }

    @Override
    public String createContent(String template, Context context) {
        return templateEngine.process(template, context);
    }
}
