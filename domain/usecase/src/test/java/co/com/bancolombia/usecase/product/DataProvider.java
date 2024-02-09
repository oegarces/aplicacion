package co.com.bancolombia.usecase.product;

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
                .build();
    }
}
