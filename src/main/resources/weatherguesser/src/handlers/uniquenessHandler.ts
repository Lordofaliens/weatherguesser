import {toast} from "react-toastify";
import React, {useEffect, useState} from 'react';
import axios from 'axios';

class UniquenessHandler {
    constructor() {}

    checkUsername = async (username : string) => {
        if(username.length >= 4) {
            try {
                const response = await axios.get<string>(`http://localhost:5000/api/user/uniqueUsername?name=${username}`);
                console.log("USERNAME UNIQUENESS"+response)
                if(response.data=="1") {
                    toast.error("Username is not unique!", {
                        position: toast.POSITION.TOP_CENTER,
                        draggablePercent: 50,
                        role: "alert"
                    });
                } else {
                    console.log('Username unique!');
                    return "Success!";
                }
            } catch (error) {
                console.error('Username check failed:', error);
                // Handle the error here
            }
        } else return "Username too short. Make it at least 4 symbols!";
    }


    checkPassword = async (password : string) => {
        const specialSymbolRegex = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/;
        if(password.length >= 8) {
            if(specialSymbolRegex.test(password)) {
                if(/[A-Z]/.test(password)) {
                    if(/[0-9]/.test(password)) {
                        console.log('Password nice!');
                        return "Success!";
                    } else return "Password should contain numbers!";
                } else return "Password should contain uppercase letters!";
            } else return "Password should contain special symbols!";
        } else return "Password too short. Make it at least 8 symbols!";
    }

    checkEmail = async (email : string) => {
        if(email.length >= 5) {
            if(email.indexOf("@")!=-1&&email.indexOf(".")!=-1&&email.indexOf("@")<email.indexOf(".")) {
                try {
                    const response = await axios.get<string>(`http://localhost:5000/api/user/uniqueEmail?email=${email}`);
                    console.log("EMAIL UNIQUENESS"+response)
                    if(response.data=="1") {
                        toast.error("Email is not unique!", {
                            position: toast.POSITION.TOP_CENTER,
                            draggablePercent: 50,
                            role: "alert"
                        });
                    } else {
                        console.log('Email unique!');
                        return "Success!";
                    }
                } catch (error) {
                    console.error('Email check failed:', error);
                    // Handle the error here
                }
            } else return "Email should contain '@' and '.'!";
        } else return "Email too short. Make it at least 5 symbols!";
    }
}

export default UniquenessHandler;