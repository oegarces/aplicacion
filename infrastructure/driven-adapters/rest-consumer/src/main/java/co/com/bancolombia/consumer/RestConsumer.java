package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.config.AdapterProperties;
import co.com.bancolombia.consumer.exception.ExceptionRestConsumer;
import co.com.bancolombia.consumer.helper.ProductHelper;
import co.com.bancolombia.exception.TechnicalException;
import co.com.bancolombia.exception.message.TechnicalErrorMessage;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties( AdapterProperties.class)
public class RestConsumer implements ProductGateway {
    private final WebClient client;
    private final AdapterProperties adapterProperties;
    private final ExceptionRestConsumer exceptionRestConsumer;

    @Override
    public Flux<Product> getProducts() {
        return client
                .get()
                .uri(adapterProperties.getGetBooks())
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .map(ProductHelper::getProduct)
                .timeout(Duration.ofMillis(5000))
                .onErrorMap(TimeoutException.class, ex-> new TechnicalException((TechnicalErrorMessage.TECHNICAL_ERROR_TIMEOUT)))
                .onErrorMap(WebClientRequestException.class, ex-> new TechnicalException((TechnicalErrorMessage.TECHNICAL_ERROR_UNKNOWN)))
                .onErrorMap(WebClientResponseException.class, exceptionRestConsumer::getError);
               // .onErrorResume(WebClientResponseException);

    }


}
