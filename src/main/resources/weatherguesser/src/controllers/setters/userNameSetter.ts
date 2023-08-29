import axios from 'axios';
import user from "../../interfaces/IUser";
import {toast} from "react-toastify";
import UniquenessHandler from "../../handlers/uniquenessHandler";

export const setUserName = async (token : string, name: string) => {
        try {
            const uniquenessHandlerInstance = new UniquenessHandler();
            const headers = {
                Authorization: `Bearer ${token}`
            };
            const data = {
                name: name
            };
            const check1 = await uniquenessHandlerInstance.checkUsername(name);
            if(check1!=="Success!") {
                toast.dismiss();
                toast.error(check1, {
                    position: toast.POSITION.TOP_CENTER,
                    draggablePercent: 50,
                    role: "alert"
                })
                return 1;
            } else {
                const response = await axios.post<user>(`http://localhost:5000/api/user/changeName`, data, {headers});
                toast.success(`Your name changed to ${name} successfully!`, {
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