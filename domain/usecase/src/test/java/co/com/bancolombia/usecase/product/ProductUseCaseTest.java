package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.ProductGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static co.com.bancolombia.usecase.product.DataProvider.getFluxProduct;
import static org.junit.jupiter.api.Assertions.*;
//@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @InjectMocks
    private ProductUseCase productUseCase;
    @Mock
    private ProductGateway productGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldReturnProductsTest(){
        Mockito.when(productGateway.getProducts())
                .thenReturn(getFluxProduct());

        StepVerifier.create(productUseCase.getProduct())
                .expectNextMatches(product -> product.getId()==1 && product.getTitle().equals("tv"))
                .expectNextMatches(product -> product.getId()==2 && product.getTitle().equals("pc"))
                .verifyComplete();

    }


}