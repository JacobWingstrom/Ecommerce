import '../Sheets/SignIn.css'
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../Context/AuthContext.js'

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
    const [error, setError] = useState('')
    const { login } = useAuth();
    const { navigate } = useNavigate();

    const handleSubmit = async e => {
        e.preventDefault();
        setError('');
        try {
            const user = await signInUser({
                username,
                password
            });
            login(user);
            navigate('/MainPage.js');
        } catch {
            setError('Invalid Login Credentials');
            navigate('/MainPage.js');
        }
    }

    return(
        <form onSubmit={handleSubmit}>
            {error && <p id="SignIn-error">{error}</p>}
            <label>
                <p>Email: </p>
                <input type="email" name="email" onChange={e => setUsername(e.target.value)} />
            </label>
            <br />

            <label>
                <p>Password: </p>
                <input type="password" name="password" onChange={e => setPassword(e.target.value)} />
            </label>
            <br />

            <input type="submit" value="Submit" />
        </form>
    );
}
export default function SignIn() {
    return(
        <SignInForm />
    );
}