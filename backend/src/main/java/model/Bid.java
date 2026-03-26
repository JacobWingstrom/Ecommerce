package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bid {
    private int bidId, bidderId, itemId;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    public Bid(int bidderId, int itemId, BigDecimal amount, LocalDateTime timestamp){
        this.bidId = -1;
        this.bidderId = bidderId;
        this.itemId = itemId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Bid(int bidId, int bidderId, int itemId, BigDecimal amount, LocalDateTime timestamp){
        this.bidId = bidId;
        this.bidderId = bidderId;
        this.itemId = itemId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public int getBidId(){
        return this.bidId;
    }
    public int getBidderId(){
        return this.bidderId;
    }
    public int getItemId(){
        return this.itemId;
    }
    public BigDecimal getAmount(){
        return this.amount;
    }
    public LocalDateTime getTimestamp(){
        return this.timestamp;
    }
    
}
