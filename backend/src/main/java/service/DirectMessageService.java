package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dto.DirectMessageResponse;
import model.Database;
import model.MessageDTO;

public class DirectMessageService {

	public static List<DirectMessageResponse> getAllConversationsForUser(int userId) throws SQLException {
		List<DirectMessageResponse> conversations = new ArrayList<>();

		try (Connection con = Database.getConnection()) {
			if (con == null)
				return conversations;

			String query = "SELECT cp.conversation_id, u.username, c.item_id, i.name AS item_title, " +
					"latest_msg.content AS last_message, latest_msg.timestamp AS last_message_time " +
					"FROM conversation_participants cp " +
					"JOIN conversation_participants cp2 ON cp.conversation_id = cp2.conversation_id AND cp.user_id != cp2.user_id " +
					"JOIN users u ON cp2.user_id = u.user_id " +
					"LEFT JOIN conversations c ON cp.conversation_id = c.conversation_id " +
					"LEFT JOIN items i ON c.item_id = i.item_id " +
					"LEFT JOIN (" +
					"  SELECT m1.conversation_id, m1.content, m1.timestamp " +
					"  FROM messages m1 " +
					"  INNER JOIN (SELECT conversation_id, MAX(timestamp) AS max_ts FROM messages GROUP BY conversation_id) m2 " +
					"  ON m1.conversation_id = m2.conversation_id AND m1.timestamp = m2.max_ts" +
					") latest_msg ON cp.conversation_id = latest_msg.conversation_id " +
					"WHERE cp.user_id = ? " +
					"ORDER BY latest_msg.timestamp DESC, cp.conversation_id DESC";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				LocalDateTime lastMessageTime = null;
				if (rs.getTimestamp("last_message_time") != null)
					lastMessageTime = rs.getTimestamp("last_message_time").toLocalDateTime();

				conversations.add(new DirectMessageResponse(
					rs.getInt("conversation_id"), rs.getString("username"),
					rs.getInt("item_id"), rs.getString("item_title"),
					rs.getString("last_message"), lastMessageTime
				));
			}

			Database.releaseConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conversations;
	}

	public static DirectMessageResponse getConversationMessages(int conversationId, int userId) throws SQLException {
		try (Connection con = Database.getConnection()) {
			if (con == null)
				return new DirectMessageResponse(false, "Database connection failed");

			String verifyQuery = "SELECT COUNT(*) FROM conversation_participants WHERE conversation_id = ? AND user_id = ?";
			PreparedStatement verifyStmt = con.prepareStatement(verifyQuery);
			verifyStmt.setInt(1, conversationId);
			verifyStmt.setInt(2, userId);
			ResultSet verifyRs = verifyStmt.executeQuery();
			verifyRs.next();
			if (verifyRs.getInt(1) == 0) {
				Database.releaseConnection(con);
				return new DirectMessageResponse(false, "User is not a participant in this conversation");
			}

			String otherUserQuery = "SELECT u.username, i.name AS item_title " +
					"FROM conversation_participants cp " +
					"JOIN users u ON cp.user_id = u.user_id " +
					"LEFT JOIN conversations c ON cp.conversation_id = c.conversation_id " +
					"LEFT JOIN items i ON c.item_id = i.item_id " +
					"WHERE cp.conversation_id = ? AND cp.user_id != ?";
			PreparedStatement otherStmt = con.prepareStatement(otherUserQuery);
			otherStmt.setInt(1, conversationId);
			otherStmt.setInt(2, userId);
			ResultSet otherRs = otherStmt.executeQuery();

			String otherUsername = "Unknown";
			String itemTitle = null;
			if (otherRs.next()) {
				otherUsername = otherRs.getString("username");
				itemTitle = otherRs.getString("item_title");
			}

			Database.releaseConnection(con);
			model.Conversation conversation = Database.getConversation(conversationId);

			if (conversation == null)
				return new DirectMessageResponse(false, "Conversation not found");

			List<MessageDTO> messages = new ArrayList<>();
			for (MessageDTO msg : conversation.getMessages())
				messages.add(msg);

			return new DirectMessageResponse(conversationId, otherUsername, itemTitle, messages);
		} catch (SQLException e) {
			e.printStackTrace();
			return new DirectMessageResponse(false, "Error retrieving conversation");
		}
	}

