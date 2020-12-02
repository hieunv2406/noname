package com.example.common;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class I18n {

    public static String getFieldLanguage(String code, Object... args) {
//        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        ResourceBundleMessageSource messageSource = SpringUtils.getBean("getMessageSource");
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public static Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

//    public static String getLanguage(MessageSource source, String code, Object[] params) {
//        return getLanguage1(source, code, params);
//    }
//
//    public static String getLanguage1(MessageSource source, String code, Object[] params) {
//        if (DataUtil.isNullOrEmpty(source)) {
//            throw new IllegalArgumentException("message source is null");
//        }
//        if (DataUtil.isNullOrEmpty(code)) {
//            throw new IllegalArgumentException("code is empty");
//        }
//        if (DataUtil.isNullOrEmpty(params)) {
//            params = new Object[]{};
//        }
//        return source.getMessage(code, params, LocaleContextHolder.getLocale());
//    }

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
