package service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import model.Database;

@Component
public class AuctionHandler {
    
    @Scheduled(cron = "0 0 0 * * *",zone = "UTC")
    public void closeExpiredAuctions() {
        Database.setExpiredItemsSold();
    }
}
