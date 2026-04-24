import '../Sheets/BuyPage.css'
import Header from "./Header.js"
import { useState, useEffect } from "react";
import { useAuth } from '../Context/AuthContext.js'
import Listings from './Listings.js';

const ARIZONA_CITIES = ['Ahwatukee', 'Ajo', 'Apache Junction', 'Avondale', 'Benson', 'Bisbee', 'Buckeye', 'Bullhead City', 'Camp Verde', 'Carefree', 'Casa Grande', 'Cave Creek', 'Chandler', 'Clifton', 'Coolidge', 'Cottonwood', 'Douglas', 'El Mirage', 'Eloy', 'Flagstaff', 'Florence', 'Fountain Hills', 'Gilbert', 'Glendale', 'Globe', 'Goodyear', 'Holbrook', 'Kingman', 'Lake Havasu City', 'Laveen', 'Litchfield Park', 'Marana', 'Maricopa', 'Mesa', 'Nogales', 'Oro Valley', 'Page', 'Paradise Valley', 'Parker', 'Payson', 'Peoria', 'Phoenix', 'Prescott', 'Prescott Valley', 'Queen Creek', 'Quartzsite', 'Safford', 'Sahuarita', 'San Tan Valley', 'Scottsdale', 'Sedona', 'Show Low', 'Sierra Vista', 'Somerton', 'Sun City', 'Sun City West', 'Surprise', 'Tempe', 'Tolleson', 'Tombstone', 'Tucson', 'Wickenburg', 'Williams', 'Winslow', 'Youngtown', 'Yuma' ];

function BuyPageHeader({ setLocation, location, setSearch }) {
    return (
        <div id="BuyPage-Header">
            <h1>All Listings</h1>
            <select className="BuyPage-Input" onChange={e => setLocation(e.target.value)} value={location}>
                <option value="">Select City</option>
                {ARIZONA_CITIES.map(c => (
                    <option key={c} value={c}>{c}</option>
                ))}
            </select>
            <label>Search: <input type='text' placeholder='' onChange={e => setSearch(e.target.value.toLowerCase())} /></label>
        </div>
    );
}

function Content({ location, search }) {
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

    const filtered = data
        ? data
            .filter(item => !location || item.location === location)
            .filter(item => !search || item.username.toLowerCase().includes(search))
        : null;

    return (
        <div className="BuyPage-Content">
            <div className="BuyPage-ContentPage">
                {filtered && <Listings data={ filtered }/>}
            </div>
        </div>
    )
}

function BuyPageBody({ location, search }) {
    return (
        <div id="BuyPage-Body">
            <Content location={location} search={search}/>
        </div>
    )
}

export default function BuyPage() {
    const [ location, setLocation ] = useState('')
    const [ search, setSearch ] = useState('')
    return (
        <div id="BuyPage">
            <Header/>
            <BuyPageHeader setLocation={setLocation} location={location} setSearch={setSearch}/>
            <BuyPageBody location={location} search={search}/>
        </div>
    )
}