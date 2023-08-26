import React, {useState} from 'react';
import axios from 'axios';
import {Link, useNavigate} from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import TextField from "@mui/material/TextField";
import AccountInput from "../components/accountInput";
import UniquenessHandler from "../handlers/uniquenessHandler";

interface Data {
    name: string;
    email: string;
    password: string;
}

const Auth: React.FC = () => {
    const navigate = useNavigate();
    if(localStorage.getItem("token")) navigate("../account");
    const [isLogin, setIsLogin] = useState(true);
    const [userData, setUserData] = useState<Data>({name: '', email: '', password: ''});
    const uniquenessHandlerInstance = new UniquenessHandler();

    const handleLogin = async () => {
        try {
            await loginRequest();
        } catch (error) {
            console.error('Login failed:', error);
        }
    }

    const handlerRegister = async () => { //ADD EMAIL VERIFICATION
        try {
            const check1 = await uniquenessHandlerInstance.checkUsername(userData.name);
            const check2 = await uniquenessHandlerInstance.checkEmail(userData.email);
            if(check1!=="Success!") {
                toast.dismiss();
                toast.error(check1, {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    role: "alert",
                    autoClose: 6000
                })
            } else if(check2!=="Success!") {
                toast.dismiss();
                toast.error(check2, {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    role: "alert",
                    autoClose: 6000
                })
            } else await registerRequest();
        } catch (error) {
            console.error('Login failed:', error);
        }
    }
    //TODO: fix duplicate code
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
                setTimeout(()=>{window.location.href = "../account";},3000);
            }
            // Do something with the successful response
        } catch (error) {
            console.error('Login failed:', error);
            // Handle the error here
        }
    }

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
                toast.success("User created! You will be redirected to your account!", {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    autoClose: 3000,
                    role: "alert"
                });
                localStorage.setItem("token",JSON.stringify(response.data));
                setTimeout(()=>{window.location.href = "../account";},3000);
            }
            // Do something with the successful response
        } catch (error) {
            console.error('Register failed:', error);
            // Handle the error here
        }
    }

    return (
        localStorage.getItem("token")==null ? (
            <div className="gradient-bg w-screen h-screen bg-gradient-to-r from-pink-300 via-pink-200 to-blue-200 animate-gradient flex flex-col items-center justify-center">
                <div className="flex justify-around w-account w-min-account m-5">
                    <button onClick={()=>setIsLogin(true)} className={`bg-gray-200 p-2 rounded-lg border-4 border-blue-200 text-pink-300 ${isLogin ? "selectedAccForm" : ""}`}>Login</button>
                    <button onClick={()=>setIsLogin(false)} className={`bg-gray-200 p-2 rounded-lg border-4 border-pink-300  text-blue-200 ${!isLogin ? "selectedAccForm" : ""}`}>Register</button>
                </div>
                <div className="bg-white rounded-lg w-account min-w-account max-h-account p-5 m-4 shadow-lg">
                    <p className={"title gradient-bg font-black text-2xl text-transparent bg-clip-text bg-gradient-to-r from-blue-300 via-blue-200 to-pink-200 inset-0 text-center"}>{isLogin ? "LOGIN" : "REGISTER"}</p>
                    <div className="flex justify-between pt-2">
                        <span className={"gradient-bg font-black text-transparent bg-clip-text bg-gradient-to-r from-blue-300 via-blue-200 to-pink-200 inset-0"}>Username</span>
                        <AccountInput>
                            <TextField  type={"text"}  id="usernameInput" placeholder={`type your name`} onChange={(e) => setUserData({ ...userData, name: e.target.value })} InputProps={{ disableUnderline: true }} />
                        </AccountInput>
                    </div>
                    {!isLogin &&
                        <div className="flex justify-between pt-2">
                            <span  className={"gradient-bg font-black text-transparent bg-clip-text bg-gradient-to-r from-blue-300 via-blue-200 to-pink-200 inset-0"}>Email</span>
                            <AccountInput>
                                <TextField  type={"text"}  id="emailInput" placeholder={`type your email`} onChange={(e) => setUserData({ ...userData, email: e.target.value })} InputProps={{ disableUnderline: true }} />
                            </AccountInput>
                        </div>
                    }
                    <div className="flex justify-between pt-2">
                        <span className={"gradient-bg font-black text-transparent bg-clip-text bg-gradient-to-r from-blue-300 via-blue-200 to-pink-200 inset-0 mr-3"}>Password</span>
                        <AccountInput>
                            <TextField  type={"password"}  id="passwordInput" placeholder={`type your password`} onChange={(e) => setUserData({ ...userData, password: e.target.value })} InputProps={{ disableUnderline: true }} />
                        </AccountInput>
                    </div>
                    {isLogin && <div className="flex justify-end pt-2 pb-2 text-sm font-normal"><Link to={"."}>Forgot password?</Link></div>}
                    {isLogin ?
                        <div className="flex justify-center pt-2">
                            <button onClick={()=>handleLogin()}>Login</button>
                        </div>
                        :
                        <div className="flex justify-center pt-2">
                            <button onClick={()=>handlerRegister()}>Register</button>
                        </div>
                    }
                    <ToastContainer />
                </div>
            </div>
        ) : window.location.href = "../account"
    );
};

export default Auth;