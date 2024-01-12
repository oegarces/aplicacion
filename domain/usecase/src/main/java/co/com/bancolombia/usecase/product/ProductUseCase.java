package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductGateway productGateway;
    public Flux<Product> getProduct(){
        return productGateway.getProducts();
    }
/*
    public Flux<Product> getProduct(){
        return Flux.zip(productGateway.getProducts(),productGateway.getProducts())
                .flatMap(tuple-> Flux.merge(Flux.just(Flux.just(tuple.getT1()), Flux.just(tuple.getT2()))))
                .distinct();
        //  .take(2);
        //.filter(product -> product.getPrice()>100);
    }

 */
}
