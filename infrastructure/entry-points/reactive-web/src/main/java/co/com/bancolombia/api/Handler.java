package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.ProductDTO;
import co.com.bancolombia.api.helper.ProductHelper;
import co.com.bancolombia.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

 private  final ProductUseCase productUseCase;
    public Mono<ServerResponse> getProducts(ServerRequest serverRequest) {

        return ServerResponse.ok().body(ProductHelper.getFluxProductDTO(productUseCase.getProduct()),ProductDTO.class);
    }


}
