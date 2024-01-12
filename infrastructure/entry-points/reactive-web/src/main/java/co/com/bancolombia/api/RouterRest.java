package co.com.bancolombia.api;

import co.com.bancolombia.api.config.RouteProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({RouteProperties.class})
public class RouterRest {

    private final RouteProperties routeProperties;
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET(routeProperties.getProductsRoute()), handler::getProducts);
               // .andRoute(POST("/api/usecase/otherpath"), handler::listenPOSTUseCase)
                //.and(route(GET("/api/otherusercase/path"), handler::listenGETOtherUseCase));
    }
}
