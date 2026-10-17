package tn.esprit.autoloc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI autolocOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("AutoLoc API")
                .description("Plateforme de gestion de location de véhicules multi-agences")
                .version("0.0.1"));
    }
}
