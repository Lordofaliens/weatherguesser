import React, {useEffect, useState} from 'react';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Account: React.FC = () => {

    return (
        <div>
            <a href={"../home"}>Home</a>
            {localStorage.getItem("token")==null?<a href={"../login"}>Login</a>:<a href={"../logout"}>Logout</a>}
            <a href={"../register"}>Register</a>
            <ToastContainer />
        </div>
    );
};

export default Account;