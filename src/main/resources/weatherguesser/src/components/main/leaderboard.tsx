import React from 'react';
import Box from '@mui/material/Box';
import { Fade } from '@mui/material';
import {v4 as uuidv4} from "uuid";
import {useHomeContext} from "../../contexts/HomeContext";
import {ILeaderboardProps} from "../../interfaces/IProps";

const Leaderboard: React.FC<ILeaderboardProps> = () => {
    const {
        rating,
        highStreak,
        name,
        leaderBoard,
        showLeaderboard
    } = useHomeContext();

    return (
        <div className="absolute right-0 max-w-max">
            <Fade in={showLeaderboard} timeout={500}>
                <Box className="rounded-bl-lg"
                    sx={{
                        backgroundColor: 'rgba(245,245,245,0.9)',
                        p: 2,
                        position: 'absolute',
                        top: '64px',
                        right: '100%',
                        zIndex: '49',
                        color: '#ffa404',
                        maxHeight: 'calc(100vh - 64px)',
                        overflowY: 'scroll',
                        overflowX: 'hidden'
                    }}
                >
                    <div className="bg-second rounded-b-lg fixed w-leaderboard shadow-md" style={{marginTop:"-17px"}}>
                        <div className={"bg-logo rounded-lg m-2"}>
                            <p className="text-second text-center">LeaderBoard</p>
                        </div>
                        <div className={"bg-logo rounded-lg m-2 grid grid-cols-[40px,185px,40px] gap-y-2 w-full max-w-max"}>
                            <span className="text-sm text-center rounded-l-lg bg-logo text-second">{rating}</span>
                            <span className="text-sm text-center bg-logo text-second">{name}</span>
                            <span className="text-sm text-center rounded-r-lg bg-logo text-second">{highStreak}</span>
                        </div>
                    </div>
                    <div className="text-second rounded-lg mt-16 grid grid-cols-[40px,200px,40px] gap-y-2 w-full max-w-max" key={uuidv4()}>
                        {leaderBoard&&leaderBoard.map((user, index)=>(
                            <React.Fragment key={index}>
                                <span className={`text-sm text-center rounded-l-lg ${user.name===name ? "bg-second text-logo":"text-second"}`}>{user.rating}</span>
                                <span className={`text-sm text-center ${user.name===name ? "bg-second text-logo":"text-second"}`}>{user.name}</span>
                                <span className={`text-sm text-center rounded-r-lg ${user.name===name ? "bg-second text-logo":"text-second"}`}>{user.highStreak}</span>
                            </React.Fragment>
                        ))}
                    </div>
                </Box>
            </Fade>
        </div>
    );
};

export default Leaderboard;