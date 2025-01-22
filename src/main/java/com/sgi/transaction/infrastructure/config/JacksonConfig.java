package com.sgi.transaction.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * Jackson global configuration for object serialization.
 * Configures ObjectMapper to not include null values ​​in serialization
 * and adds support for Java 8 date types.
 */
@Configuration
public class JacksonConfig {

    /**
     * Configures the ObjectMapper to ignore null values ​​during serialization
     * and registers the JavaTimeModule module to handle date and time types.
     *
     * @return The configured ObjectMapper.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return objectMapper;
    }
}
