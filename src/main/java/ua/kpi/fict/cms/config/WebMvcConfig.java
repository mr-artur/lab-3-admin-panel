package ua.kpi.fict.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource getReloadableResourceBundle(){
        ReloadableResourceBundleMessageSource resourceBundle = new ReloadableResourceBundleMessageSource();
        resourceBundle.setBasename("classpath:messages");
        resourceBundle.setDefaultEncoding("UTF-8");
        return resourceBundle;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("*/**").
            addResourceLocations(
                "classpath:/META-INF/resources/",
                "classpath:/resources/",
                "classpath:/static/",
                "classpath:/public/");
    }
}
