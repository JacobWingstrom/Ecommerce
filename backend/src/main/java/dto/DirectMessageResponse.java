package dto;

import java.time.LocalDateTime;
import java.util.List;
import model.MessageDTO;

public class DirectMessageResponse {

	private int conversationId;
	private String otherUsername;
	private int itemId;
	private String itemTitle;
	private List<MessageDTO> messages;
	private String lastMessage;
	private LocalDateTime lastMessageTime;
	private boolean success;
	private String message;

	public DirectMessageResponse() {
	}

	// For conversation list (all conversations for a user)
	public DirectMessageResponse(int conversationId, String otherUsername, int itemId, String itemTitle, String lastMessage, LocalDateTime lastMessageTime) {
		this.conversationId = conversationId;
		this.otherUsername = otherUsername;
		this.itemId = itemId;
		this.itemTitle = itemTitle;
		this.lastMessage = lastMessage;
		this.lastMessageTime = lastMessageTime;
	}

	// For getting messages in a conversation
	public DirectMessageResponse(int conversationId, String otherUsername, String itemTitle, List<MessageDTO> messages) {
		this.conversationId = conversationId;
		this.otherUsername = otherUsername;
		this.itemTitle = itemTitle;
		this.messages = messages;
		this.success = true;
	}

	// For operation results (send, create)
	public DirectMessageResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public int getConversationId() {
		return conversationId;
	}

	public void setConversationId(int conversationId) {
		this.conversationId = conversationId;
	}

	public String getOtherUsername() {
		return otherUsername;
	}

	public void setOtherUsername(String otherUsername) {
		this.otherUsername = otherUsername;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public List<MessageDTO> getMessages() {
		return messages;
	}

	public void setMessages(List<MessageDTO> messages) {
		this.messages = messages;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public LocalDateTime getLastMessageTime() {
		return lastMessageTime;
	}

	public void setLastMessageTime(LocalDateTime lastMessageTime) {
		this.lastMessageTime = lastMessageTime;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
