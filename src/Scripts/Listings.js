export default function Listings({ data }){
    return (
        <div>
            {data && data.map(item => (
                <div key={item.itemId}>
                    <h4>{item.username}</h4>
                    <p>{item.description}</p>
                    <p>{item.highestBid}</p>
                    <p>{item.end_time}</p>
                    <img src={`data:image/jpeg;base64,${item.image}`} alt={item.description} />
                </div>
            ))}
        </div>
    )
}