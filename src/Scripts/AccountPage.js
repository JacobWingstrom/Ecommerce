import '../Sheets/AccountPage.css';
import Header from './Header.js'
//import pineappleLogo from '../Images/pineappleLogoPlaceholder.jpg'
//import { useAuth } from '../Context/AuthContext.js'

function AccountHeader() {
    //const { user } = useAuth();
    
    return (
        <div id="AccountPage-Header">
            <h1>UserName</h1>
        </div>
    )
}

function Buttons() {
    return (
        <div className="AccountPage-Buttons">

        </div>
    )
}

function Content() {
    
}



function AccountBody() {
    return (
        <div id="AccountPage-Body">
            <Buttons />
            <Content />
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