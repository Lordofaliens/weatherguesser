import React, {useEffect, useState, useRef} from 'react';
import axios from 'axios';
import Scale from "../components/accuracy/scale/scale";
import Digit from "../components/accuracy/digit/digit";
import Slider from "../components/accuracy/slider/slider";
import DifficultyBtns from "../components/accuracy/difficultyBtns/difficultyBtns";
import GuessBtns from "../components/accuracy/guessBtns/guessBtns";
import {getUserData} from "../controllers/getters/userDataGetter";
import user from "../contexts/userContext";
import LeaderBoard from "../components/leaderBoard";
import {getLeaderBoard} from "../controllers/getters/leaderBoardGetter";

const Home: React.FC = () => {
    const [leaderBoard, setLeaderBoard] = useState<user[]>([]);
    const [difficulty, setDifficulty] = useState<string>("easy");
    const [guess, setGuess] = useState<string>("");
    const [guessed, setGuessed] = useState<string>("");
    const [guesses, setGuesses] = useState<string[]>([]);
    const [email, setEmail] = useState<string>("");
    const [rating, setRating] = useState<number>(0);
    const [accuracy, setAccuracy] = useState<number>(0);
    const [highStreak, setHighStreak] = useState<number>(0);
    const [currentStreak, setCurrentStreak] = useState<number>(0);
    const [city, setCity] = useState<string>("New York");
    const handleCityChange = (newCity: string) => {setCity(newCity)}
    const handleDifficultyChange = (newDifficulty: string) => {setDifficulty(newDifficulty)}
    const handleGuessChange = (newGuess: string) => { setGuess(newGuess)}

    useEffect(() => {
        async function storeLeaderBoard() {
            const lb = await getLeaderBoard();
            if(lb!==undefined) setLeaderBoard(lb);
        }
        async function storeUser() {
            if(localStorage.getItem("token")) {
                const u = getUserData(JSON.parse(localStorage.getItem("token") as string));

                try {
                    const userInstance = await u;

                    if (userInstance) {
                        setEmail(userInstance.email);
                        setRating(userInstance.rating);
                        setAccuracy(userInstance.accuracy);
                        setHighStreak(userInstance.highStreak);
                        setCurrentStreak(userInstance.currentStreak);
                        setGuesses(userInstance.guess);
                    } else {
                        // Handle the case where user is undefined
                    }
                } catch (error) {
                    // Handle any errors that might occur
                }
            }
        }
        storeLeaderBoard();
        storeUser();
    }, []);

    useEffect(() => {
        const matchingGuess = guesses.find(guess => guess.includes(city));

        if (matchingGuess) {
            const parts = matchingGuess.split(";");
            if (parts.length >= 2) setGuessed(parts[1])
        } else setGuessed("");
    }, [city, guesses]);

    useEffect( () => {
        makeGuess();
    }, [guess]);

    const makeGuess = async () => {
        if (guess !== "") {
            console.log("hook is triggered", guess, city);
            const headers = {
                Authorization: `Bearer ${JSON.parse(localStorage.getItem("token") as string)}`
            };
            const data = {
                guess: guess,
                city: city
            };

            try {
                const response = await axios.post<string[]>("http://localhost:5000/api/user/makeGuess", data, { headers });
                // Add error handling if needed
                setGuesses(response.data);
            } catch (error) {
                // Handle error here
                console.error("An error occurred:", error);
            }
        }
        return;
    };



    return (
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
            <h1>WeatherGuesser</h1>
            <Scale accuracy={Math.PI + (Math.PI * accuracy/100)} />
            <Digit accuracy={accuracy} />
            <Slider difficulty={difficulty} onChangeCity={handleCityChange}/>
            <GuessBtns guessed={guessed} onChangeGuess={handleGuessChange} />
            <p>Current difficulty: {difficulty}</p>
            <DifficultyBtns onChangeDifficulty={handleDifficultyChange} />
            <LeaderBoard users={leaderBoard}/>
        </div>
    );
};

export default Home;