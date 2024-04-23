package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")  // Frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Content-Type", "Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }
    // CORS (Cross-Origin Resource Sharing) umožňuje webům omezit zdroje, které jsou sdíleny s jinými doménami.
    // Nastavením CORS na serveru můžeme specifikovat, které domény mají přístup k našim zdrojům.
    // Toto nastavení pomáhá chránit proti běžným webovým útokům, jako je Cross-Site Scripting (XSS) a Data theft.

}
