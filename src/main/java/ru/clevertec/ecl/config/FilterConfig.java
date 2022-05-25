package ru.clevertec.ecl.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.ecl.filter.ReplaceStreamFilter;

import javax.servlet.Filter;

@Configuration
public class FilterConfig {


    /**
     * Регистрация фильтра
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(replaceStreamFilter());
        registration.addUrlPatterns("/gifts/update/**");
        registration.addUrlPatterns("/gifts/save");
        registration.setName("streamFilter");
        return registration;
    }

    /**
     * Создание StreamFilter
     *
     * @return Filter
     */
    @Bean(name = "replaceStreamFilter")
    public Filter replaceStreamFilter() {
        return new ReplaceStreamFilter();
    }

}
