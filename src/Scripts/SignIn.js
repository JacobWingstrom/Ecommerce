import '../Sheets/SignIn.css'
import { useState } from 'react';

async function signInUser(credentials) {
  return fetch('http://localhost:8080/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(credentials)
  })
    .then(data => data.json());
}

function SignInForm() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    
    const handleSubmit = async e => {
        e.preventDefault();
        const token = await signInUser({
            username,
            password
        });

        //Do Something with token
    }

    return(
        <form onSubmit={handleSubmit}>
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