import '../Sheets/Header.css'
import { useNavigate } from 'react-router-dom';


export default function Header() {
    const { navigate } = useNavigate();
    return(
        <div class="Header-header">
            <p onClick={ navigate('BuyPage') }>Buy</p>
            <p onClick={ navigate('SellPage') }>Sell</p>
        </div>
    );
}