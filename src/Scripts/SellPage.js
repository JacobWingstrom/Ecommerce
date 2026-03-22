import '../Sheets/SellPage.css'
import Header from "./Header.js"
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { useAuth } from '../Context/AuthContext.js'

function SellPageHeader() {
    const navigate = useNavigate();

    return (
        <div id="SellPage-header">
            <h1>Sell Item</h1>
            <button id="SellPage-Button" onClick={ () => navigate('/AccountPage')}>My Listings</button>
        </div>
    );
}

async function listItem(data, token) {
    
    const response = await fetch('http://localhost:8080/createListing', {
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
    const [image, setImage] = useState(null)
    const [error, setError] = useState('')
    
    const navigate = useNavigate();
    const { token } = useAuth();

    const handleSubmit = async e => {
        e.preventDefault();
        try{
            const data = new FormData();
            data.append('title', title);
            data.append('price', price);
            data.append('description', description);
            data.append('image', image);
        
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
                    <p>Item Name:</p>
                    <input type="text" name="itemName" onChange={ e => setTitle(e.target.value) } />
                </label>
                <label>
                    <p>Description:</p>
                    <textarea name="description" onChange={ e => setDescription(e.target.value) } rows={4}/>
                </label>
                <label>
                    <p>Starting Price:</p>
                    <input type="number" name="price" onChange={ e => setPrice(e.target.value) } />
                </label>
                <label>
                    <p>Image:</p>
                    <input type="file" accept="image/*" onChange={ e => setImage(e.target.files[0])} />
                </label>
                <br />

                <input type="submit" value="List Item" disabled={!title || !description || !price || !image}/>
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