import React, {useEffect, useRef, useState} from "react";
import axios from 'axios';

interface SliderProps {
    dailyCity: string;
    city: string;
    difficulty: string;
    onChangeCity: (city: string) => void;
}

interface city {
    location: string;
    temperature: number;
    condition: string;
    latitude: number;
    longitude: number;
    difficulty: string;
}

const Slider: React.FC<SliderProps> = ({ dailyCity, city, difficulty, onChangeCity }) => {
    // Your component code here
    const [listOfCities, setListOfCities] = useState<string[]>([]);
    const sliderRef = useRef<HTMLDivElement | null>(null);
    const [cityIdx, setCityIdx] = useState<number>(0);

    useEffect(() => {
        setCityIdx(listOfCities.findIndex(c=>c===city));
    }, [city]);

    useEffect(() => {
        if(listOfCities.length!==0) dailyCity===city?setCityIdx(listOfCities.findIndex(c=>c===city)):setCityIdx(0);
    }, [listOfCities]);

    useEffect(() => {
        fetchData(difficulty);
    }, [difficulty]);

    const handleCityChange = (city: string) => {onChangeCity(city)};

    const fetchData = async (difficulty : string) => {
        try {
            const response = await axios.get<city[]>(
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
        <div ref={sliderRef}>
            <p>{city}</p>
            <div className={"sliderBtnsContainer"}>
                <button key={"previousCity"} onClick={() => previousCityHandler()}>
                Previous
                </button>
                <button key={"nextCity"} onClick={() => nextCityHandler()}>
                    Next
                </button>
            </div>
        </div>
    );
};

export default Slider;