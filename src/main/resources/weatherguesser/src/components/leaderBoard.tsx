import React, {useEffect, useRef, useState} from "react";
import { v4 as uuidv4 } from 'uuid';
import axios from 'axios';
import {getLeaderBoard} from "../controllers/getters/leaderBoardGetter";
import user from "../contexts/userContext";
import {randomUUID} from "crypto";

interface LeaderBoardProps {
    users: user[];
}

const Digit: React.FC<LeaderBoardProps> = ({ users }) => {
    const leaderBoardRef = useRef<HTMLDivElement | null>(null);


    return (
        <div ref={leaderBoardRef}>
            <p>LeaderBoard</p>
            {users&&users.map((user)=>(
                <div key={uuidv4()}>
                    {user.rating}
                    {user.name}
                    {user.highStreak}
                </div>
            ))}
        </div>
    );
};

export default Digit;