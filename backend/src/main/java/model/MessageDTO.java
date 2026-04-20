package model;
import java.time.LocalDateTime;
public class MessageDTO {
    int sender_id;
    String content;
    LocalDateTime timestamp;
    public MessageDTO(int sender_id, String content, LocalDateTime timestamp){
        this.sender_id = sender_id;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getSender_id(){
        return this.sender_id;
    }

    public String getContent(){
        return this.content;
    }

    public LocalDateTime getTimestamp(){
        return this.timestamp;
    }
}
