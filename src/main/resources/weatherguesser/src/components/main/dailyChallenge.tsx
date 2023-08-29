import React, {useRef} from "react";
import {useHomeContext} from "../../contexts/HomeContext";
import {IDailyChallengeProps} from "../../interfaces/IProps";

const DailyChallenge: React.FC<IDailyChallengeProps> = () => {
    const dailyChallengeRef = useRef<HTMLDivElement | null>(null);

    const {
        dailyChallenge,
        setDifficulty,
        setCity
    } = useHomeContext();

    function handleChange() {
        setDifficulty(dailyChallenge[1]);
        setCity(dailyChallenge[0]);
    }

    return (
        <div ref={dailyChallengeRef} className="flex items-center">
            <button onClick={()=>handleChange()}>
                <img alt="challenge" src={"./icons/challenge.png"} className="m-3 h-footerIcon w-footerIcon hover:scale-150 ease-in-out duration-300" />
            </button>
        </div>
    );
};

export default DailyChallenge;