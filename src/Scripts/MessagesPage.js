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
    const [messages, setMessages] = useState([]);
    const { token } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        fetch('/api/messages/allMessages', {
            method: 'GET',
            headers: { 'Authorization': `Bearer ${token}` }
        })
            .then(data => data.json())
            .then(setMessages)
            .catch(console.error);
    }, [token]);

    return (
        <div className="MessagesPage-content">
            {messages.length === 0 && (
                <p className="MessagesPage-empty">No conversations yet</p>
            )}
            {messages.map(message => (
                <div
                    key={message.conversationId}
                    className="MessagesPage-message"
                    onClick={() => navigate(`/DirectMessagePage/${message.conversationId}`)}
                >
                    <div className="MessagesPage-message-top">
                        <span className="MessagesPage-user">{message.otherUsername}</span>
                        <span className="MessagesPage-item">{message.itemName}</span>
                    </div>
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