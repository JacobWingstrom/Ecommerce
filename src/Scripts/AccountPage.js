import '../Sheets/AccountPage.css';
import Header from './Header.js'
//import pineappleLogo from '../Images/pineappleLogoPlaceholder.jpg'
import { useAuth } from '../Context/AuthContext.js'
import { useState } from 'react'

function AccountHeader() {
    const { user } = useAuth();
    
    return (
        <div id="AccountPage-Header">
            <h1>{user}</h1>
        </div>
    )
}



function Buttons({ setter }) {
    return ( 
        <div className="AccountPage-Buttons">
            <button onClick={ () => setter('MyItems') } className="AccountPage-Button" id="AccountPage-MyItems">My Items</button>
            <button onClick={ () => setter('BidsWon') } className="AccountPage-Button" id="AccountPage-BidsWon">Bids Won</button>
            <button onClick={ () => setter('BiddingOn') } className="AccountPage-Button" id="AccountPage-BiddingOn">Bidding On</button>
        </div>
    )
}

function MyItems() {
    return (
        <div className='AccountPage-MyItems'>

        </div>
    )
}

function BidsWon() {
        <div className='AccountPage-BidsWon'>
            
        </div>
}

function BiddingOn() {
        <div className='AccountPage-BiddingOn'>
            
        </div>
}

function Content({ currentPage }) {
    return (
        <div className="AccountPage-Content">
            {currentPage === 'MyItems' && <MyItems />}
            {currentPage === 'BidsWon' && <BidsWon />}
            {currentPage === 'BiddingOn' && <BiddingOn />}
        </div>
    )
}



function AccountBody() {
    const [currentPage, setCurrentPage] = useState('MyItems');

    return (
        <div id="AccountPage-Body">
            <Buttons setter={ setCurrentPage }/>
            <Content currentPage={ currentPage }/>
        </div>
    )
}

export default function AccountPage() {
    return (
        <div id="AccountPage">
            <Header />
            <AccountHeader />
            <AccountBody />
        </div>
    )
}