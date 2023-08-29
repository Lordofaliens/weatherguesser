import axios from 'axios';
import user from "../../interfaces/IUser";
import {toast} from "react-toastify";
import UniquenessHandler from "../../handlers/uniquenessHandler";

export const setUserEmail = async (token : string, email: string) => {
    try {
        const uniquenessHandlerInstance = new UniquenessHandler();
        const headers = {
            Authorization: `Bearer ${token}`
        };
        const data = {
            email: email
        };
        const check1 = await uniquenessHandlerInstance.checkEmail(email);
        if(check1!=="Success!") {
            toast.dismiss();
            toast.error(check1, {
                position: toast.POSITION.TOP_CENTER,
                draggablePercent: 50,
                role: "alert"
            })
            return 1;
        } else {
            const response = await axios.post<user>(`http://localhost:5000/api/user/changeEmail`, data, {headers});
            toast.success(`Your email changed to ${email} successfully!`, {
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