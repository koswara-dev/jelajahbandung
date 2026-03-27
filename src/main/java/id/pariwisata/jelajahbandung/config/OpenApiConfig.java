package id.pariwisata.jelajahbandung.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.servers.Server;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jelajahBandungOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        Server productionServer = new Server();
        productionServer.setUrl("https://devops.juaracoding.com/kelompok2");
        productionServer.setDescription("Production Server via NGINX");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development Server");

        return new OpenAPI()
                .info(new Info().title("Jelajah Bandung API")
                        .description("API Documentation for Jelajah Bandung Application.")
                        .version("v0.0.1"))
                .servers(List.of(productionServer, localServer))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
