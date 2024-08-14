package io.igorv404.library.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    Contact contact = new Contact()
        .name("Igor Vilkov")
        .email("igorvilkov404@gmail.com")
        .url("https://www.linkedin.com/in/igor-vilkov/");
    Info info = new Info()
        .title("Library")
        .version("1.0.1")
        .description("System for managing library")
        .contact(contact);
    return new OpenAPI().info(info);
  }
}
