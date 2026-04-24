import '../Sheets/SellPage.css'
import Header from "./Header.js"
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { useAuth } from '../Context/AuthContext.js'

const ARIZONA_CITIES = ['Ahwatukee', 'Ajo', 'Apache Junction', 'Avondale', 'Benson', 'Bisbee', 'Buckeye', 'Bullhead City', 'Camp Verde', 'Carefree', 'Casa Grande', 'Cave Creek', 'Chandler', 'Clifton', 'Coolidge', 'Cottonwood', 'Douglas', 'El Mirage', 'Eloy', 'Flagstaff', 'Florence', 'Fountain Hills', 'Gilbert', 'Glendale', 'Globe', 'Goodyear', 'Holbrook', 'Kingman', 'Lake Havasu City', 'Laveen', 'Litchfield Park', 'Marana', 'Maricopa', 'Mesa', 'Nogales', 'Oro Valley', 'Page', 'Paradise Valley', 'Parker', 'Payson', 'Peoria', 'Phoenix', 'Prescott', 'Prescott Valley', 'Queen Creek', 'Quartzsite', 'Safford', 'Sahuarita', 'San Tan Valley', 'Scottsdale', 'Sedona', 'Show Low', 'Sierra Vista', 'Somerton', 'Sun City', 'Sun City West', 'Surprise', 'Tempe', 'Tolleson', 'Tombstone', 'Tucson', 'Wickenburg', 'Williams', 'Winslow', 'Youngtown', 'Yuma' ];

function SellPageHeader() {
    return (
        <div id="SellPage-header">
            <h1>Sell Item</h1>
        </div>
    );
}

async function listItem(data, token) {
    
    const response = await fetch('http://localhost:8080/api/item/createListing', {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}` },
        body: data  
    });

    if (!response.ok) throw new Error("Listing Failed");
    return response.json();
}

function SellPageBody() {
    const [title, setTitle] = useState('')
    const [description, setDescription] = useState('')
    const [price, setPrice] = useState('')
    const [location, setLocation] = useState('')
    const [image, setImage] = useState(null)
    const [endDate, setEndDate] = useState('')
    const [error, setError] = useState('')
    
    const navigate = useNavigate();
    const { token } = useAuth();

    const handleSubmit = async e => {
        e.preventDefault();
        try{
            const data = new FormData();
            data.append('title', title);
            data.append('minimumPrice', price);
            data.append('description', description);
            data.append('location', location)
            data.append('image', image);
            data.append('endDate', endDate);
            await listItem(data, token);

            navigate('/AccountPage')
        } catch {
            setError('Invalid Listing');
        }
    }

    return (
        <div id="SellPage-body">
            <form className="SellPage-form" onSubmit={handleSubmit}>
                {error && <p id="SellPage-error" style={{color: 'red'}}>{error}</p>}
                <label>
                    <p className="SellPage-Label">Item Name:</p>
                    <input className="SellPage-Input" type="text" name="itemName" onChange={ e => setTitle(e.target.value) } />
                </label>
                <label>
                    <p className="SellPage-Label">Description:</p>
                    <textarea className="SellPage-Input" name="description" onChange={ e => setDescription(e.target.value) } rows={4}/>
                </label>
                <label>
                    <p className="SellPage-Label">Starting Price:</p>
                    <input className="SellPage-Input" type="number" name="price" onChange={ e => setPrice(e.target.value) } />
                </label>
                <label>
                    <p className="SellPage-Label">City:</p>
                    <select className="SellPage-Input" onChange={e => setLocation(e.target.value)} value={location}>
                        <option value="">Select City</option>
                        {ARIZONA_CITIES.map(c => (
                            <option key={c} value={c}>{c}</option>
                        ))}
                    </select>
                </label>
                <label>
                    <p className="SellPage-Label">Image:</p>
                    <br />
                    <input type="file" accept="image/*" onChange={ e => setImage(e.target.files[0])} />
                </label>
                <label>
                    <p className="SellPage-Label">End Date:</p>
                    <input className="SellPage-Input" type="date" name="endDate" onChange={ e => setEndDate(e.target.value) } />
                </label>
                <br />

                <input id="SellPage-Button" type="submit" value="List Item" disabled={!title || !description || !price || !location || !image || !endDate}/>
            </form>
        </div>
    );
}

export default function SellPage() {
    return (
        <div id="SellPage">
            <Header />
            <SellPageHeader />
            <SellPageBody />
        </div>
    )
}