import React, {useEffect, useRef, useState} from "react";
import axios from 'axios';

interface DigitProps {
    accuracy: number;
}

const Digit: React.FC<DigitProps> = ({ accuracy }) => {
    const digitRef = useRef<HTMLDivElement | null>(null);
    return (
        <div ref={digitRef}>
            <p>{accuracy}</p>
        </div>
    );
};

export default Digit;