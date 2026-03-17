import '../Sheets/App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import SignInOrUp from './SignInOrUp.js';
import SignIn from './SignIn.js';
import SignUp from './SignUp.js';
import AuthProvider from '../Context/AuthContext.js'
import MainPage from './MainPage.js';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<SignInOrUp />} />
          <Route path="/SignIn" element={<SignIn />} />
          <Route path="/SignUp" element={<SignUp />} />
          <Route path="/MainPage" element={<MainPage />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
