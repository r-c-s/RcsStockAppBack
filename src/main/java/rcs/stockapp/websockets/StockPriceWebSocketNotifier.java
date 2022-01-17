package rcs.stockapp.websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rcs.stockapp.models.StockPrice;
import rcs.stockapp.repositories.UserStocksRepository;
import rcs.stockapp.services.FinnhubService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@EnableScheduling
@Component
public class StockPriceWebSocketNotifier {

    private final Logger logger = LoggerFactory.getLogger(StockPriceWebSocketNotifier.class);

    private final Map<String, StockPrice> lastStockPrice = new HashMap<>();

    private final UserStocksRepository userStocksRepository;
    private final WebSocketSessionRegistry webSocketSessionRegistry;
    private final FinnhubService finnhubService;
    private final SimpMessagingTemplate template;

    public StockPriceWebSocketNotifier(
            UserStocksRepository userStocksRepository,
            WebSocketSessionRegistry webSocketSessionRegistry,
            FinnhubService finnhubService,
            SimpMessagingTemplate template) {
        this.webSocketSessionRegistry = webSocketSessionRegistry;
        this.userStocksRepository = userStocksRepository;
        this.finnhubService = finnhubService;
        this.template = template;
    }

    /**
     * TODO: consider optimizing this to not check for price updates outside trading hours
     */
    @Scheduled(fixedRateString = "${web-socket.stock-price-notify-rate}")
    public void notifyStockPriceChanges() {
        logger.info("Notifying websocket subscribers");

        Collection<String> stocksWithSubscribers = webSocketSessionRegistry.getStocksWithSubscribers();

        userStocksRepository.getStocksWithFollowers()
                .keySet()
                .stream()
                .filter(stock -> {
                    boolean hasListener = stocksWithSubscribers.contains(stock);
                    if (hasListener) {
                        logger.info("There are subscribers of " + stock + "; fetching price");
                        return true;
                    } else {
                        logger.info("There are no subscribers of " + stock + "; not fetching price");
                        return false;
                    }
                })
                .map(finnhubService::getPrice)
                .filter(stockPrice -> {
                    boolean isEqualToLastPrice = stockPrice.equals(lastStockPrice.get(stockPrice.symbol()));
                    if (isEqualToLastPrice) {
                        logger.info("Price has not changed; not notifying subscribers of " + stockPrice.symbol());
                        return false;
                    } else {
                        logger.info("Price has changed; notifying subscribers of" + stockPrice.symbol());
                        return true;
                    }
                })
                .peek(stockPrice -> lastStockPrice.put(stockPrice.symbol(), stockPrice))
                .forEach(stockPrice -> template.convertAndSend("/topic/stocks/" + stockPrice.symbol(), stockPrice));
    }
}
