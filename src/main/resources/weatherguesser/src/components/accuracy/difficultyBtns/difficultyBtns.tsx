import React from "react";

interface DifficultyBtnsProps {
    onChangeDifficulty: (difficulty: string) => void;
}

const DifficultyBtns: React.FC<DifficultyBtnsProps> = ({ onChangeDifficulty }) => {
    const levels = ["easy", "medium", "hard"];

    const handleDifficultyChange = (difficulty: string) => {
        onChangeDifficulty(difficulty);
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
