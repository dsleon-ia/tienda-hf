package com.hfsolutions.rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        String dynamicUrl = "http://localhost:" + serverPort + contextPath;
        
        return new OpenAPI()
                .info(new Info()
                        .title("Store API")
                        .version("1.0.0")
                        .description("API REST para la gestión integral de un catálogo de productos y categorías de una tienda en línea.\n\n" +
                                "### Propósito\n" +
                                "Esta API permite a las aplicaciones cliente (Frontend Web, Mobile, etc.) realizar operaciones de comercio electrónico como:\n" +
                                "*   Explorar y buscar productos con filtros avanzados (precio, categoría, texto).\n" +
                                "*   Administrar el inventario y las categorías de productos.\n" +
                                "*   Auditar los cambios realizados en el catálogo para fines de seguridad y trazabilidad.\n\n" +
                                "### Módulos Principales\n" +
                                "*   **Catálogo**: Gestión de categorías y clasificación de productos.\n" +
                                "*   **Inventario**: CRUD completo de productos, control de stock y precios.\n" +
                                "*   **Auditoría**: Registro inmutable de todas las operaciones de escritura sobre los productos.")
                        .contact(new Contact()
                                .name("Desarrollador: Diego Saavedra")
                                .email("dev@hfsolutions.com")))
                .servers(List.of(
                        new Server().url(dynamicUrl).description("Servidor Local (Puerto " + serverPort + ")"),
                        new Server().url(contextPath + "/").description("Servidor Relativo (Proxy-safe)")
                ));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/products/**", "/api/categories/**")
                .build();
    }

    @Bean
    public GroupedOpenApi auditApi() {
        return GroupedOpenApi.builder()
                .group("audit")
                .pathsToMatch("/api/audit/**")
                .build();
    }
}