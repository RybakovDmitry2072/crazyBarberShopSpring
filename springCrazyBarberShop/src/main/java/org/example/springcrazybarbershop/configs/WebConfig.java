package org.example.springcrazybarbershop.configs;

import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.converters.LocalDateTimeConverter;
import org.example.springcrazybarbershop.converters.StringToLocalDateTimeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    @Value("${image.storage.path}")
    private String imagePath;

    private final LocalDateTimeConverter localDateTimeConverter;
    private final StringToLocalDateTimeConverter stringToLocalDateTimeConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(localDateTimeConverter);
        registry.addConverter(stringToLocalDateTimeConverter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/appointments").setViewName("appointments");
        registry.addViewController("/error/403").setViewName("error/403");
        registry.addViewController("/error/404").setViewName("error/404");
        registry.addViewController("/error/500").setViewName("error/500");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/profile_images/**")
                .addResourceLocations("file:" + imagePath + "/");

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/", "classpath:/resources/");
    }
}
