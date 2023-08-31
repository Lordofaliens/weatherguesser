import React, {createContext, useContext, useState, useEffect, Dispatch, SetStateAction} from 'react';
import axios from 'axios';
import user from "../interfaces/IUser";
import {getLeaderBoard} from "../controllers/getters/leaderBoardGetter";
import {getUserData} from "../controllers/getters/userDataGetter";
import {getDailyChallenge} from "../controllers/getters/dailyChallengeGetter";
import IHomeContextType from "../interfaces/IHomeContextType";

const HomeContext = createContext<IHomeContextType | undefined>(undefined);

export const useHomeContext = () => {
    const context = useContext(HomeContext);
    if (!context) {
        throw new Error('useHomeContext must be used within a HomeProvider');
    }
    return context;
};

export const HomeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [leaderBoard, setLeaderBoard] = useState<user[]>([]);
    const [dailyChallenge, setDailyChallenge] = useState<string[]>([]);
    const [difficulty, setDifficulty] = useState<string>("easy");
    const [guess, setGuess] = useState<string>("");
    const [guessed, setGuessed] = useState<string>("");
    const [guesses, setGuesses] = useState<string[]>([]);
    const [name, setName] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [email, setEmail] = useState<string>("");
    const [rating, setRating] = useState<number>(0);
    const [accuracy, setAccuracy] = useState<number>(0);
    const [highStreak, setHighStreak] = useState<number>(0);
    const [currentStreak, setCurrentStreak] = useState<number>(0);
    const [totalGuesses, setTotalGuesses] = useState<number>(0);
    const [city, setCity] = useState<string>("New York");
    const [backgroundColor, setBackgroundColor] = useState<string>("rgba(0, 0, 0, 0.4)")
    const [opacityBlack, setOpacityBlack] = useState<number>(1);
    const [opacityColor, setOpacityColor] = useState<number>(0);
    const [showLeaderboard, setShowLeaderboard] = useState(false);
    const [photoUrl, setPhotoUrl] = useState<string | null>(null);

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
                        setName(userInstance.name);
                        setEmail(userInstance.email);
                        setRating(userInstance.rating);
                        setAccuracy(userInstance.accuracy);
                        setHighStreak(userInstance.highStreak);
                        setCurrentStreak(userInstance.currentStreak);
                        setGuesses(userInstance.guess);
                        setPassword(userInstance.password);
                        setTotalGuesses(userInstance.totalGuesses);
                    }
                } catch (error) {
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
        if(city===dailyChallenge[0]) {
            setBackgroundColor('linear-gradient(90deg, rgba(46, 128, 176, 0.4), rgba(46, 128, 176, 0.4), rgba(0, 0, 0, 0.4), rgba(46, 128, 176, 0.4), rgba(46, 128, 176, 0.4))');
            setOpacityColor(1);
            setOpacityBlack(0);
        }
        else {
            setOpacityBlack(1);
            setOpacityColor(0);
        }
    }, [city]);

    useEffect(() => {
        if(city!==dailyChallenge[0]) {
            setOpacityColor(1);
            setOpacityBlack(0);
            if(difficulty==="easy") setBackgroundColor('linear-gradient(90deg, rgba(134, 239, 172, 0.4), rgba(0, 0, 0, 0.4), rgba(134, 239, 172, 0.4))');
            else if(difficulty==="medium") setBackgroundColor('linear-gradient(90deg, rgba(253, 224, 71, 0.4), rgba(0, 0, 0, 0.4), rgba(253, 224, 71, 0.4))');
            else if(difficulty==="hard") setBackgroundColor('linear-gradient(90deg, rgba(252, 165, 165, 0.4), rgba(0, 0, 0, 0.4), rgba(252, 165, 165, 0.4))');
            setTimeout(() => {
                setOpacityBlack(1);
                setOpacityColor(0);
            }, 3000);
        }
    }, [difficulty]);

    const makeGuess = async () => {
        if (guess !== "") {
            const headers = {
                Authorization: `Bearer ${JSON.parse(localStorage.getItem("token") as string)}`
            };
            const data = {
                guess: guess,
                city: city
            };

            try {
                const response = await axios.post<string[]>("http://localhost:5000/api/user/makeGuess", data, { headers });
                setGuesses(response.data);
            } catch (error) {
                console.error("An error occurred:", error);
            }
        }
        return;
    };



    return (
        <HomeContext.Provider
            value={{
                leaderBoard,
                setLeaderBoard,
                dailyChallenge,
                setDailyChallenge,
                difficulty,
                setDifficulty,
                guess,
                setGuess,
                guessed,
                setGuessed,
                guesses,
                setGuesses,
                name,
                setName,
                password,
                setPassword,
                email,
                setEmail,
                rating,
                setRating,
                accuracy,
                setAccuracy,
                highStreak,
                setHighStreak,
                currentStreak,
                setCurrentStreak,
                totalGuesses,
                setTotalGuesses,
                city,
                setCity,
                backgroundColor,
                setBackgroundColor,
                opacityBlack,
                setOpacityBlack,
                opacityColor,
                setOpacityColor,
                showLeaderboard,
                setShowLeaderboard,
                photoUrl,
                setPhotoUrl
            }}
        >
            {children}
        </HomeContext.Provider>
    );
};
