import React, {useEffect, useState} from 'react';
import axios from 'axios';
import Scale from "../components/main/scale/scale";
import Digit from "../components/main/digit/digit";
import Slider from "../components/main/slider/slider";
import DifficultyBtns from "../components/main/difficultyBtns/difficultyBtns";
import GuessBtns from "../components/main/guessBtns/guessBtns";
import {getUserData} from "../controllers/getters/userDataGetter";
import user from "../contexts/userContext";
import LeaderBoard from "../components/leaderBoard";
import {getLeaderBoard} from "../controllers/getters/leaderBoardGetter";
import {getDailyChallenge} from "../controllers/getters/dailyChallengeGetter";
import DailyChallenge from "../components/main/dailyChallenge/dailyChallenge";

const Home: React.FC = () => {
    const [leaderBoard, setLeaderBoard] = useState<user[]>([]);
    const [dailyChallenge, setDailyChallenge] = useState<string[]>([]);
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
        async function storeDailyChallenge() {
            const dc = await getDailyChallenge();
            if(dc!==undefined) setDailyChallenge(dc);
        }
        storeLeaderBoard();
        storeDailyChallenge();
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

    const [photoUrl, setPhotoUrl] = useState<string | null>(null);

    const fetchPhoto = () => {
        axios.get(`http://localhost:5000/api/photo/get?city=${city}`, { responseType: 'arraybuffer' })
            .then(response => {
                const imageBlob = new Blob([response.data], { type: 'image/jpeg' });
                setPhotoUrl(URL.createObjectURL(imageBlob));
            })
            .catch(error => {
                console.error('Error fetching photo:', error);
            });
    };
    useEffect(() => {
        fetchPhoto();
    }, [city]);
    return (
        <div
            style={{
                width: "100vw",
                height: "100vh",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                backgroundImage: photoUrl ? `url(${photoUrl})` : "",
                backgroundSize: "cover",
                backgroundPosition: "center",
                transition: "background-image 0.5s ease",
                zIndex: "1"
            }}
        >
            <div
                style={{
                    position: "absolute",
                    top: 0,
                    left: 0,
                    width: "100%",
                    height: "100%",
                    backgroundColor: "rgba(0, 0, 0, 0.4)", // 20% darker overlay
                    zIndex: "0"
                }}
            />
            <div style={{
                zIndex: "2",
                display: "flex",
                flexDirection: "column",
                alignItems: "center"
            }}>
                <h1>WeatherGuesser</h1>
                <DailyChallenge
                    currentStreak={currentStreak}
                    city={dailyChallenge[0]}
                    difficulty={dailyChallenge[1]}
                    onChangeDifficulty={async () =>
                        await handleDifficultyChange(dailyChallenge[1])
                    }
                    onChangeCity={async () => await handleCityChange(dailyChallenge[0])}
                />
                <div style={{display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    justifyContent: "center",
                    marginTop: "80px"
                }}>
                    <GuessBtns guessed={guessed} onChangeGuess={handleGuessChange} />
                    <Scale accuracy={Math.PI + (Math.PI * accuracy / 100)} />
                    <Digit accuracy={accuracy} />

                </div>

                <Slider
                    dailyCity={dailyChallenge[0]}
                    city={city}
                    difficulty={difficulty}
                    onChangeCity={handleCityChange}
                />

                <p>Current difficulty: {difficulty}</p>
                <DifficultyBtns
                    onChangeCity={handleCityChange}
                    onChangeDifficulty={handleDifficultyChange}
                />
                <LeaderBoard users={leaderBoard} />
            </div>

        </div>
    );
};

export default Home;