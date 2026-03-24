import '../Sheets/AccountPage.css';
import Header from './Header.js'
import { useAuth } from '../Context/AuthContext.js'
import { useState } from 'react'

function Buttons({ setter }) {
    return ( 
        <div className="AccountPage-Buttons">
            <button onClick={ () => setter('MyItems') } className="AccountPage-Button" id="AccountPage-MyItems">My Items</button>
            <button onClick={ () => setter('BidsWon') } className="AccountPage-Button" id="AccountPage-BidsWon">Bids Won</button>
            <button onClick={ () => setter('BiddingOn') } className="AccountPage-Button" id="AccountPage-BiddingOn">Bidding On</button>
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

function Listings({ currentPage }){
    return (
        <div className='AccountPage-ContentPage'>
            
        </div>
    )
}

function Content({ currentPage }) {
    return (
        <div className="AccountPage-Content">
            {currentPage && <Listings currentPage={ currentPage }/>}
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
    const [currentPage, setCurrentPage] = useState('MyItems');

    return (
        <div id="AccountPage">
            <Header />
            <AccountHeader setter={ setCurrentPage }/>
            <AccountBody currentPage={ currentPage }/>
        </div>
    )
}