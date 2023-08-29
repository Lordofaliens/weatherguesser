import React from "react";
import {useHomeContext} from "../../contexts/HomeContext";
import {IDifficultyButtonsProps} from "../../interfaces/IProps";

const DifficultyButtons: React.FC<IDifficultyButtonsProps> = () => {
    const {
        difficulty,
        setCity,
        setDifficulty,
    } = useHomeContext();

    const levels = ["easy", "medium", "hard"];

    const handleDifficultyChange = (difficulty: string) => {
        setDifficulty(difficulty);
        if(difficulty==="easy") setCity("New York")
        else if(difficulty==="medium") setCity("Paris")
        else setCity("Tokyo");
    };

    return (
        <div className="m-3">
            {levels.map((level) => (
                <button key={level} onClick={() => handleDifficultyChange(level)} className={`p-2 ${level==="easy"?"bg-green": level==="medium"? "bg-yellow" : "bg-red"} ${difficulty!==level?"opacity-80 scale-diffNotActive":"opacity-100 scale-diffActive"} rounded-lg ease-in-out`}>
                    <img alt="difficulty button" src={`./icons/${level}.png`} className="h-diffIcon w-diffIcon" />
                </button>
            ))}
        </div>
    );
};

export default DifficultyButtons;
