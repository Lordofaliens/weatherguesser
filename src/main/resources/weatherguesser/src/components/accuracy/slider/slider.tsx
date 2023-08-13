import React, {useEffect, useRef, useState} from "react";
import axios from 'axios';

interface SliderProps {
    difficulty: string;
}

interface city {
    location: string;
    temperature: number;
    condition: string;
    latitude: number;
    longitude: number;
    difficulty: string;
}

const Slider: React.FC<SliderProps> = ({ difficulty }) => {
    // Your component code here
    const [listOfCities, setListOfCities] = useState<string[]>([]);
    const [currentCity, setCurrentCity] = useState<string>('New York');
    const sliderRef = useRef<HTMLDivElement | null>(null);
    const [cityIdx, setCityIdx] = useState<number>(0);

    useEffect(() => {
        if(difficulty=="easy") setCurrentCity("New York")
        else if(difficulty=="medium") setCurrentCity("Paris")
        else setCurrentCity("Tokyo");
        setCityIdx(0);
        fetchData(difficulty);
    }, [difficulty]);
    useEffect(() => {
        console.log('Difficulty prop changed:', difficulty);
    }, [difficulty]);
    const fetchData = async (difficulty : string) => {
        try {
            const response = await axios.get<city[]>(
                'http://localhost:5000/api/weather/getCitiesByDiff',
                {
                    params: { difficulty },
                }
            );
            console.log(response.data)
            setListOfCities(response.data.map(d => d.location));
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };
    const previousCityHandler = () => {
        if(cityIdx==0) return;
        setCurrentCity(listOfCities[cityIdx-1]);
        setCityIdx(cityIdx-1);
    }
    const nextCityHandler = () => {
        if(cityIdx==listOfCities.length-1) return;
        setCurrentCity(listOfCities[cityIdx+1]);
        setCityIdx(cityIdx+1);
    }
    return (
        <div ref={sliderRef}>
            <p>{currentCity}</p>
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