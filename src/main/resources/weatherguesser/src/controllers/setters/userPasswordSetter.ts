import React, {useEffect, useState} from 'react';
import axios from 'axios';
import user from "../../contexts/userContext";
import UniquenessHandler from "../../handlers/uniquenessHandler";
import {toast} from "react-toastify";
export const setUserPassword = async (token : string, password: string) => {
    try {
        const uniquenessHandlerInstance = new UniquenessHandler();
        console.log(`Bearer ${token}`)
        const headers = {
            Authorization: `Bearer ${token}`
        };
        const data = {
            password: password
        };
        const check1 = await uniquenessHandlerInstance.checkPassword(password);
        if(check1!=="Success!") {
            toast.dismiss();
            toast.error(check1, {
                position: toast.POSITION.TOP_CENTER,
                draggablePercent: 50,
                role: "alert"
            })
            return 1;
        } else {
            const response = await axios.post<user>(`http://localhost:5000/api/user/changePassword`, data, {headers});
            console.log(response.data);
            toast.success(`Your password changed to ${password} successfully!`, {
                position: toast.POSITION.TOP_CENTER,
                draggablePercent: 50,
                autoClose: 3000,
                role: "alert"
            });
            return response.data;
        }
    } catch (error) {
        console.error('Error fetching data:', error);
        return 1;
    }
}