package dto;

public class DirectMessageRequest {

	private int conversationId;
	private int recipientId;
	private int itemId;
	private String content;

	public DirectMessageRequest() {
	}

	public DirectMessageRequest(int conversationId, String content) {
		this.conversationId = conversationId;
		this.content = content;
	}

	public int getConversationId() {
		return conversationId;
	}

	public void setConversationId(int conversationId) {
		this.conversationId = conversationId;
	}

	public int getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(int recipientId) {
		this.recipientId = recipientId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
