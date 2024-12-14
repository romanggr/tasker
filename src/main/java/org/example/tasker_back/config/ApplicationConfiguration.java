package org.example.tasker_back.config;


import org.example.tasker_back.security.JwtFilter;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public BCrypt bcrypt() {
        return new BCrypt();
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
}
