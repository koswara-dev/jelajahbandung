package id.pariwisata.jelajahbandung.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    // http://localhost:8080/uploads/filename.png

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Expose the uploads directory to be accessible via HTTP
        // Maps /uploads/** to the file locations in the uploads directory
        String path = Paths.get(uploadDir).toAbsolutePath().toUri().toString();
        if (!path.endsWith("/")) {
            path += "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(path);
    }
}
