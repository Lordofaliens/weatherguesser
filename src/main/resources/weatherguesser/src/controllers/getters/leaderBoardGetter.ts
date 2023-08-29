import axios from 'axios';
import user from "../../interfaces/IUser";

export const getLeaderBoard = async () => {
    try {
        const response = await axios.get<user[]>(`http://localhost:5000/api/leaderboard/getLeaderBoard`);
        return response.data;
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}