import React, {useEffect, useState} from 'react';
import axios from 'axios';
import { v4 as uuidv4 } from 'uuid';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {getUserData} from "../controllers/getters/userDataGetter";
import {setUserName} from "../controllers/setters/userNameSetter";
import {setUserPassword} from "../controllers/setters/userPasswordSetter";
import {setUserEmail} from "../controllers/setters/userEmailSetter";
import {Link} from "react-router-dom";

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
    const successGuess = totalGuesses * accuracy / 100; //change to variables
    return (
        <div>
            {localStorage.getItem("token")==null ?
                (<div>
                    <Link to={"../login"}>Login</Link>
                    <Link to={"../register"}>Register</Link>
                </div>)
                :
                (<div>
                    {fields.map((field: string) => (
                        <div key={uuidv4()}>
                            <p>{field}</p>
                            {editState[fields.indexOf(field)] ? <input id={`account${field}Input`} placeholder={fieldStateMapping[fields.indexOf(field)]} /> : <p>{ field==="Password"&&!showPassword ? "*******" : fieldStateMapping[fields.indexOf(field)]}</p>}
                            {field==="Password"&&!editState[fields.indexOf(field)] && (<button onClick={()=>handleShowPassword()}>{!showPassword?"Open":"Close"}</button>) }
                            <button onClick={()=>handleChangeEdit(`${field}`)}>{editState[fields.indexOf(field)]?<span>Apply</span>:<span>Edit</span>}</button>
                        </div>
                    ))}
                    <Link to={"../logout"}>Logout</Link>
                </div>)
            }
            <div>
                <p>Your stats</p>
                <p>Rating</p>
                {rating}
                <p>HighStreak</p>
                {highStreak}
                <p>CurrentStreak</p>
                {currentStreak}
                <p>Accuracy</p>
                {accuracy} %
                <p>Total Guesses</p>
                {totalGuesses}
                <p>Successful Guesses</p>
                {successGuess}
            </div>

            <Link to={"../home"}>Home</Link>
            <ToastContainer />
        </div>
    );
};

export default Account;