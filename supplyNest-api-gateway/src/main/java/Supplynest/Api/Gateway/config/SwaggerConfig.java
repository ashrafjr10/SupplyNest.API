package Supplynest.Api.Gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI supplyNestOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SupplyNest Auth Service")
                        .version("v1")
                        .description("Authentication APIs"));
    }
}