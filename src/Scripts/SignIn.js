import '../Sheets/SignIn.css';
import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../Context/AuthContext.js';
import pineappleLogo from '../Images/pineappleLogoPlaceholder.jpg';
 
async function signInUser(credentials) {
  const response = await fetch('http://localhost:8080/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    credentials: 'include',
    body: JSON.stringify(credentials)
  });
 
  if (!response.ok) throw new Error("Invalid Credentials");
  return response.json();
}
 
function SignInForm() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();
 
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const data = await signInUser({ username, password });
      login(data.user, data.token);
      navigate('/MainPage');
    } catch {
      setError('Invalid login credentials. Please try again.');
    }
  };
 
  return (
    <div className="form">
      {error && <p>{error}</p>}
 
      <label>
        EMAIL
        <input
          type="email" name="email" placeholder="JohnDoe@example.com"
          onChange={e => setUsername(e.target.value)} required
        />
      </label>
 
      <label>
        PASSWORD
        <input type="password" name="password" placeholder="Password" 
            onChange={e => setPassword(e.target.value)} required
        />
      </label>
 
      <button className="sign-in-button" onClick={handleSubmit}>
        Sign In
      </button>
 
      <p className="create-account-link">
        Don't have an account?{' '}
        <a href="/SignUp">Create one</a>
      </p>
    </div>
  );
}
 
export default function SignIn() {
  return (
    <div className="sign-in-div">
      <div className="left-container">
        <div className="logo-wrapper">
          <img src={pineappleLogo} alt="Pineapple logo" />
        </div>
        <span className="brand-name">Pineapple</span>
        <span className="brand-tagline">Bid local, pick up fast
            <br></br>That's the Pineapple Express way.</span>
      </div>
 
      <div className="right-container">
        <h2>Log In</h2>
        <SignInForm />
      </div>
    </div>
  );
}