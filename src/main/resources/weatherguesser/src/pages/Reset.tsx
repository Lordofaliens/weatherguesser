import React, {useEffect, useState} from "react";
import axios from 'axios';
import {IResetProps} from "../interfaces/IProps";
import TextField from "@mui/material/TextField";
import ResetInput from "../contexts/resetInput";
import {useLocation, useNavigate} from "react-router-dom";
import {toast, ToastContainer} from "react-toastify";
import UniquenessHandler from "../handlers/uniquenessHandler";

const Reset: React.FC<IResetProps> = ({type}) => {
    const navigate = useNavigate();
    const uniquenessHandlerInstance = new UniquenessHandler();
    const location = useLocation();
    const [token, setToken] = useState<string | null>("");
    const [email, setEmail] = useState<string | null>("");

    useEffect(() => {
        const searchParams = new URLSearchParams(location.search);
        setToken(searchParams.get('token'));
        setEmail(searchParams.get('email'));
    }, [location.search]);

    useEffect(() => {
        async function finishRegister() {
            if(type==="verify"&&token!=="") {
                try{
                    const response = await axios.post(`http://localhost:5000/api/user/register?token=${token}`);
                    localStorage.setItem("token", JSON.stringify(response.data));
                    toast.dismiss();
                    toast.success("Email is verified successfully!", {
                        position: toast.POSITION.TOP_CENTER,
                        draggablePercent: 50,
                        role: "alert"
                    })
                    setTimeout(()=>{navigate("../home")},6000)
                } catch(error) {
                    toast.error("Invalid or expired token. Register once again!", {
                        position: toast.POSITION.TOP_CENTER,
                        draggablePercent: 50,
                        role: "alert"
                    })
                }
            }
        }
        finishRegister();
    }, [token]);

    useEffect(() => {
        if(type==="email"&&email!=="") {
            try {
                const response = axios.post(`http://localhost:5000/data-reset/receiveEmail?token=${token}&email=${email}`);
                toast.dismiss();
                toast.success("Email is changed successfully!", {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    role: "alert"
                })
                setTimeout(()=>{navigate("../home")},6000)
            } catch (error) {
                toast.error("Invalid or expired token. Request an email once again!", {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    role: "alert"
                })
            }
        }
    }, [email]);

    const sendNewPassword = async () => {
        const password = (document.getElementById("passwordResetInput") as HTMLInputElement).value;
        try {
            const check1 = await uniquenessHandlerInstance.checkPassword(password);
            console.log(check1)
            if(check1!=="Success!") {
                toast.dismiss();
                toast.error(check1, {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    role: "alert"
                })
                return 1;
            }
        } catch (error) {
            console.error(error);
        }
        try {
            const response = await axios.post(`http://localhost:5000/data-reset/receivePassword?token=${token}&password=${password}`);
            (document.getElementById("passwordResetInput") as HTMLInputElement).value = "";
            toast.dismiss();
            toast.success("Password is changed successfully!", {
                position: toast.POSITION.TOP_CENTER,
                draggablePercent: 50,
                role: "alert"
            })
            setTimeout(()=>{navigate("../home")},6000)
            return 0;
        } catch (error) {
            toast.error("Invalid or expired token. Request an email once again!", {
                position: toast.POSITION.TOP_CENTER,
                draggablePercent: 50,
                role: "alert"
            })
        }
    }

    return (
        <div className="animate-gradientAnimation w-screen h-screen bg-gradient-to-r from-logo to-second animate-gradient flex flex-col items-center justify-center" style={{backgroundSize:'400% 400%'}}>
            <div className="bg-white rounded-lg w-auto min-w-account max-h-account p-5 m-4 shadow-md flex flex-col">
            {type==="password" &&
               <>
                   <p className="animate-gradientAnimation font-black text-2xl text-transparent bg-clip-text bg-gradient-to-r from-second to-logo inset-0 text-center m-3" style={{backgroundSize:'400% 400%'}}>{type.toUpperCase()} RESET</p>
                   <ResetInput className="mb-3 mt-3 flex justify-center relative h-footerIcon w-full">
                       <TextField type={"text"}  id={`${type}ResetInput`} placeholder={`type new ${type}`} />
                   </ResetInput>
                   <button onClick={()=>sendNewPassword()} className="text-second rounded-lg border-4 hover:border-second hover:shadow-md ease-in-out duration-500">Reset</button>
               </>
            }
            {type==="email" &&
                <p className="font-black text-2xl text-second text-center m-3">Email change request is being proceeded.</p>
            }
            {type==="verify" &&
                <p className="font-black text-2xl text-second text-center m-3">Email is being verified.</p>
            }
            </div>
            <ToastContainer />
        </div>
    )
}

export default Reset;