import { useEffect, useState } from "react";
import Header from "./Header"
import { useParams } from "react-router-dom"
import '../Sheets/ListingPage.css'
import { useAuth } from '../Context/AuthContext.js'
import { timeLeft } from './Listings.js'

async function listItem(data, token) {
    const response = await fetch('http://localhost:8080/api/buy/placeBid', {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json'},
        body: JSON.stringify(data)  
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
            const data = {
                itemId: itemId,
                bid: bid
            }

            const x = await listItem(data, token);
            console.log(x.item.itemId + " " + x.item.highestBid)
            window.location.reload()
        } catch {
            console.log("asdfsd")
            setError('Invalid Bid');
        }
    }
    return (
        <form className="ListingPage-form" onSubmit={handleSubmit}>
            {error && <p id="ListingPage-error" style={{color: 'red'}}>{error}</p>}
            <label>
                <p className="ListingPage-Label">Bid:</p>
                <input className="ListingPage-Input" type='number' name='bid' placeholder={"Minimum of " + (currentBid + 1)} onChange={ e => setBid(e.target.value) }/>
            </label>
            <input id="ListingPage-Button" type="submit" value="Submit Bid" disabled={!bid || bid <= currentBid} />
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
            {data && (data.sold ? <p className="ListingPage-ended">This auction has ended</p> :
                <div id="ListingPage-Body-Data">
                    <h1 id='ListingPage-Body-Title'>{data.username}</h1>
                    <img id='ListingPage-Body-Image' src={`data:image/jpeg;base64,${data.image}`} alt="Item"/>
                    <p id='ListingPage-Body-Description'>Description: {data.description}</p>
                    <p id='ListingPage-Body-Time'>Ends In: {timeLeft(data.endTime)}</p>
                    <p id='ListingPage-Body-Price'>Current Highest Bid: ${data.highestBid}</p>
                    <ListingPageForm currentBid={ data.highestBid } itemId={ params.item } />
                </div>)
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