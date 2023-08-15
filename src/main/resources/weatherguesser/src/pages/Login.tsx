import React, {useEffect, useState} from 'react';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface LoginData {
    name: string;
    password: string;
}
const userData: LoginData = {
    name: 'yourUsername',
    password: 'yourPassword'
};

const Login: React.FC = () => {
    const usernameElem = document.getElementById('usernameInput') as HTMLInputElement;
    const passwordElem = document.getElementById('passwordInput') as HTMLInputElement;
    const [userData, setUserData] = useState<LoginData>({
        name: '',
        password: ''
    });

    const handleLogin = async () => {
        try {
            await loginRequest();
        } catch (error) {
            console.error('Login failed:', error);
        }
    };
    async function loginRequest() {
        try {
            const response = await axios.post<string>('http://localhost:5000/api/user/login', userData);
            if(response.data==="user_not_found") {
                toast.error("User not found!", {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    role: "alert"
                });
            } else {
                console.log('Login successful:', response.data);
                localStorage.setItem("token",JSON.stringify(response.data));
                toast.success("Logged in! You will be redirected to your account!", {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    autoClose: 3000,
                    role: "alert"
                });
                setTimeout(()=>{window.location.href = "../home";},3000);
            }
            // Do something with the successful response
        } catch (error) {
            console.error('Login failed:', error);
            // Handle the error here
        }
    }

    return (
        <div>
            <p className={"title"}>Login</p>
            <p>Username</p>
            <input type={"text"} placeholder={"Type your username"} id="usernameInput" onChange={(e) => setUserData({ ...userData, name: e.target.value })}/>
            <p>Password</p>
            <input type={"password"} placeholder={"Type your password"} id="passwordInput" onChange={(e) => setUserData({ ...userData, password: e.target.value })}/>
            <a href={"."}>Forgot password?</a>
            <button onClick={()=>handleLogin()}>Login</button>
            <a href={"../register"}>Or sign up?</a>
            <ToastContainer />
        </div>
    );
};

export default Login;