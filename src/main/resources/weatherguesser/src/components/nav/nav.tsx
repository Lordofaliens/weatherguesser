import React, {useRef} from "react";
import DailyChallenge from "../main/dailyChallenge";
import {Link} from "react-router-dom";
import {useHomeContext} from "../../contexts/HomeContext";
import {INavProps} from "../../interfaces/IProps";

const Nav: React.FC<INavProps> = () => {
    const footerRef = useRef<HTMLDivElement | null>(null);

    const {
        showLeaderboard,
        setShowLeaderboard
    } = useHomeContext();

    return (
        <div ref={footerRef} className="flex fixed justify-between items-center pl-3 pr-3 w-full z-50 bg-second bg-opacity-100 shadow-md">
            <div className="flex items-center">
                <span className="text-xl text-logo font-bold">Weather</span>
                <img alt="logo light" src={"./icons/logolight.png"} className="h-diffIcon m-3 ease-in-out duration-300 cursor-pointer hover:animate-rotateAnimation"/>
                <span className="text-xl text-logo font-bold">Guesser</span>
            </div>
            <div className="flex items-center">
                <DailyChallenge />
                <button onClick={()=>setShowLeaderboard(!showLeaderboard)}>
                    <img alt="leaderboard" src={"./icons/leaderboard.png"} className="m-3 w-diffIcon h-footerIcon hover:scale-150 ease-in-out duration-300" />
                </button>
                <Link to={"../account"}>
                    <img alt="account" src={"./icons/account.png"} className="m-3 h-footerIcon hover:scale-150 ease-in-out duration-300" style={{width:"25px"}} />
                </Link>
            </div>
        </div>
    );
};

export default Nav;