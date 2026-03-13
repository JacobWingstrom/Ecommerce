import '../Sheets/App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import SignInOrUp from './SignInOrUp.js';
import SignIn from './SignIn.js';
import SignUp from './SignUp.js';
import AuthContext from '../Context/AuthContext.js'
import MainPage from './MainPage.js';

function App() {
  return (
    <AuthContext>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<SignInOrUp />} />
          <Route path="/SignIn" element={<SignIn />} />
          <Route path="/SignUp" element={<SignUp />} />
          <Route path="/MainPage" element={<MainPage />} />
        </Routes>
      </BrowserRouter>
    </AuthContext>
  );
}

export default App;
