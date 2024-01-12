package co.com.bancolombia.api.helper;

import co.com.bancolombia.api.dto.ProductDTO;
import co.com.bancolombia.model.product.Product;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;

@UtilityClass
public class ProductHelper {
    public static Flux<ProductDTO> getFluxProductDTO(Flux<Product> productFlux) {
        return productFlux
                .map(product-> ProductDTO.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .price(product.getPrice())
                        .description(product.getDescription())
                        .images(product.getImages())
                        .category(ProductDTO.CategoryDTO.builder()
                                .id(product.getCategory().getId())
                                .name(product.getCategory().getName())
                                .build())
                        .build());

    }
}
