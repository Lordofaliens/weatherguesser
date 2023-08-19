import React from "react";

interface DifficultyBtnsProps {
    onChangeCity: (city: string) => void;
    onChangeDifficulty: (difficulty: string) => void;
}

const DifficultyBtns: React.FC<DifficultyBtnsProps> = ({ onChangeCity, onChangeDifficulty }) => {
    const levels = ["easy", "medium", "hard"];

    const handleDifficultyChange = (difficulty: string) => {
        onChangeDifficulty(difficulty);
            if(difficulty==="easy") onChangeCity("New York")
            else if(difficulty==="medium") onChangeCity("Paris")
            else onChangeCity("Tokyo");
    };

    return (
        <div>
            {levels.map((level) => (
                <button key={level} onClick={() => handleDifficultyChange(level)}>
                    {level}
                </button>
            ))}
        </div>
    );
};

export default DifficultyBtns;
