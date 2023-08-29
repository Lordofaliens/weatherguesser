import React, {useEffect, useRef, useState} from "react";
import {useHomeContext} from "../../contexts/HomeContext";
import {IDigitProps} from "../../interfaces/IProps";

const Digit: React.FC<IDigitProps> = () => {
    const {
        accuracy
    } = useHomeContext();

    const digitRef = useRef<HTMLDivElement | null>(null);
    const [number, setNumber] = useState<number>(0);
    function getColor() {
        const targetRed = 46;
        const targetGreen = 128;
        const targetBlue = 176;

        const red = Math.round((targetRed - 245) * (accuracy / 100)) + 245;
        const green = Math.round((targetGreen - 245) * (accuracy / 100)) + 245;
        const blue = Math.round((targetBlue - 245) * (accuracy / 100)) + 245;

        return `rgba(${red}, ${green}, ${blue}, 1)`;
    }

    useEffect(() => {
        if (number < accuracy) {
            const timeoutId = setTimeout(() => {
                setNumber(number + 1);
            }, 15);
            return () => clearTimeout(timeoutId);
        }
    }, [number, accuracy]);

    return (
        <div ref={digitRef} style={{position:"absolute"}}>
            <p style={{fontSize:"100px", color:getColor() }}>{number}</p>
        </div>
    );
};

export default Digit;