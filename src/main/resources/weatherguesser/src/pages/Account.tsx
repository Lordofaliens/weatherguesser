import React, {useEffect, useState} from 'react';
import Box from '@mui/material/Box';
import Input from '@mui/material/Input';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import FormControl from '@mui/material/FormControl';
import TextField from '@mui/material/TextField';
import { v4 as uuidv4 } from 'uuid';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {getUserData} from "../controllers/getters/userDataGetter";
import {setUserName} from "../controllers/setters/userNameSetter";
import {setUserPassword} from "../controllers/setters/userPasswordSetter";
import {setUserEmail} from "../controllers/setters/userEmailSetter";
import {Link} from "react-router-dom";
import {styled} from "@mui/material";
import AccountInput from '../components/accountInput';

const Account: React.FC = () => {
    const [Name, setName] = useState<string>("");
    const [Password, setPassword] = useState<string>("");
    const [Email, setEmail] = useState<string>("");
    const [rating, setRating] = useState<number>(0);
    const [accuracy, setAccuracy] = useState<number>(0);
    const [totalGuesses, setTotalGuesses] = useState<number>(0);
    const [highStreak, setHighStreak] = useState<number>(0);
    const [currentStreak, setCurrentStreak] = useState<number>(0);

    const [editState, setEditState] = useState<boolean[]>([false,false,false]);
    const [showPassword, setShowPassword] = useState<boolean>(false);

    useEffect(() => {
        async function storeUser() {
            if(localStorage.getItem("token")) {
                const u = getUserData(JSON.parse(localStorage.getItem("token") as string));

                try {
                    const userInstance = await u;

                    if (userInstance) {
                        setName(userInstance.name);
                        setPassword(userInstance.password);
                        setEmail(userInstance.email);
                        setRating(userInstance.rating);
                        setAccuracy(userInstance.accuracy);
                        setTotalGuesses(userInstance.totalGuesses);
                        setHighStreak(userInstance.highStreak);
                        setCurrentStreak(userInstance.currentStreak);
                    } else {
                        // Handle the case where user is undefined
                    }
                } catch (error) {
                    // Handle any errors that might occur
                }
            }
        }
        storeUser();
    }, []);

    const handleChangeEdit = async (category : string) => {
        console.log("edit pressed")
        const updatedEditState = [...editState];
        switch (category) {
            case "Name":
                if(updatedEditState[0]) {
                    const newName = (document.getElementById("accountNameInput") as HTMLInputElement).value;
                    if(Name!==newName) {
                        if(await setUserName(JSON.parse(localStorage.getItem("token") as string), newName)!==1)setName(newName);
                    }
                }
                updatedEditState[0] = !updatedEditState[0];
                setEditState(updatedEditState);
                break;
            case "Password":
                if(updatedEditState[1]) {
                    const newPassword = (document.getElementById("accountPasswordInput") as HTMLInputElement).value;
                    if (Password !== newPassword) {
                        if(await setUserPassword(JSON.parse(localStorage.getItem("token") as string), newPassword)!==1) setPassword(newPassword);
                    }
                }
                updatedEditState[1] = !updatedEditState[1];
                setEditState(updatedEditState);
                break;
            case "Email":
                if(updatedEditState[2]) {
                    const newEmail = (document.getElementById("accountEmailInput") as HTMLInputElement).value;
                    if(Password!==newEmail) {
                        if(await setUserEmail(JSON.parse(localStorage.getItem("token") as string), newEmail)!==1) setEmail(newEmail);
                    }
                }
                updatedEditState[2] = !updatedEditState[2];
                setEditState(updatedEditState);
                break;
        }
    }

    const handleShowPassword = () => {
        setShowPassword(!showPassword);
    }

    const fields = ["Name", "Password", "Email"];

    const fieldStateMapping = [Name, Password, Email];
    const successGuess = totalGuesses * accuracy / 100;



    return (
        <div className="gradient-bg w-screen h-screen bg-gradient-to-r from-pink-300 via-pink-200 to-blue-200 animate-gradient flex flex-col items-center justify-center">
            {localStorage.getItem("token")==null ?
                (
                    window.location.href="../auth"
                )
                :
                (<div className="bg-white rounded-lg w-account min-w-account max-h-account p-5 m-4 shadow-md">
                    <p className="gradient-bg font-black text-2xl text-transparent bg-clip-text bg-gradient-to-r from-blue-300 via-blue-200 to-pink-200 inset-0 text-center">YOUR DATA</p>
                    {fields.map((field: string) => (
                        <div key={uuidv4()} className="justify-between flex text-gray-300 items-center m-3">
                            <span className="gradient-bg font-black text-transparent bg-clip-text bg-gradient-to-r from-blue-300 via-blue-200 to-pink-200 inset-0">
                                {field}
                            </span>
                            <div className="flex">
                                {editState[fields.indexOf(field)] ?
                                    <AccountInput>
                                        <TextField  placeholder={`type new ${field.toLowerCase()}`} label={fieldStateMapping[fields.indexOf(field)]} id={`account${field}Input`} InputProps={{ disableUnderline: true }} />
                                    </AccountInput>
                                    // <input className="bg-gradient-to-r from-pink-300 via-pink-200 to-blue-200 focus:outline-none focus:ring focus:border-blue-300 placeholder-white m-1 mr-3 text-white rounded-lg" id={`account${field}Input`} placeholder={fieldStateMapping[fields.indexOf(field)]} />
                                    :
                                    <span className="text-gray-300 mr-3">{ field==="Password"&&!showPassword ? "*******" : fieldStateMapping[fields.indexOf(field)]}</span>
                                }
                                {field==="Password"&&!editState[fields.indexOf(field)] && (
                                    <button className="text-gray-300 mr-3" onClick={()=>handleShowPassword()}>
                                        {!showPassword?"O":"C"}
                                    </button>)
                                }
                                <button  onClick={()=>handleChangeEdit(`${field}`)} className="pl-4">
                                    {editState[fields.indexOf(field)]?
                                        <span className="text-green-300">Apply</span>
                                        :
                                        <span className="text-yellow-300">Edit</span>
                                    }
                                </button>
                            </div>
                        </div>
                    ))}
                        <div key={uuidv4()} className="justify-end flex text-gray-300 items-center m-3">
                            <Link to={"../logout"} className="text-red-500 text-end">Logout</Link>
                        </div>

                </div>)
            }
            {localStorage.getItem("token")!=null && (<div className="w-account min-w-account max-h-account bg-white rounded-lg justify-between p-5 shadow-md">
                <p className="gradient-bg font-black text-2xl text-transparent bg-clip-text bg-gradient-to-r from-blue-300 via-blue-200 to-pink-200 inset-0 text-center">YOUR STATS</p>
                <div className="justify-between flex">
                    <span className="gradient-bg font-black text-transparent bg-clip-text bg-gradient-to-r from-blue-300 via-blue-200 to-pink-200 inset-0">Rating</span>
                    <span className="text-gray-200">{rating}</span>
                </div>
                <div className="justify-between flex">
                    <span className="gradient-bg font-black text-transparent bg-clip-text bg-gradient-to-r from-blue-300 via-blue-200 to-pink-200 inset-0">HighStreak</span>
                    <span className="text-gray-200">{highStreak}</span>
                </div>
                <div className="justify-between flex">
                    <span className="gradient-bg font-black text-transparent bg-clip-text bg-gradient-to-r from-blue-300 via-blue-200 to-pink-200 inset-0">CurrentStreak</span>
                    <span className="text-gray-200">{currentStreak}</span>
                </div>
                <div className="justify-between flex">
                    <span className="gradient-bg font-black text-transparent bg-clip-text bg-gradient-to-r from-blue-300 via-blue-200 to-pink-200 inset-0">Accuracy</span>
                    <span className="text-gray-200">{accuracy} %</span>
                </div>
                <div className="justify-between flex">
                    <span className="gradient-bg font-black text-transparent bg-clip-text bg-gradient-to-r from-blue-300 via-blue-200 to-pink-200 inset-0">Total Guesses</span>
                    <span className="text-gray-200">{totalGuesses}</span>
                </div>
                <div className="justify-between flex">
                    <span className="gradient-bg font-black text-transparent bg-clip-text bg-gradient-to-r from-blue-300 via-blue-200 to-pink-200 inset-0">Correct Guesses</span>
                    <span className="text-gray-200">{successGuess}</span>
                </div>
            </div>)}
            <ToastContainer />
        </div>
    );
};

export default Account;