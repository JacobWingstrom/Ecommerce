import '../Sheets/App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import SignInOrUp from './SignInOrUp.js';
import SignIn from './SignIn.js';
import SignUp from './SignUp.js';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SignInOrUp />} />
        <Route path="/SignIn" element={<SignIn />} />
        <Route path="/SignUp" element={<SignUp />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
