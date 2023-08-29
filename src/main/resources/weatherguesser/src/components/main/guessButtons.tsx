import React, {useEffect, useState} from 'react';
import {useHomeContext} from "../../contexts/HomeContext";
import {IGuessButtonsProps} from "../../interfaces/IProps";

const GuessButtons: React.FC<IGuessButtonsProps> = () => {
    const {
        guessed,
        setGuess
    } = useHomeContext();

    const [visibleButtons, setVisibleButtons] = useState<string[]>([]);
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

    const handleGuessChange = (option: string) => {setGuess(option)};

    const radius = 275;
    const startAngle = Math.PI;
    const endAngle = 2 * Math.PI;

    return (
        <div className="z-10 absolute w-mainDiv h-mainDiv">
            {visibleButtons.map((option, index) => {
                const angle = startAngle + (index / (options.length - 1)) * (endAngle - startAngle);
                const xPos = radius-275+(radius) * Math.cos(angle);
                const yPos = radius-60+(radius) * Math.sin(angle);

                return (
                    <div key={index} className="guessBtnContainer group" style={{position: "relative", transform: `translate(${xPos}px, ${yPos}px)`, transition: "all 0.3s ease-in-out", display: "flex", alignItems: "center", justifyContent: "center" }}>
                        <div className={`absolute z-10 group-hover:animate-rotate5Animation ${option === guessed ? "animate-guessedBackBtnAnimation" : "animate-guessBackBtnAnimation"}`}
                            style={{ position: "absolute", zIndex: 1 }}>
                            {option === guessed ? <img alt="guess button back active" src={'./icons/guessbackactive.png'} /> : <img alt="guess button back" src={'./icons/guessback.png'} />}
                        </div>
                        <div style={{ position: "absolute", height:"40px", zIndex: 2 }}>
                        <button
                            key={option}
                            onClick={() => handleGuessChange(option)}
                            className="w-guessIcon h-auto rounded-full bg-cover bg-center bg-no-repeat animate-guessBtnAnimation group-hover:animate-rotate5Animation" style={{ backgroundImage: `url("../public/guessBtn.png")` }}
                        >
                            <img alt="guess button front" src={`./icons/${option.toLowerCase()}.png`}/>
                        </button>
                        </div>
                    </div>

                );
            })}
        </div>
    );
};

export default GuessButtons;
