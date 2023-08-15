import React from 'react';
const Logout: React.FC = () => {
    function logoutEffect() {
        localStorage.removeItem("token");
        window.location.href = "../home";
    }
    logoutEffect();
    return (<div></div>);
};

export default Logout;