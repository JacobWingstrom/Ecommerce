import '../Sheets/MainPage.css'
import Header from './Header.js'
import { useNavigate } from 'react-router-dom';

function MainBody() {
    const navigate = useNavigate();
    return (
        <div id="MainBody">
            <div id="MainBody-card">
                <h2>What would you like to do?</h2>
                <button className="MainBody-Body" onClick={() => navigate('/BuyPage')}>Buy</button>
                <button className="MainBody-Body" onClick={() => navigate('/SellPage')}>Sell</button>
            </div>
        </div>
    );
}

export default function MainPage() {
    return(
        <div id="MainPage">
            <Header />
            <MainBody />
        </div>
    );
}