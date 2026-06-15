package github.com.AlexxCobb.advertising_stand.service;

import github.com.AlexxCobb.advertising_stand.controller.dto.ProductForStandDto;
import github.com.AlexxCobb.advertising_stand.controller.dto.enums.ProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private volatile ConcurrentHashMap<String, ProductForStandDto> productMap = new ConcurrentHashMap<>();
    private final WebClient webClient;

    public void loadProducts() {
        var products =
                webClient.get()
                        .uri("api/stand/products")
                        .retrieve()
                        .bodyToFlux(ProductForStandDto.class)
                        .collectList()
                        .timeout(Duration.ofSeconds(10))
                        .block();
        if (products == null) {
            return;
        }
        var updatedProducts =
                products.stream().collect(Collectors.toMap(ProductForStandDto::publicProductId, Function.identity()));
        var newMap = new ConcurrentHashMap<>(updatedProducts);
        productMap = newMap;
    }

    public List<ProductForStandDto> getPopularProducts() {
        return filterProductsByType(ProductType.TOP);
    }

    public List<ProductForStandDto> getDiscountProducts() {
        return filterProductsByType(ProductType.DISCOUNT);
    }

    public List<ProductForStandDto> getNewProducts() {
        return filterProductsByType(ProductType.NEW);
    }

    public void updateProduct(ProductForStandDto dtoToUpdate) {
        productMap.computeIfPresent(dtoToUpdate.publicProductId(),
                                    (k, existedDto) -> existedDto.withUpdatedFields(dtoToUpdate));
    }

    public void deleteProduct(String publicProductId) {
        productMap.remove(publicProductId);
    }

    private List<ProductForStandDto> filterProductsByType(ProductType type) {
        return productMap.values().stream()
                .filter(productForStandDto -> productForStandDto.type().equals(type))
                .toList();
    }
}