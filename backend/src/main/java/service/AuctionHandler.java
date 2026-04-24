package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import model.Database;

@Component
public class AuctionHandler {

    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    public void closeExpiredAuctions() {
        Database.setExpiredItemsSold();
    }
}
