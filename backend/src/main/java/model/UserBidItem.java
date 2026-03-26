package model;

public class UserBidItem {
    private final Item item;
    private final Bid bid;
    public UserBidItem(Item item, Bid userBid){
        this.item = item;
        this.bid = userBid;
    }
    public Item getItem(){
        return item;
    }
    public Bid getUserBid(){
        return bid;
    }
}
