import axios from 'axios';

export const getDailyChallenge = async () => {
    try {
        const response = await axios.get<string[]>(`http://localhost:5000/api/leaderboard/getDailyChallenge`);
        return response.data;
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}