package com.caykhe.itforum.configs;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.caykhe.itforum.repositories")
public class LocaleFieldConfig {
    
    @Bean
    public MessageSource fieldMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:fields");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
}
