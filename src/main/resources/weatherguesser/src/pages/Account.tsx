import React, {useState} from 'react';
import TextField from '@mui/material/TextField';
import { v4 as uuidv4 } from 'uuid';
import axios from 'axios';
import {toast, ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {setUserName} from "../controllers/setters/userNameSetter";
import {setUserPassword} from "../controllers/setters/userPasswordSetter";
import {Link, useNavigate} from "react-router-dom";
import AccountInput from '../contexts/accountInput';
import {useHomeContext} from "../contexts/HomeContext";
import UniquenessHandler from "../handlers/uniquenessHandler";

const Account: React.FC = () => {
    const {
        name,
        setName,
        password,
        setPassword,
        email,
        rating,
        accuracy,
        highStreak,
        currentStreak,
        totalGuesses
    } = useHomeContext();
    const uniquenessHandlerInstance = new UniquenessHandler();
    const navigate = useNavigate();
    if(!localStorage.getItem("token")) navigate("../auth");

    const [editState, setEditState] = useState<boolean[]>([false,false,false]);

    const fields = ["Name", "Password", "Email"];
    const fieldStateMapping = [name, password, email];
    const successGuess = totalGuesses * accuracy / 100;

    const handleChangeEdit = async (category : string) => {
        const updatedEditState = [...editState];
        switch (category) {
            case "Name":
                if(updatedEditState[0]) {
                    const newName = (document.getElementById("accountNameInput") as HTMLInputElement).value;
                    if(name!==newName) {
                        if(await setUserName(JSON.parse(localStorage.getItem("token") as string), newName)!==1)setName(newName);
                    }
                }
                updatedEditState[0] = !updatedEditState[0];
                setEditState(updatedEditState);
                break;
            case "Password":
                if(updatedEditState[1]) {
                    const newPassword = (document.getElementById("accountPasswordInput") as HTMLInputElement).value;
                    if (password !== newPassword) {
                        if(await setUserPassword(JSON.parse(localStorage.getItem("token") as string), newPassword)!==1) setPassword(newPassword);
                    }
                }
                updatedEditState[1] = !updatedEditState[1];
                setEditState(updatedEditState);
                break;
            case "Email":
                if(updatedEditState[2]) {
                    const newEmail = (document.getElementById("accountEmailInput") as HTMLInputElement).value;
                    const emailCheck = await uniquenessHandlerInstance.checkEmail(newEmail);
                    if(emailCheck!=="Success!") {
                        toast.error(emailCheck, {
                            position: toast.POSITION.TOP_CENTER,
                            draggablePercent: 50,
                            role: "alert",
                            autoClose: 6000
                        })
                    } else if(email!==newEmail) {
                        toast.success(`Check ${newEmail}!`, {
                            position: toast.POSITION.TOP_CENTER,
                            draggablePercent: 50,
                            role: "alert",
                            autoClose: 6000
                        })
                        try {
                            const response = await axios.post(`http://localhost:5000/data-reset/requestEmail?oldEmail=${email}&newEmail=${newEmail}`);
                            console.log(response.data);
                        } catch (error) {
                            console.error(error);
                        }
                    }
                }
                updatedEditState[2] = !updatedEditState[2];
                setEditState(updatedEditState);
                break;
        }
    }

    const handleResetPassword = async () => {
        toast.success(`Check ${email}!`, {
            position: toast.POSITION.TOP_CENTER,
            draggablePercent: 50,
            role: "alert",
            autoClose: 6000
        })
        try {
            const response = await axios.post(`http://localhost:5000/data-reset/requestPassword?email=${email}`);
            console.log(response.data);
        } catch (error) {
            console.error(error);
        }
    }

    function logout() {
        localStorage.removeItem("token");
        navigate('../auth');
    }

    return (
        <div className="animate-gradientAnimation w-screen h-screen bg-gradient-to-r from-logo to-second animate-gradient flex flex-col items-center justify-center" style={{backgroundSize:'400% 400%'}}>
            <div className={"m-2 p-2 bg-white rounded-lg shadow-md flex justify-center items-center hover:scale-150 ease-in-out duration-300 cursor-pointer"}>
                <Link to={`../home`}>
                    <img alt="dark logo" src={"./icons/logodark.png"} className={"w-footerIcon"}/>
                </Link>
            </div>
            {localStorage.getItem("token")==null ?
                (
                    window.location.href="../auth"
                )
                :
                (<div className="bg-white rounded-lg w-account min-w-account max-h-account p-5 m-4 shadow-md">
                    <p className="animate-gradientAnimation font-black text-2xl text-transparent bg-clip-text bg-gradient-to-r from-second to-logo inset-0 text-center" style={{backgroundSize:'400% 400%'}}>YOUR DATA</p>
                    {fields.map((field: string) => (
                        <div key={uuidv4()} className="justify-between flex text-gray-300 items-center m-3">
                            <span className="animate-gradientAnimation font-black  bg-clip-text text-second inset-0" style={{backgroundSize:'400% 400%'}}>
                                {field}
                            </span>
                            <div className="flex items-center">
                                {editState[fields.indexOf(field)] ?
                                    <AccountInput>
                                        <TextField  placeholder={`type new ${field.toLowerCase()}`} label={fieldStateMapping[fields.indexOf(field)]} id={`account${field}Input`} />
                                    </AccountInput>
                                    :
                                    <span className="text-gray-300 mr-3">{ field==="Password" ? "*******" : fieldStateMapping[fields.indexOf(field)]}</span>
                                }
                                {field==="Password"&&!editState[fields.indexOf(field)] && <button onClick={()=>handleResetPassword()} className="pl-4 text-yellow">Edit</button>}
                                {field!=="Password" &&
                                    <button  onClick={()=>handleChangeEdit(`${field}`)} className="pl-4">
                                        {editState[fields.indexOf(field)]?
                                            <span className="text-green">Apply</span>
                                            :
                                            <span className="text-yellow">Edit</span>
                                        }
                                    </button>
                                }
                            </div>
                        </div>
                    ))}
                        <div key={uuidv4()} className="justify-end flex text-gray-300 items-center m-3">
                            <button onClick={()=>logout()} className="text-red text-end">Logout</button>
                        </div>

                </div>)
            }
            {localStorage.getItem("token")!=null && (<div className="w-account min-w-account max-h-account bg-white rounded-lg justify-between p-5 shadow-md m-4">
                <p className="animate-gradientAnimation font-black text-2xl text-transparent bg-clip-text bg-gradient-to-r from-second to-logo inset-0 text-center" style={{backgroundSize:'400% 400%'}}>YOUR STATS</p>
                <div className="justify-between flex">
                    <span className="animate-gradientAnimation font-black text-second inset-0" style={{backgroundSize:'400% 400%'}}>Rating</span>
                    <span className="text-gray-300">{rating}</span>
                </div>
                <div className="justify-between flex">
                    <span className="animate-gradientAnimation font-black text-second inset-0" style={{backgroundSize:'400% 400%'}}>HighStreak</span>
                    <span className="text-gray-300">{highStreak}</span>
                </div>
                <div className="justify-between flex">
                    <span className="animate-gradientAnimation font-black text-second inset-0" style={{backgroundSize:'400% 400%'}}>CurrentStreak</span>
                    <span className="text-gray-300">{currentStreak}</span>
                </div>
                <div className="justify-between flex">
                    <span className="animate-gradientAnimation font-black text-second inset-0" style={{backgroundSize:'400% 400%'}}>Accuracy</span>
                    <span className="text-gray-300">{accuracy} %</span>
                </div>
                <div className="justify-between flex">
                    <span className="animate-gradientAnimation font-black text-second inset-0" style={{backgroundSize:'400% 400%'}}>Total Guesses</span>
                    <span className="text-gray-300">{totalGuesses}</span>
                </div>
                <div className="justify-between flex">
                    <span className="animate-gradientAnimation font-black text-second inset-0" style={{backgroundSize:'400% 400%'}}>Correct Guesses</span>
                    <span className="text-gray-300">{successGuess}</span>
                </div>
            </div>)}
            <ToastContainer />
        </div>
    );
};

export default Account;