import { useState, useEffect, useRef, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../Context/AuthContext';
import Header from './Header';
import '../Sheets/DirectMessagePage.css';

const SLOTS = 24;
const DAYS = 7;
const POLL_MS = 3000;

function slotLabel(index) {
    const hour = 7 + Math.floor(index / 2);
    const min = index % 2 === 0 ? '00' : '30';
    const ampm = hour < 12 ? 'am' : 'pm';
    const display = hour > 12 ? hour - 12 : hour;
    return `${display}:${min}${ampm}`;
}

function dateLabel(startDate, dayIndex) {
    const d = new Date(startDate);
    d.setDate(d.getDate() + dayIndex);
    return d.toLocaleDateString('en-US', { weekday: 'short', month: 'numeric', day: 'numeric' });
}

function emptyGrid() {
    const grid = [];
    for (let d = 0; d < DAYS; d++) {
        const row = [];
        for (let s = 0; s < SLOTS; s++) {
            row.push(false);
        }
        grid.push(row);
    }
    return grid;
}


function DirectMessagePageHeader({ conversation }) {
    return (
        <div id="DirectMessagePage-Header">
            <h1>{conversation?.otherUsername ?? '...'}</h1>
            <span>{conversation?.itemTitle ?? ''}</span>
        </div>
    );
}

function MessagesPanel() {
    const { conversationId } = useParams();
    const { token, user } = useAuth();

    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');
    const messagesEndRef = useRef(null);

    const fetchMessages = useCallback(() => {
        fetch(`/api/directMessage/messages/${conversationId}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        })
            .then(res => res.json())
            .then(data => {
                setMessages(data.messages ?? []);
                messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
            })
            .catch(console.error);
    }, [token, conversationId]);

    useEffect(() => {
        fetchMessages();
        const id = setInterval(fetchMessages, POLL_MS);
        return () => clearInterval(id);
    }, [fetchMessages]);

    function sendMessage(e) {
        e.preventDefault();
        if (!input.trim()) return;
        fetch('/api/directMessage/send', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ conversationId, content: input.trim() })
        })
            .then(() => { setInput(''); fetchMessages(); })
            .catch(console.error);
    }

    return (
        <div className="DirectMessagePage-messages">
            <div className="DirectMessagePage-messages-list">
                {messages.map((m, i) => (
                    <div
                        key={i}
                        className={`DirectMessagePage-messages-bubble ${m.senderUsername === user ? 'bubble-this' : 'bubble-other'}`}
                    >
                        <p>{m.content}</p>
                    </div>
                ))}
                <div ref={messagesEndRef} />
            </div>
            <form className="DirectMessagePage-messages-form" onSubmit={sendMessage}>
                <input
                    className="DirectMessagePage-messages-input"
                    value={input}
                    onChange={e => setInput(e.target.value)}
                    placeholder="Type a message"
                />
                <button type="submit" className="DirectMessagePage-messages-button">Send</button>
            </form>
        </div>
    );
}

function AvailabilityPanel({ conversation }) {
    const { conversationId } = useParams();
    const { token } = useAuth();

    const [Grid, setGrid] = useState(emptyGrid());
    const [otherGrid, setotherGrid] = useState(emptyGrid());

    useEffect(() => {
        if (!conversation?.userId || !conversation?.otherUserId) {
            return;
        }

        fetch('/api/availibilty/getAvailibility', {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId: String(conversation.userId) })
        }).then(res => res.json()).then(data => {
            const grid = emptyGrid();
            (data.availability ?? []).forEach((row, d) => { if (d < DAYS) grid[d] = row.slice(0, SLOTS); });
            setGrid(grid);
        }).catch(console.error);

        fetch('/api/availibilty/getAvailibility', {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId: String(conversation.otherUserId) })
        }).then(res => res.json()).then(data => {
            const grid = emptyGrid();
            (data.availability ?? []).forEach((row, d) => { if (d < DAYS) grid[d] = row.slice(0, SLOTS); });
            setotherGrid(grid);
        }).catch(console.error);
    }, [token, conversationId, conversation]);

    function handleCellClick(day, slot) {
        const next = Grid.map(r => [...r]);
        next[day][slot] = !next[day][slot];
        setGrid(next);
        fetch('/api/availibilty/updateAvailibilty', {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
            body: JSON.stringify({ token, availibilty: next })
        }).then(res => { if (!res.ok) res.text().then(t => console.error('save failed', res.status, t)); })
          .catch(console.error);
    }

    function cellClassName(day, slot) {
        const grid = Grid[day]?.[slot];
        const other = otherGrid[day]?.[slot];
        if (grid && other) return 'DirectMessagePage-availiblity-cell cell-overlap';
        if (grid) return 'DirectMessagePage-availiblity-cell cell-this';
        if (other) return 'DirectMessagePage-availiblity-cell cell-other';
        return 'cell';
    }

    const startDate = new Date();
    const tableHeads = [];
    for (let d = 0; d < DAYS; d++) {
        tableHeads.push(
            <th key={d} className="DirectMessagePage-availiblity-grid-col-header">
                {dateLabel(startDate, d)}
            </th>
        );
    }

    const tableRows = [];
    for (let s = 0; s < SLOTS; s++) {
        const tableCells = [];
        for (let d = 0; d < DAYS; d++) {
            tableCells.push(
                <td
                    key={d}
                    className={cellClassName(d, s)}
                    onClick={() => handleCellClick(d, s)}
                />
            );
        }
        tableRows.push(
            <tr key={s}>
                <td className="DirectMessagePage-availiblity-grid-row-label">{slotLabel(s)}</td>
                {tableCells}
            </tr>
        );
    }

    return (
        <div className="DirectMessagePage-availiblity">
            <div className="DirectMessagePage-availiblity-title">
                <h3>Availability</h3>
            </div>
            <div className="DirectMessagePage-availiblity-legend">
                <span className="DirectMessagePage-availiblity-this">Your availability</span>
                <span className="DirectMessagePage-availiblity-other">Their availability</span>
                <span className="DirectMessagePage-availiblity-overlap">Both available</span>
            </div>
            <div className="DirectMessagePage-availiblity-grid-wrapper">
                <table className="DirectMessagePage-availiblity-grid">
                    <thead>
                        <tr>
                            <th className="DirectMessagePage-availiblity-grid-corner" />
                            {tableHeads}
                        </tr>
                    </thead>
                    <tbody>
                        {tableRows}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

function DirectMessagePageBody({ conversation }) {
    return (
        <div className="DirectMessagePage-Body">
            <MessagesPanel />
            <AvailabilityPanel conversation={conversation} />
        </div>
    );
}

export default function DirectMessagePage() {
    const { conversationId } = useParams();
    const { token } = useAuth();

    const [conversation, setConversation] = useState(null);

    useEffect(() => {
        fetch(`/api/directMessage/messages/${conversationId}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        })
            .then(data => data.json())
            .then(setConversation)
            .catch(console.error);
    }, [token, conversationId]);

    return (
        <div className="DirectMessagePage">
            <Header />
            <DirectMessagePageHeader conversation={conversation} />
            <DirectMessagePageBody conversation={conversation} />
        </div>
    );
}
