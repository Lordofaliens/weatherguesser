import React from 'react';
import { useNavigate } from 'react-router-dom';
const Logout: React.FC = () => {
    const navigate = useNavigate();
    function logoutEffect() {
        localStorage.removeItem("token");
        navigate("../home");
    }
    logoutEffect();
    return (<div></div>);
};

export default Logout;