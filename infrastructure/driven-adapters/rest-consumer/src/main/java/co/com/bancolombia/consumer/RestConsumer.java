package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.config.AdapterProperties;
import co.com.bancolombia.consumer.helper.ProductHelper;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties( AdapterProperties.class)
public class RestConsumer implements ProductGateway {
    private final WebClient client;
    private final AdapterProperties adapterProperties;

    @Override
    public Flux<Product> getProducts() {
        return client
                .get()
                .uri(adapterProperties.getGetBooks())
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .map(ProductHelper::getProduct);
    }


}
