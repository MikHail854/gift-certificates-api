package ru.clevertec.ecl.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.clevertec.ecl.interceptor.IntegrationDataInterceptor;
import ru.clevertec.ecl.interceptor.OrderInterceptor;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final OrderInterceptor orderInterceptor;
    private final IntegrationDataInterceptor integrationDataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(orderInterceptor)
                .addPathPatterns("/orders/**");
        registry.addInterceptor(integrationDataInterceptor)
                .addPathPatterns("/*/update/**")
                .addPathPatterns("/*/save");
    }

}
