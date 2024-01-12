package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.helper.ProductHelper;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class RestConsumer implements ProductGateway {
    private final WebClient client;

    @Override
    public Flux<Product> getProducts() {
        return client
                .get()
                .uri("/products")
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .map(ProductHelper::getProduct);
    }


}
