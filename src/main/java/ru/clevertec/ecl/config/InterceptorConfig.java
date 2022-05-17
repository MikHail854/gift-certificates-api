package ru.clevertec.ecl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.clevertec.ecl.interceptor.RequestGetInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private RequestGetInterceptor requestGetInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestGetInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/health-check");
    }

}
