import React from "react";
import "../../../index.css";
interface GuessBtnsProps {
    guessed: string
    onChangeGuess: (option: string) => void;
}

const GuessBtns: React.FC<GuessBtnsProps> = ({ guessed, onChangeGuess }) => {
    const options = ["Snowy", "Thunder", "Humid", "Rainy", "Cloudy", "Clear", "Sunny"];

    const handleGuessChange = (option: string) => {
        onChangeGuess(option);
        console.log("btn pressed");
    };

    return (
        <div>
            {options.map((option) => (
                <button
                    key={option}
                    onClick={() => handleGuessChange(option)}
                    className={option === guessed ? "selected" : ""}
                >
                    {option}
                </button>
            ))}
        </div>
    );
};

export default GuessBtns;
