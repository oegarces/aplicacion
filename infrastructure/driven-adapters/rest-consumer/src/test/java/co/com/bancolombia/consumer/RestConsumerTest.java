package co.com.bancolombia.consumer;


import co.com.bancolombia.consumer.config.AdapterProperties;
import co.com.bancolombia.consumer.exception.ExceptionRestConsumer;
import co.com.bancolombia.exception.BusinessException;
import co.com.bancolombia.exception.TechnicalException;
import co.com.bancolombia.model.product.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static co.com.bancolombia.consumer.DataProvider.getAdapterProperties;
import static co.com.bancolombia.consumer.DataProvider.getAdapterPropertiesTiemout;
import static co.com.bancolombia.consumer.DataProvider.getProductsDTO;


class RestConsumerTest {

    public static MockWebServer mockWebServer;

    @Mock
    private AdapterProperties adapterProperties;

    @Mock
    private AdapterProperties adapterPropertiesTimeout;
    private RestConsumer restConsumer;

    private RestConsumer restConsumerTimeout;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        adapterProperties = getAdapterProperties();
        adapterPropertiesTimeout = getAdapterPropertiesTiemout();
        Map<String, BusinessException> businessExceptionMap = new HashMap<String, BusinessException>();
        Map<String, TechnicalException> technicalExceptionMap = new HashMap<String, TechnicalException>();
        ExceptionRestConsumer exceptionRestConsumer = new ExceptionRestConsumer(new ObjectMapper(), businessExceptionMap,technicalExceptionMap);
        restConsumer = new RestConsumer(WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build(), adapterProperties, exceptionRestConsumer);
        restConsumerTimeout = new RestConsumer(WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build(), adapterPropertiesTimeout, exceptionRestConsumer);

    }

    @Test
    void shouldReturnProductsFromApi() throws InterruptedException, JsonProcessingException {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader("Content-Type", "application/json ; charset=UTF-8")
                .setBody(new ObjectMapper().writeValueAsString(getProductsDTO()))
                .throttleBody(16, 5, TimeUnit.MILLISECONDS);

        mockWebServer.enqueue(mockResponse);

        Flux<Product> response = restConsumer.getProducts();

        StepVerifier.create(response)
                .expectNextMatches(product -> product.getId()==1)
                .expectNextMatches(product -> product.getId()==2)
                .verifyComplete();

    }

    @Test
    void shouldReturnTechnicalErrorTimeoutFromApi() throws InterruptedException, JsonProcessingException {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader("Content-Type", "application/json ; charset=UTF-8")
                .setBody(new ObjectMapper().writeValueAsString(getProductsDTO()))
                .throttleBody(16, 5, TimeUnit.MILLISECONDS);

        mockWebServer.enqueue(mockResponse);

        Flux<Product> response = restConsumerTimeout.getProducts();

        StepVerifier.create(response)
                .expectErrorMatches( e -> e instanceof TechnicalException &&
                        e.getMessage().equals("Error de timeout"))
                .verify();

    }

}