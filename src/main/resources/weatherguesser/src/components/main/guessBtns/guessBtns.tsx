import React, {useEffect, useState} from 'react';
import "../../../index.css";
interface GuessBtnsProps {
    guessed: string;
    onChangeGuess: (option: string) => void;
}

const GuessBtns: React.FC<GuessBtnsProps> = ({ guessed, onChangeGuess }) => {
    const [visibleButtons, setVisibleButtons] = useState<string[]>([]); // State to track visible buttons
    const options = ["Snowy", "Thunder", "Humid", "Rainy", "Cloudy", "Clear", "Sunny"];
    const buttonTimeouts : NodeJS.Timeout[] = [];

    useEffect(() => {
        function displayButtons() {

            options.forEach((option, index) => {
                const timeout = setTimeout(() => {
                    setVisibleButtons(prevButtons => [...prevButtons, option]);
                }, index * 200);
                buttonTimeouts.push(timeout);
            });
            return () => {
                buttonTimeouts.forEach(timeout => clearTimeout(timeout));
            };
        }
        setTimeout(()=>{displayButtons()},0)
    }, []);

    const handleGuessChange = (option: string) => {onChangeGuess(option)};

    const radius = 215; // Radius of the half circle
    const startAngle = Math.PI; // Start angle for the half circle
    const endAngle = 2 * Math.PI; // End angle for the half circle

    return (
        <div className="z-10 absolute" style={{width:"550px", height:"275px"}}>
            {visibleButtons.map((option, index) => {
                const angle = startAngle + (index / (options.length - 1)) * (endAngle - startAngle);
                const xPos = radius-75+(radius-80) * Math.cos(angle);
                const yPos = radius-40+(radius) * Math.sin(angle);

                return (
                    <button
                        key={option}
                        onClick={() => handleGuessChange(option)}
                        className={`guessBtn ${option === guessed ? "selected" : "notSelected"}  animate-opacity animate-scale`}
                        style={{transform: `translate(${xPos}px, ${yPos}px)`, transition: "all 0.3s ease-in-out"}}
                    >
                        {option}
                    </button>
                );
            })}
        </div>
    );
};

export default GuessBtns;
