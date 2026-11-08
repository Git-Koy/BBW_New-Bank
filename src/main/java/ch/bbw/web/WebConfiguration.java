package ch.bbw.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS configuration. The React frontend runs on the Vite dev server (localhost:5173), which is another origin than the backend (localhost:8080),
 * so the browser would block its requests without this. Only this origin is allowed, not every website.
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    /**
     * The origin of the Vite dev server
     */
    public static final String FRONTEND_ORIGIN = "http://localhost:5173";

    /**
     * Allows the frontend to call every endpoint
     *
     * @param registry the CORS registry of Spring
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(FRONTEND_ORIGIN)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
