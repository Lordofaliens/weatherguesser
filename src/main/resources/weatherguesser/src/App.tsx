import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import Scale from "./components/accuracy/scale/scale";
import Slider from "./components/accuracy/slider/slider";
import DifficultyBtns from "./components/accuracy/difficultyBtns/difficultyBtns";
import Digit from "./components/accuracy/digit/digit";

interface user {
    name: string;
    location: string;
    accuracy: number;
    registration: Date;
    rating : number;
    currentStreak: number;
    highStreak: number;
    email: string;
    userId: string;
    guess: string[];
}

function App() {
    const [difficulty, setDifficulty] = useState<string>("easy");
    const [accuracy, setAccuracy] = useState<number>(0);
    const handleDifficultyChange = (newDifficulty: string) => {
        setDifficulty(newDifficulty);
    };
    useEffect(() => {
        getUserData('o89483fjsdb43');
    }, []);


    const getUserData = async (userId : string) => {
        try {
            const response = await axios.get<user>(`http://localhost:5000/api/user/${userId}`);
            console.log(response);
            setAccuracy(response.data.accuracy);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    }

    return (
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
            <h1>WeatherGuesser</h1>
            <Scale accuracy={Math.PI + (Math.PI * accuracy/100)} />
            <Digit accuracy={accuracy} />
            <Slider difficulty={difficulty}/>
            <p>Current difficulty: {difficulty}</p>
            <DifficultyBtns onChangeDifficulty={handleDifficultyChange} />
        </div>
    );
}

export default App;
