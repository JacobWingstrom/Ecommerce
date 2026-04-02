package dto;

public class UserListingRequest {
    private String username;
    private int pageNum;

    public UserListingRequest(){}

    public String getUsername(){
        return username;
    }

    public int getPageNum() {  
        return pageNum;
    }
}
