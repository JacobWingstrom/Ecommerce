import '../Sheets/App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import SignIn from './SignIn.js';
import SignUp from './SignUp.js';
import AuthProvider from '../Context/AuthContext.js'
import MainPage from './MainPage.js';
import SellPage from './SellPage.js';
import AccountPage from './AccountPage.js'

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<SignIn />} />
          <Route path="/SignUp" element={<SignUp />} />
          <Route path="/MainPage" element={<MainPage />} />
          <Route path="/SellPage" element={<SellPage />} />
          <Route path="/AccountPage" element={<AccountPage />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
