package co.com.bancolombia.model.product.gateway;

import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductGateway {
    Flux<Product> getProducts();

}
