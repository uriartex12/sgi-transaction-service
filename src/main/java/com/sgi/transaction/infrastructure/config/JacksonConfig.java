package com.sgi.transaction.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * Configuración global de Jackson para la serialización de objetos.
 * Se configura el ObjectMapper para que no incluya valores nulos en la serialización
 * y se agrega soporte para tipos de fecha de Java 8.
 */
@Configuration
public class JacksonConfig {

    /**
     * Configura el ObjectMapper para que ignore los valores nulos durante la serialización
     * y registre el módulo JavaTimeModule para manejar tipos de fecha y hora.
     *
     * @return El ObjectMapper configurado.
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
