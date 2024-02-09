package co.com.bancolombia.consumer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "adapter.rest-consumer.uri")
public class AdapterProperties {
        private String getBooks;
        private Configuration configuration;

        @Getter
        @Setter
        public static class Configuration{
                private Long timeout;

        }

}
