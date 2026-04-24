import { useNavigate } from 'react-router-dom';
import '../Sheets/Listings.css'

export function timeLeft(endTime) {
    const diff = new Date(endTime) - new Date();
    if (diff <= 0) return 'Ended';

    const days    = Math.floor(diff / (1000 * 60 * 60 * 24));
    const hours   = Math.floor((diff / (1000 * 60 * 60)) % 24);
    const minutes = Math.floor((diff / (1000 * 60)) % 60);

    if (days > 0)   return `${days} days`;
    if (hours > 0)  return `${hours} hours`;
    return `${minutes} minutes`;
}


export default function Listings({ data }){
    const navigate = useNavigate();

    return (
        <div>
            {data && data.map(item => (
                <div className="Listings-content" key={item.itemId} onClick={ () => navigate(`/ListingPage/${item.itemId}`)}>
                    <img className="Listings-content-image" src={`data:image/jpeg;base64,${item.image}`} alt="Item"/>
                    <h4 className="Listings-content-title">{item.username}</h4>
                    <p className="Listings-content-price">Current Price: {item.highestBid}</p>
                    <p className="Listings-content-location">{item.location}</p>
                    <p className="Listings-content-time">Time Left: {timeLeft(item.endTime)}</p>
                </div>
            ))}
        </div>
    )
}