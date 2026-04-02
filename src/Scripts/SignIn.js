import '../Sheets/SignIn.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../Context/AuthContext.js';
import pineappleLogo from '../Images/pineappleLogoPlaceholder.jpg';
 
async function signInUser(credentials) {
  console.log("print2");
  console.log(credentials.username)
  console.log(credentials.password)

  const response = await fetch('http://localhost:8080/api/auth/login', {
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
    console.log("print");
    try {
      const data = await signInUser({ username, password });
      console.log(data.username)
      console.log(data.token)
      login(data.username, data.token);
      navigate('/MainPage');
    } catch {
      setError('Invalid login credentials. Please try again.');
    }
  };
 
  return (
    <div className="form">
      <form onSubmit={handleSubmit}>
        {error && <p>{error}</p>}
  
        <label>
          EMAIL
          <input
            type="text" name="email" placeholder="JohnDoe@example.com"
            onChange={e => setUsername(e.target.value)} required
          />
        </label>
  
        <label>
          PASSWORD
          <input type="password" name="password" placeholder="Password" 
              onChange={e => setPassword(e.target.value)} required
          />
        </label>
  
        <button className="sign-in-button" disabled={!username || !password}>
          Sign In
        </button>
      </form>
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
        <span className="brand-name">Pineapple Express</span>
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