	public static DirectMessageResponse sendMessage(int conversationId, int senderId, String content) throws SQLException {
		try {
			if (content == null || content.trim().isEmpty())
				return new DirectMessageResponse(false, "Message content cannot be empty");

			Database.addMessage(new MessageDTO(conversationId, senderId, content, LocalDateTime.now()));
			return new DirectMessageResponse(true, "Message sent successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return new DirectMessageResponse(false, "Error sending message");
		}
	}

	public static DirectMessageResponse createConversationAndSendMessage(int userId, int recipientId, String content)
			throws SQLException {
		try {
			if (userId == recipientId)
				return new DirectMessageResponse(false, "Cannot start conversation with yourself");

			if (content == null || content.trim().isEmpty())
				return new DirectMessageResponse(false, "Message content cannot be empty");

			try (Connection con = Database.getConnection()) {
				if (con == null)
					return new DirectMessageResponse(false, "Database connection failed");

				String checkQuery = "SELECT c.conversation_id FROM conversations c " +
						"JOIN conversation_participants cp1 ON c.conversation_id = cp1.conversation_id " +
						"JOIN conversation_participants cp2 ON c.conversation_id = cp2.conversation_id " +
						"WHERE cp1.user_id = ? AND cp2.user_id = ?";
				PreparedStatement checkStmt = con.prepareStatement(checkQuery);
				checkStmt.setInt(1, userId);
				checkStmt.setInt(2, recipientId);
				ResultSet checkRs = checkStmt.executeQuery();

				int conversationId;
				if (checkRs.next()) {
					conversationId = checkRs.getInt("conversation_id");
				} else {
					PreparedStatement createStmt = con.prepareStatement(
						"INSERT INTO conversations () VALUES ()", Statement.RETURN_GENERATED_KEYS);
					createStmt.executeUpdate();
					ResultSet generatedKeys = createStmt.getGeneratedKeys();

					if (!generatedKeys.next()) {
						Database.releaseConnection(con);
						return new DirectMessageResponse(false, "Failed to create conversation");
					}
					conversationId = generatedKeys.getInt(1);

					PreparedStatement addStmt = con.prepareStatement(
						"INSERT INTO conversation_participants (conversation_id, user_id) VALUES (?, ?)");
					addStmt.setInt(1, conversationId);
					addStmt.setInt(2, userId);
					addStmt.executeUpdate();
					addStmt.setInt(2, recipientId);
					addStmt.executeUpdate();
				}

				Database.releaseConnection(con);
				Database.addMessage(new MessageDTO(conversationId, userId, content, LocalDateTime.now()));
				return new DirectMessageResponse(true, "Conversation created and message sent");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new DirectMessageResponse(false, "Error creating conversation");
		}
	}

	public static DirectMessageResponse startItemConversation(int buyerId, int itemId, String content) {
		try {
			if (content == null || content.trim().isEmpty())
				return new DirectMessageResponse(false, "Message content cannot be empty");

			try (Connection con = Database.getConnection()) {
				if (con == null)
					return new DirectMessageResponse(false, "Database connection failed");

				PreparedStatement sellerStmt = con.prepareStatement(
					"SELECT seller_id FROM items WHERE item_id = ?");
				sellerStmt.setInt(1, itemId);
				ResultSet sellerRs = sellerStmt.executeQuery();
				if (!sellerRs.next())
					return new DirectMessageResponse(false, "Item not found");

				int sellerId = sellerRs.getInt("seller_id");
				if (buyerId == sellerId)
					return new DirectMessageResponse(false, "Cannot message yourself");

				PreparedStatement checkStmt = con.prepareStatement(
					"SELECT c.conversation_id FROM conversations c " +
					"JOIN conversation_participants cp1 ON c.conversation_id = cp1.conversation_id " +
					"JOIN conversation_participants cp2 ON c.conversation_id = cp2.conversation_id " +
					"WHERE c.item_id = ? AND cp1.user_id = ? AND cp2.user_id = ?");
				checkStmt.setInt(1, itemId);
				checkStmt.setInt(2, buyerId);
				checkStmt.setInt(3, sellerId);
				ResultSet checkRs = checkStmt.executeQuery();

				int conversationId;
				if (checkRs.next()) {
					conversationId = checkRs.getInt("conversation_id");
				} else {
					conversationId = Database.createConversationForItem(sellerId, buyerId, itemId);
					if (conversationId == -1)
						return new DirectMessageResponse(false, "Failed to create conversation");
				}

				Database.releaseConnection(con);
				Database.addMessage(new MessageDTO(conversationId, buyerId, content, LocalDateTime.now()));

				DirectMessageResponse response = new DirectMessageResponse(true, "Message sent");
				response.setConversationId(conversationId);
				return response;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new DirectMessageResponse(false, "Error creating conversation");
		}
	}

	public static int getExistingConversation(int userId1, int userId2) throws SQLException {
		try (Connection con = Database.getConnection()) {
			if (con == null)
				return -1;

			String query = "SELECT c.conversation_id FROM conversations c " +
					"JOIN conversation_participants cp1 ON c.conversation_id = cp1.conversation_id " +
					"JOIN conversation_participants cp2 ON c.conversation_id = cp2.conversation_id " +
					"WHERE cp1.user_id = ? AND cp2.user_id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, userId1);
			stmt.setInt(2, userId2);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int conversationId = rs.getInt("conversation_id");
				Database.releaseConnection(con);
				return conversationId;
			}

			Database.releaseConnection(con);
			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
