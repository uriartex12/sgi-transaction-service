package com.sgi.transaction.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración global de Jackson para la serialización de objetos.
 * Se configura el ObjectMapper para que no incluya valores nulos en la serialización.
 */
@Configuration
public class JacksonConfig {

    /**
     * Configura el ObjectMapper para que ignore los valores nulos durante la serialización.
     *
     * @return El ObjectMapper configurado.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }
}
