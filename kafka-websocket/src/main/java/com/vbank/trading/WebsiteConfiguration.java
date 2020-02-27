package com.vbank.trading;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebsiteConfiguration implements WebMvcConfigurer{
    
    @Value("${server.document-root}")
    String documentRoot;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/*")
               .addResourceLocations("file:///" + documentRoot + "/");        
    }
}
