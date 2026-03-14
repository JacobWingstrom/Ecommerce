import '../Sheets/SignUp.css'
import pineappleLogo from '../Images/pineappleLogoPlaceholder.jpg';

function checkFields(event) {
    event.preventDefault();

    const passwordInput = document.getElementById("password");
    const confirmPasswordInput = document.getElementById("confirmPassword");

    const password = passwordInput.value;
    const confirmPassword = confirmPasswordInput.value;

    if (password !== confirmPassword) {
        passwordInput.value = '';
        confirmPasswordInput.value = '';
        alert("Passwords do not match");
        return;
    }

    alert("Account created successfully!");
}

function SignUpForm() {

    return (
        <form className='form' onSubmit={checkFields}>  
            <label>Email</label><br></br>
            <input type='email' placeholder='Email'></input><br></br>
            <br></br>
            <label>Username</label><br></br>
            <input type='text' placeholder='Username'></input><br></br>
            <br></br>
            <label>Password</label><br></br>
            <input id='password' type='password' placeholder='password'></input><br></br>
            <br></br>
            <label>Confirm Password</label><br></br>
            <input id='confirmPassword' type='password' placeholder='password'></input><br></br>
            <br></br>
            <button type='submit' className='button'>Sign Up</button>
        </form>
    );
}

function SignUpContainer() {
    return (
        <div className='container'>
            <img src={pineappleLogo} alt="Pineapple logo" className='image'></img>
            <h2>Get Started With Your Account</h2>
            <SignUpForm />
        </div>
    );
}



export default function SignUp() {
    return(
        <div className='sign-up-div'>
            <SignUpContainer />
        </div>
    );
}