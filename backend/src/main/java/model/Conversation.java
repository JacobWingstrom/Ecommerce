package model;
import java.util.List;
public class Conversation {
    int conversation_id;
    List<MessageDTO> messages;

    public Conversation(int conversation_id){
        this.conversation_id = conversation_id;
        this.messages = new java.util.ArrayList<>();
    }

    public Conversation(int conversation_id, List<MessageDTO> messages){
        this.conversation_id = conversation_id;
        this.messages = messages;
    }
    public int getConversation_id(){
        return this.conversation_id;
    }
    public MessageDTO[] getMessages(){
        return this.messages.toArray(new MessageDTO[0]);
    }

    public void addMessage(MessageDTO message){
        this.messages.add(message);
    }


}
