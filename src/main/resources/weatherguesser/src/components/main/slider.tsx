import React, {useEffect, useRef, useState} from "react";
import axios from 'axios';
import {useHomeContext} from "../../contexts/HomeContext";
import {ISliderProps} from "../../interfaces/IProps";
import ICity from "../../interfaces/ICity";

const Slider: React.FC<ISliderProps> = () => {
    const {
        dailyChallenge,
        city,
        difficulty,
        setCity,
        currentStreak,
        highStreak
    } = useHomeContext();

    const [listOfCities, setListOfCities] = useState<string[]>([]);
    const sliderRef = useRef<HTMLDivElement | null>(null);
    const [cityIdx, setCityIdx] = useState<number>(0);

    useEffect(() => {
        setCityIdx(listOfCities.findIndex(c=>c===city));
    }, [city]);

    useEffect(() => {
        if(listOfCities.length!==0) dailyChallenge[0]===city?setCityIdx(listOfCities.findIndex(c=>c===city)):setCityIdx(0);
    }, [listOfCities]);

    useEffect(() => {
        fetchData(difficulty);
    }, [difficulty]);

    const handleCityChange = (city: string) => {setCity(city)};

    const fetchData = async (difficulty : string) => {
        try {
            const response = await axios.get<ICity[]>(
                'http://localhost:5000/api/weather/getCitiesByDiff',
                {
                    params: { difficulty },
                }
            );
            setListOfCities(response.data.map(d => d.location));
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    }

    const previousCityHandler = () => {
        if(cityIdx===0) {
            handleCityChange(listOfCities[listOfCities.length-1]);
            setCityIdx(listOfCities.length-1);
        } else {
            handleCityChange(listOfCities[cityIdx-1]);
            setCityIdx(cityIdx-1);
        }
    }

    const nextCityHandler = () => {
        if(cityIdx===listOfCities.length-1) {
            handleCityChange(listOfCities[0]);
            setCityIdx(0);
        } else {
            handleCityChange(listOfCities[cityIdx+1]);
            setCityIdx(cityIdx+1);
        }
    }

    return (
        <div className="relative">
            <div ref={sliderRef} className="flex justify-between w-slider min-w-account">
                <button key={"previousCity"} onClick={() => previousCityHandler()}>
                    <img alt="left" src={"./icons/left.png"} />
                </button>
                <p key={city} className="text-5xl text-center text-logo animate-scaleLargeAnimation">{city}</p>
                <button key={"nextCity"} onClick={() => nextCityHandler()}>
                    <img alt="right" src={"./icons/right.png"} />
                </button>
            </div>
            {city===dailyChallenge[0] && (
                <div className="flex flex-col justify-center items-center p-2 text-xl text-second font-black" style={{animation: 'fadeInAnimation 1.4s ease-out' }}>
                    <div className="relative w-full h-4 bg-logo rounded-lg shadow-md">
                        <div
                            className="absolute h-full bg-second rounded-lg"
                            style={{ width: `${(currentStreak/(highStreak+1))*100}%`, animation: 'fillAnimation 1.5s ease-out'}}
                        />
                    </div>
                    {currentStreak===highStreak?
                        <p>You are on fire!</p>
                        :
                        <p>Breaking the record of {highStreak}</p>
                    }
                </div>
            )}
            <div className={"blur-container"}></div>
            <style>
                {`
                  @keyframes fillAnimation {
                    0% { width: 0; }
                    100% { width: ${highStreak!==0 ? (currentStreak/(highStreak+1))*100 : 0}%; }
                  }
                  @keyframes fadeInAnimation {
                    0% {scale: 0; }
                    80% {scale: 1.1;}
                    100% {scale: 1; }
                  }
                `}
            </style>
        </div>

    );
};

export default Slider;