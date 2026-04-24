import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../Context/AuthContext';
import Header from './Header';
import '../Sheets/MessagesPage.css';

function MessagesPageHeader() {
    return (
        <div id="MessagesPage-Header">
            <h2>Messages</h2>
        </div>
    )
}

function MessagesPageBody() {
    const [conversations, setConversations] = useState([]);
    const { token } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        fetch('/api/directMessage/conversations', {
            headers: { 'Authorization': `Bearer ${token}` }
        })
            .then(res => res.json())
            .then(setConversations)
            .catch(console.error);
    }, [token]);

    return (
        <div className="MessagesPage-content">
            {conversations.length === 0 && (
                <p className="MessagesPage-empty">No conversations yet</p>
            )}
            {conversations.map(convo => (
                <div key={convo.conversationId} className="MessagesPage-card">
                    <div className="MessagesPage-card-info">
                        <span className="MessagesPage-card-user">{convo.otherUsername}</span>
                        {convo.itemTitle && (
                            <span className="MessagesPage-card-item">{convo.itemTitle}</span>
                        )}
                        <span className="MessagesPage-card-preview">
                            {convo.lastMessage ?? 'No messages yet'}
                        </span>
                    </div>
                    <button
                        className="MessagesPage-card-btn"
                        onClick={() => navigate(`/DirectMessagePage/${convo.conversationId}`)}
                    >
                        Open
                    </button>
                </div>
            ))}
        </div>
    )
}

export default function MessagesPage() {
    return (
        <div id="MessagesPage">
            <Header />
            <MessagesPageHeader />
            <MessagesPageBody />
        </div>
    )
}
