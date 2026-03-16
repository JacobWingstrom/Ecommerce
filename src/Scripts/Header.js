import '../Sheets/Header.css'
import { useNavigate } from 'react-router-dom';
import pineappleLogo from '../Images/pineappleLogoPlaceholder.jpg';

export default function Header() {
    const navigate = useNavigate();
    return(
        <div className="Header-header">
            <img src={pineappleLogo} alt="Pineapple logo" className='image' />
            <button id="button1" onClick={ () => navigate('/BuyPage') }>Buy</button>
            <button id="button2" onClick={ () => navigate('/SellPage') }>Sell</button>
            <button id="button3" onClick={ () => navigate('/AccountPage') }>Account</button>
        </div>
    );
}