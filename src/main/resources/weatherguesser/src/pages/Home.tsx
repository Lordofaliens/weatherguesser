import React, {useEffect, useState} from 'react';
import Scale from "../components/main/scale";
import Digit from "../components/main/digit";
import Slider from "../components/main/slider";
import DifficultyButtons from "../components/main/difficultyButtons";
import GuessButtons from "../components/main/guessButtons";
import Nav from "../components/nav/nav";
import Leaderboard from "../components/main/leaderboard";
import {useHomeContext} from "../contexts/HomeContext";
import {toast, ToastContainer} from "react-toastify";
import {useNavigate} from "react-router-dom";

const Home: React.FC = () => {
    const navigate = useNavigate();
    if(!localStorage.getItem("token")) navigate("../auth");

    const {
        backgroundColor,
        opacityBlack,
        opacityColor,
        photoUrl
    } = useHomeContext();

    const [showToast, setShowToast] = useState(false);

    useEffect(() => {
        const today = new Date().toLocaleDateString();
        const lastVisit = localStorage.getItem('lastVisit');

        if (!lastVisit || lastVisit !== today) {
            setShowToast(true);
            localStorage.setItem('lastVisit', today);
        }
    }, []);

    useEffect(() => {
        console.log("JUST TOAST")
        if (showToast) {
            console.log("NEW TOAST")
            toast.dismiss();
            toast.info(`Your stats are updated! Check account!`, {
                position: toast.POSITION.TOP_CENTER,
                draggablePercent: 50,
                role: "alert",
                autoClose: 6000
            })
            setShowToast(false);
        }
    }, [showToast]);

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
            <ToastContainer />
        </div>
    );
};

export default Home;