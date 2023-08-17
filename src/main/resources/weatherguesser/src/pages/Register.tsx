import React, {useEffect, useState} from 'react';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import UniquenessHandler from "../handlers/uniquenessHandler";
import {Link, useNavigate} from "react-router-dom";

interface RegisterData {
    name: string;
    email: string;
    password: string;
}
const userData: RegisterData = {
    name: 'yourUsername',
    email: 'yourEmail',
    password: 'yourPassword'
};

const Register: React.FC = () => {
    const navigate = useNavigate();
    if(localStorage.getItem("token")) navigate("../account");
    const usernameElem = document.getElementById('usernameInput') as HTMLInputElement;
    const passwordElem = document.getElementById('passwordInput') as HTMLInputElement;
    const uniquenessHandlerInstance = new UniquenessHandler();
    const [userData, setUserData] = useState<RegisterData>({
        name: '',
        email: '',
        password: ''
    });
    const handlerRegister = async () => { //ADD EMAIL VERIFICATION
        toast.info('Creating an account for you!', {
            position: toast.POSITION.TOP_CENTER,
            draggablePercent: 50,
            role: "alert"
        })
        try {
            const check1 = await uniquenessHandlerInstance.checkUsername(userData.name);
            const check2 = await uniquenessHandlerInstance.checkEmail(userData.email);
            if(check1!=="Success!") {
                toast.dismiss();
                toast.error(check1, {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    role: "alert"
                })
            } else if(check2!=="Success!") {
                toast.dismiss();
                toast.error(check2, {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    role: "alert"
                })
            } else await registerRequest();
        } catch (error) {
            console.error('Login failed:', error);
        }
    };
    async function registerRequest() {
        try {
            const response = await axios.post<string>('http://localhost:5000/api/user/add', userData);
            if(response.data==="user_not_created") {
                toast.dismiss();
                toast.error("User not created!", {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    role: "alert"
                });
            } else {
                console.log('Register successful:', response.data);
                toast.dismiss();
                toast.success("User created! You will be redirected to home page!", {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    autoClose: 3000,
                    role: "alert"
                });
                localStorage.setItem("token",response.data);
                setTimeout(()=>{window.location.href = "../home";},3000);
            }
            // Do something with the successful response
        } catch (error) {
            console.error('Register failed:', error);
            // Handle the error here
        }
    }

    return (
        <div>
            <p className={"title"}>Register</p>
            <p>Username</p>
            <input type={"text"} placeholder={"Type your username"} id="usernameInput" onChange={(e) => setUserData({ ...userData, name: e.target.value })}/>
            <p>Email</p>
            <input type={"text"} placeholder={"Type your email"} id="emailInput" onChange={(e) => setUserData({ ...userData, email: e.target.value })}/>
            <p>Password</p>
            <input type={"password"} placeholder={"Type your password"} id="passwordInput" onChange={(e) => setUserData({ ...userData, password: e.target.value })}/>
            <button onClick={()=>handlerRegister()}>Register</button>
            <Link to={"../login"}>Or sign in?</Link>
            <ToastContainer />
        </div>
    );
};

export default Register;