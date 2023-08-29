import React, {useState} from 'react';
import axios from 'axios';
import {Link, useNavigate} from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import TextField from "@mui/material/TextField";
import AccountInput from "../contexts/accountInput";
import UniquenessHandler from "../handlers/uniquenessHandler";
import IAuthData from "../interfaces/IAuthData";

const Auth: React.FC = () => {
    const navigate = useNavigate();
    if(localStorage.getItem("token")) navigate("../account");
    const [isLogin, setIsLogin] = useState(true);
    const [userData, setUserData] = useState<IAuthData>({name: '', email: '', password: ''});
    const uniquenessHandlerInstance = new UniquenessHandler();

    const handleLogin = async () => {
        try {
            await serverRequest("login");
        } catch (error) {
            console.error('Login failed:', error);
        }
    }

    const handleRegister = async () => {
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
            } else await serverRequest("add");
        } catch (error) {
            console.error('Register failed:', error);
        }
    }

    async function serverRequest(type: string) {
        try {
            const response = await axios.post<string>(`http://localhost:5000/api/user/${type}`, userData);
            if (response.data === "user_not_found") {
                toast.error("User not found!", {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    role: "alert"
                });
            } else {
                localStorage.setItem("token", JSON.stringify(response.data));
                toast.success(`${type === "login" ? "Logged in! You will be redirected to your account!" : "User created! You will be redirected to your account!"}`, {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    autoClose: 3000,
                    role: "alert"
                });
                setTimeout(() => {
                    window.location.href = "../home";
                }, 3000);
            }
        } catch (error) {
            console.error('Request failed:', error);
        }
    }

    return (
        localStorage.getItem("token")==null ? (
            <div className="animate-gradientAnimation w-screen h-screen bg-gradient-to-r from-logo to-second animate-gradient flex flex-col items-center justify-center" style={{backgroundSize:'400% 400%'}}>
                <div className="flex justify-center items-center w-min-account m-5">
                    <button onClick={()=>setIsLogin(true)} className={`hover:border-second hover:bg-logo hover:filter hover:drop-shadow-md w-authBtn bg-gray-200 p-2 pl-5 pr-5 rounded-lg border-4 text-center text-second ${isLogin ? "bg-white shadow-md border-second" : "border-logo"} ease-in-out duration-500`} >Login</button>
                    <div className={"m-2 p-2 bg-white rounded-lg shadow-md flex justify-center items-center"}>
                        <img alt="dark logo" src={"./icons/logodark.png"} className={"w-footerIcon"}/>
                    </div>
                    <button onClick={()=>setIsLogin(false)} className={`hover:border-second hover:bg-logo hover:filter hover:drop-shadow-md w-authBtn bg-gray-200 p-2 pl-5 pr-5 rounded-lg border-4 text-center text-second ${!isLogin ? "bg-white shadow-md border-second" : "border-logo"} ease-in-out duration-500`}>Register</button>
                </div>
                <div className="bg-white rounded-lg w-account min-w-account max-h-account p-5 m-4 shadow-lg">
                    <p className={`title animate-gradientAnimation font-black text-2xl inset-0 text-center text-transparent bg-clip-text bg-gradient-to-r from-second to-logo `} style={{backgroundSize:'400% 400%'}}>{isLogin ? "LOGIN" : "REGISTER"}</p>
                    <div className="flex justify-between pt-2">
                        <span className={`font-black inset-0 text-second mr-3`}>Username</span>
                        <AccountInput>
                            <TextField  type={"text"}  id="usernameInput" placeholder={`type your name`} onChange={(e) => setUserData({ ...userData, name: e.target.value })} />
                        </AccountInput>
                    </div>
                    <div className="flex justify-between pt-2">
                        <span className={`font-black text-second inset-0 mr-3`}>Password</span>
                        <AccountInput>
                            <TextField  type={"password"}  id="passwordInput" placeholder={`type your password`} onChange={(e) => setUserData({ ...userData, password: e.target.value })} />
                        </AccountInput>
                    </div>
                    {!isLogin &&
                        <div className="flex justify-between pt-2">
                            <span  className={`font-black text-second mr-3 inset-0`}>Email</span>
                            <AccountInput>
                                <TextField  type={"text"}  id="emailInput" placeholder={`type your email`} onChange={(e) => setUserData({ ...userData, email: e.target.value })} />
                            </AccountInput>
                        </div>
                    }
                    {isLogin && <div className="flex justify-end pt-2 pb-2 text-sm font-normal text-gray-300"><Link to={"."}>Forgot password?</Link></div>}
                    {isLogin ?
                        <div className="flex justify-center pt-2">
                            <button onClick={()=>handleLogin()} className="text-second rounded-lg p-2 pl-5 pr-5 border-4 hover:border-second hover:shadow-md ease-in-out duration-500">Login</button>
                        </div>
                        :
                        <div className="flex justify-center pt-2 rounded-lg overflow-hidden">
                            <button onClick={()=>handleRegister()} className="text-second rounded-lg p-2 pl-5 pr-5 border-4 hover:border-second hover:shadow-md ease-in-out duration-500">Register</button>
                        </div>
                    }
                    <ToastContainer />
                </div>
            </div>
        ) : window.location.href = "../account"
    );
};

export default Auth;