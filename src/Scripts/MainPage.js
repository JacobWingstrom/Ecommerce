import '../Sheets/MainPage.css'
import Header from './Header.js'
import { useNavigate } from 'react-router-dom';

function MainBody() {
    const navigate = useNavigate();
    return (
    <div id="MainBody">
            <button class="MainBody-Body" id='MainBody-Buy' onClick={ () => navigate('/BuyPage')}>Buy</button>
            <button class="MainBody-Body" id='MainBody-Sell' onClick={ () => navigate('/SellPage')}>Sell</button>
    </div>
    );
}

export default function MainPage() {
    return(
        <>
            <Header />
            <MainBody />
        </>
    );
}