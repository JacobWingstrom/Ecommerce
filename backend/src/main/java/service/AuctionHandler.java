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

    @Scheduled(cron = "18 5 6 * * *", zone = "MST")
    public static void closeExpiredAuctions() {
        try (Connection con = Database.getConnection()) {
            if (con == null) return;

            String query = "SELECT item_id, seller_id, highest_bidder_id FROM items " +
                           "WHERE end_time <= NOW() AND sold = FALSE AND highest_bidder_id IS NOT NULL";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
                Database.createConversationForItem(rs.getInt("seller_id"), rs.getInt("highest_bidder_id"), rs.getInt("item_id"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Database.setExpiredItemsSold();
    }
}
