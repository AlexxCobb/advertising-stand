package github.com.AlexxCobb.advertising_stand.config;

import github.com.AlexxCobb.advertising_stand.service.ProductService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RunAfterStartup {

    @Bean
    public ApplicationRunner loadProducts(ProductService productService) {
        return args -> productService.loadProducts();
    }
}
