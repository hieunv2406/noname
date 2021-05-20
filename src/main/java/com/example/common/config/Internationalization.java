package com.example.common.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * @author owl
 * Cấu hình đa ngôn ngữ
 */
@Configuration
public class Internationalization {
    @Bean(name = "messageSource")
    public MessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.setBasename("i18n/messages"); dùng cho ResourceBundleMessageSource định nghĩa 1 thằng mess
        messageSource.setBasenames("classpath:i18n/messages", "classpath:i18n/validation"); // dùng cho ReloadableResourceBundleMessageSource định nghĩa > 1 thằng
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("vi", "VN")); // Set default Locale as US
        return localeResolver;
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean(MessageSource messageSource) {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource);
        return validatorFactoryBean;
    }
}
