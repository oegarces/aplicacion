package co.com.bancolombia.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "routes.path-mapping.api")
public class RouteProperties {

    private String productsRoute;
}
