package com.example.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class I18n {
    private static MessageSource messageSource = null;

    private I18n(MessageSource messageSource) {
        I18n.messageSource = messageSource;
    }

    public static MessageSource getMessageSource() {
        return messageSource;
    }

    public static String getFieldLanguage(String code, Object... args) {
//        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

//    private static final Resource[] NO_RESOURCES = {};
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
