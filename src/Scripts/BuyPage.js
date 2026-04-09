import '../Sheets/BuyPage.css'
import Header from "./Header.js"
import { useState, useEffect } from "react";
import { useAuth } from '../Context/AuthContext.js'
import Listings from './Listings.js';

function BuyPageHeader() {
    return (
        <div id="BuyPage-Header">
            <h1>All Listings</h1>
        </div>
    );
}

function Content() {
    const [ data, setData] = useState(null);
    const { token, user } = useAuth();
    
    async function handleFetch(token, user) {
        const response = await fetch(`http://localhost:8080/api/buy/bids/active`, {
            method: 'POST',
            headers: { 
                'Authorization': `Bearer ${token}`,
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
        handleFetch(token, user)
            .then(data => setData(data.items))
            .catch(err => console.log(err))
    }, [token, user]);
    
    return (
        <div className="BuyPage-Content">
            <div className="BuyPage-ContentPage">
                {data && <Listings data={ data }/>}
            </div>
        </div>
    )
}

function BuyPageBody() {
    return (
        <div id="BuyPage-Body">
            <Content/>
        </div>
    )
}

export default function BuyPage() {
    return (
        <div id="BuyPage">
            <Header />
            <BuyPageHeader />
            <BuyPageBody />
        </div>
    )
}