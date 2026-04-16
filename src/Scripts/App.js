import '../Sheets/App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import SignIn from './SignIn.js';
import SignUp from './SignUp.js';
import AuthProvider from '../Context/AuthContext.js'
import MainPage from './MainPage.js';
import SellPage from './SellPage.js';
import BuyPage from './BuyPage.js';
import AccountPage from './AccountPage.js'
import ListingPage from './ListingPage.js';
import MessagesPage from './MessagesPage.js'
import DirectMessagePage from './DirectMessagePage.js'
function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<SignIn />} />
          <Route path="/SignUp" element={<SignUp />} />
          <Route path="/MainPage" element={<MainPage />} />
          <Route path="/SellPage" element={<SellPage />} />
          <Route path="/BuyPage" element={<BuyPage />} />
          <Route path="/AccountPage" element={<AccountPage />} />
          <Route path="/ListingPage/:item" element={<ListingPage />} />
          <Route path="/MessagesPage" element={<MessagesPage />} />
          <Route path="/MessagesPage" element={<MessagesPage />} />
          <Route path="/DirectMessagePage/:messageId" element={<DirectMessagePage />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
