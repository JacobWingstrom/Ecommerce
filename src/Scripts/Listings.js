import { useNavigate } from 'react-router-dom';

export default function Listings({ data }){
    const navigate = useNavigate();

    return (
        <div>
            {data && data.map(item => (
                <div key={item.itemId} onClick={ () => navigate(`/ListingPage/${item.itemId}`)}>
                    <h1>{item.itemId}</h1>
                    <h4>{item.username}</h4>
                    <p>{item.description}</p>
                    <p>{item.highestBid}</p>
                    <p>{item.end_time}</p>
                    <img src={`data:image/jpeg;base64,${item.image}`} alt="Item"/>
                </div>
            ))}
        </div>
    )
}