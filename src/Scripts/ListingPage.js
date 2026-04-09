import { useEffect, useState } from "react";
import Header from "./Header"
import { useParams } from "react-router-dom"
import '../Sheets/ListingPage.css'
import { useAuth } from '../Context/AuthContext.js'

async function listItem(data, token) {
    const response = await fetch('http://localhost:8080/api/buy/placeBid', {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}` },
        body: data  
    });

    if (!response.ok) throw new Error("Listing Failed");
    return response.json();
}

function ListingPageForm({ currentBid, itemId }){
    const [bid, setBid] = useState(0)
    const [error, setError] = useState('')
    const { token } = useAuth();

    const handleSubmit = async e => {
        e.preventDefault()
        try{
            const data = new FormData();
            data.append('ItemId', itemId)
            data.append('Bid', bid);

            await listItem(data, token);

            window.location.reload()
        } catch {
            setError('Invalid Bid');
        }
    }
    return (
        <form onSubmit={handleSubmit}>
            <label>
                <p>Bid: </p>
                <input type='number' name='bid' placeholder={"Minimum of " + (currentBid+1)} onChange={ e => setBid(e.target.value) }/>
            </label>
            <button type="submit">Submit Bid</button>
        </form>
    )
}

function ListingPageBody(){
    const params = useParams();
    const [ data, setData ] = useState(null);

    async function handleFetch(item) {
        const response = await fetch(`http://localhost:8080/api/buy/bids/itemById`, {
            method: `POST`,
            headers: { 
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                itemId: item
            })
        })

        if (!response.ok) throw new Error("Fetch Failed");
        return response.json();
    }

    useEffect( () => {
        handleFetch(params.item)
        .then(data => setData(data.item))
        .catch(err => console.log(err))
    }, [params.item])

    return (
        <div className="ListingPage-Body">
            {data && 
                <div>
                    <h1 id='username'>{data.username}</h1>
                    <img src={`data:image/jpeg;base64,${data.image}`} alt="Item"/>
                    <p>{data.description}</p>
                    <p>{data.highestBid}</p>
                    <p>{data.endTime}</p>

                    <ListingPageForm currentBid={ data.highestBid } itemId={ params.item } />                 
                </div>
            }
        </div>
    )
}

export default function ListingPage() {
    return(
        <div className="ListingPage">
            <Header />
            <ListingPageBody />
        </div>
    )
}