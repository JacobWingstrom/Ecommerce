import '../Sheets/AccountPage.css';
import Header from './Header.js'
import { useAuth } from '../Context/AuthContext.js'
import { useState, useEffect } from 'react'
import Listings from './Listings.js';

function Buttons({ setter }) {
    return ( 
        <div className="AccountPage-Buttons">
            <button onClick={ () => setter('Selling/Current') } className="AccountPage-Button" id="AccountPage-MyItems">My Items</button>
            <button onClick={ () => setter('Bids/Won') } className="AccountPage-Button" id="AccountPage-BidsWon">Bids Won</button>
            <button onClick={ () => setter('Bids/Current') } className="AccountPage-Button" id="AccountPage-BiddingOn">Bidding On</button>
        </div>
    )
}

function AccountHeader({ setter }) {
    const { user } = useAuth();
    
    return (
        <div id="AccountPage-Header">
            <h1>{user}</h1>
            <Buttons setter={setter}/>
        </div>
    )
}


function Content({ currentPage }) {
    const [ data, setData] = useState(null);
    const { token, user } = useAuth();
    
    async function handleFetch(currentPage, token, user) {
        const response = await fetch(`http://localhost:8080/api/user/${currentPage}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
        },
        body: JSON.stringify({ 
            username: user,
            pageNum: 1
        })
        });

        if (!response.ok) throw new Error("Fetch failed");
        return response.json();
    }

    useEffect( () => {
        if (!user) return;
        handleFetch(currentPage, token, user)
            .then(data => setData(data.items))
            .catch(err => console.log(err))
    }, [currentPage, token, user]);

    return (
        <div className="AccountPage-Content">
            <div className="AccountPage-ContentPage">
                {currentPage && <Listings data={ data }/>}
            </div>
        </div>
    )
}

function AccountBody({ currentPage }) {

    return (
        <div id="AccountPage-Body">
            <Content currentPage={ currentPage }/>
        </div>
    )
}

export default function AccountPage() {
    const [currentPage, setCurrentPage] = useState('Selling/Current');

    return (
        <div id="AccountPage">
            <Header />
            <AccountHeader setter={ setCurrentPage }/>
            <AccountBody currentPage={ currentPage }/>
        </div>
    )
}