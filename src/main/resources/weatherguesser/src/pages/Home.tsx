import React from 'react';
import Scale from "../components/main/scale";
import Digit from "../components/main/digit";
import Slider from "../components/main/slider";
import DifficultyButtons from "../components/main/difficultyButtons";
import GuessButtons from "../components/main/guessButtons";
import Nav from "../components/nav/nav";
import Leaderboard from "../components/main/leaderboard";
import {useHomeContext} from "../contexts/HomeContext";

const Home: React.FC = () => {
    const {
        backgroundColor,
        opacityBlack,
        opacityColor,
        photoUrl
    } = useHomeContext();

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
                className="absolute top-0 left-0 w-full h-full bg-backBlack z-0"
                style={{
                    transition: "all 2s ease-in-out",
                    opacity: opacityBlack
                }}
            />
            <div
                className="absolute top-0 left-0 w-full h-full z-0"
                style={{
                    background: backgroundColor,
                    transition: "all 2s ease-in-out",
                    opacity: opacityColor
                }}
            />
            <Nav />
            <Leaderboard />
            <div style={{
                height: "100vh",
                zIndex: "2",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "end"
            }}>
                <div className="fixed inset-0 flex flex-col items-center justify-center z-0">
                    <div style={{display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        justifyContent: "center",
                        marginTop: "80px"
                    }}>
                        <GuessButtons />
                        <Scale />
                        <Digit />
                    </div>
                    <Slider />
                </div>
                <DifficultyButtons />
            </div>
        </div>
    );
};

export default Home;