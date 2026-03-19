import '../Sheets/SignUp.css';
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import pineappleLogo from '../Images/pineappleLogoPlaceholder.jpg';
 
async function registerUser(credentials) {
    const response = await fetch('http://localhost:8080/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(credentials)
    });

    if (!response.ok) throw new Error('Registration failed');
    return response.json();
}

function SignUpForm() {
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
 
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (password !== confirmPassword) {
            setPassword('');
            setConfirmPassword('');
            setError('Passwords do not match. Please try again.');
            return;
        }

        try {
            await registerUser({ email, username, password });
            navigate('/');
        } catch {
            setError('Something went wrong. Please try again.');
        }
    };
 
    return (
        <form className='form' onSubmit={handleSubmit}>
            {error && <p className="error-msg">{error}</p>}
 
            <label>
                EMAIL
                <input type='email' placeholder='JohnDoe@example.com' value={email} className="email"
                onChange={e => setEmail(e.target.value)} required />
            </label>
 
            <label>
                USERNAME
                <input type='text' placeholder='JohnDoe' value={username} className="username"
                onChange={e => setUsername(e.target.value)} required />
            </label>
 
            <label>
                PASSWORD
                <input type='password' placeholder='Password' value={password} className="password" 
                onChange={e => setPassword(e.target.value)} required />
            </label>
 
            <label>
                CONFIRM PASSWORD
                <input type='password' placeholder='Password' value={confirmPassword} className="password"
                onChange={e => setConfirmPassword(e.target.value)} required />
            </label>
 
            <button type='submit' className='button'>Create Account</button>
 
            <p className='sign-in-link'>
                Already have an account? <a href="/">Sign in</a>
            </p>
        </form>
    );
}
 
function SignUpContainer() {
    return (
        <div className='container'>
            <img src={pineappleLogo} alt="Pineapple logo" className='image' />
            <h2>Get Started With Your Account</h2>
            <SignUpForm />
        </div>
    );
}
 
export default function SignUp() {
    return (
        <div className='sign-up-div'>
            <SignUpContainer />
        </div>
    );
}