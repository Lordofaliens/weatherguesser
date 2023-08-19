import React, {useEffect, useRef, useState} from "react";
import axios from 'axios';

interface DailyChallengeProps {
    currentStreak: number;
    city: string;
    difficulty: string;
    onChangeDifficulty(): void;
    onChangeCity(): void;
}

const DailyChallenge: React.FC<DailyChallengeProps> = ({ currentStreak, city, difficulty, onChangeDifficulty, onChangeCity  }) => {
    const dailyChallengeRef = useRef<HTMLDivElement | null>(null);
    function handleChange() {
        onChangeDifficulty();
        onChangeCity();
    }

    return (
        <div ref={dailyChallengeRef}>
            <button onClick={()=>handleChange()}>
                <p>DailyChallenge</p>
                <p>{city} {difficulty}</p>
                <p>CurrentStreak: {currentStreak}</p>
            </button>
        </div>
    );
};

export default DailyChallenge;