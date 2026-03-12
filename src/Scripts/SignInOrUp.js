import '../Sheets/SignInOrUp.css'
import { useNavigate } from 'react-router-dom';


export default function SignInOrUp(){
    const navigate = useNavigate();
    return(
        <div className="SignInOrUp-Buttons">
            <button onClick={() => navigate('/SignIn')}>Sign in to Account</button>
            <button onClick={() => navigate('/SignUp')}>Sign up for Account</button>
        </div>
    );
}