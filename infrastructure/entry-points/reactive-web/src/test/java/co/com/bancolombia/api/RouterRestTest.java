package co.com.bancolombia.api;

import co.com.bancolombia.model.product.gateway.ProductGateway;
import co.com.bancolombia.usecase.product.ProductUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import static co.com.bancolombia.api.DataProvider.getFluxProduct;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ProductUseCase.class, Handler.class, RouterRest.class})
@WebFluxTest(properties = {"routes.path-mapping.api.products-route=/products"})
class RouterRestTest {

    @MockBean
    private ProductGateway productGateway;
    @Autowired
    private WebTestClient webTestClient;
    private static  final String ROUTE = "/products";

    @Test
    void shouldReturnProducts() {
        when(productGateway.getProducts()).thenReturn(getFluxProduct());
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(ROUTE).build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody();

    }
}
