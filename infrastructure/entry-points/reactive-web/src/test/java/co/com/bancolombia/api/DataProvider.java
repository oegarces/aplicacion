package co.com.bancolombia.api;

import co.com.bancolombia.model.product.Category;
import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Flux;

public class DataProvider {
    public static Flux<Product> getFluxProduct() {
        return Flux.just(
                getProduct(1, "tv"),
                getProduct(2, "pc"));
    }

    public static Product getProduct(Integer id, String title) {

        return Product.builder()
                .id(id)
                .title(title)
                .category(Category.builder()
                        .id(1)
                        .build())
                .build();
    }
}
