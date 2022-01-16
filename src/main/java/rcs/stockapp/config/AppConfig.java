package rcs.stockapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import rcs.stockapp.websockets.WebSocketSessionRegistry;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebSocketSessionRegistry getStockPriceRegistry() {
        return new WebSocketSessionRegistry();
    }
}
