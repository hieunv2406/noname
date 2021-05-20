package com.example.common;

import com.example.common.utils.DataUtil;
import com.example.common.utils.SpringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

public class I18n {

//    public static String getFieldLanguage(String code, Object... args) {
////        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
//        ResourceBundleMessageSource messageSource = SpringUtils.getBean("getMessageSource");
//        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
//    }

    public static Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    public static String getLanguage(String code, Object... args) {
        ReloadableResourceBundleMessageSource messageSource = SpringUtils.getBean("messageSource");
        if (DataUtil.isNullOrEmpty(messageSource)) {
            throw new IllegalArgumentException("message source is null");
        }
        if (DataUtil.isNullOrEmpty(code)) {
            throw new IllegalArgumentException("code is empty");
        }
        if (DataUtil.isNullOrEmpty(args)) {
            args = new Object[]{};
        }
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
