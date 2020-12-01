package com.example.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

public class I18n {
//    private static final Resource[] NO_RESOURCES = {};

    @Autowired
    private MessageSource messageSource;

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US); // Set default Locale as US
        return slr;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("i18n/messages");  // name of the resource bundle
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    public String getLanguage(String code, String locale) {
        return messageSource.getMessage(code, null, new Locale(locale));
    }

//    private Resource[] getResources(ClassLoader classLoader, String name) {
//        String target = name.replace('.', '/');
//        try {
//            return new PathMatchingResourcePatternResolver(classLoader)
//                    .getResources("classpath*:" + target + ".properties");
//        } catch (Exception ex) {
//            log.error(ex.getMessage(), ex);
//            return NO_RESOURCES;
//        }
//    }
}
