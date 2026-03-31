import '../Sheets/BuyPage.css'
import Header from "./Header.js"
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { useAuth } from '../Context/AuthContext.js'

function Filters() {

}

function BuyPageHeader() {
    return (
        <div id="BuyPage-Header">
            <h1>All Listings</h1>
        </div>
    );
}

function Listings({ currentPage }){
    return (
        <div className='BuyPage-ContentPage'>
            
        </div>
    )
}

function Content({ currentPage }) {
    return (
        <div className="BuyPage-Content">
            {<Listings currentPage={ currentPage }/>}
        </div>
    )
}

function BuyPageBody({ currentPage }) {

    return (
        <div id="BuyPage-Body">
            <Content currentPage={ currentPage }/>
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