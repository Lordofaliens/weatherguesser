import React from 'react';
import { BrowserRouter, BrowserRouterProps, Route, Routes } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Logout from './pages/Logout';
import Register from './pages/Register';
import Account from './pages/Account';

interface RouteConfig {
    element: React.ReactNode;
    path: string;
}

const routes: RouteConfig[] = [
    {
        element: <Home />,
        path: '/home'
    },
    {
        element: <Account />,
        path: '/account'
    },
    {
        element: <Login />,
        path: '/login'
    },
    {
        element: <Logout />,
        path: '/logout'
    },
    {
        element: <Register />,
        path: '/register'
    }
];

const AppRouter: React.FC<BrowserRouterProps> = () => {
    return (
        <BrowserRouter>
            <Routes>
                {routes.map((route, index) => (
                    <Route key={index} path={route.path} element={route.element} />
                ))}
            </Routes>
        </BrowserRouter>
    );
};

export default AppRouter;