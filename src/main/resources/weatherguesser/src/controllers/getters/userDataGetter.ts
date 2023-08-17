import React, {useEffect, useState, useRef} from 'react';
import axios from 'axios';
import user from "../../contexts/userContext";


export const getUserData = async (token : string) => {
    try {
        const headers = {
            Authorization: `Bearer ${token}`
        };
        const response = await axios.get<user>(`http://localhost:5000/api/user/getData`,{headers});
        return response.data;
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}