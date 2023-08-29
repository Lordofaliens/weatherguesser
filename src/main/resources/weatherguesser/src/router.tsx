import React from 'react';
import { BrowserRouter, BrowserRouterProps, Route, Routes } from 'react-router-dom';
import {HomeProvider} from "./contexts/HomeContext";
import Home from './pages/Home';
import Auth from './pages/Auth';
import Account from './pages/Account';

interface RouteConfig {
    element: React.ReactNode;
    path: string;
}

const routes: RouteConfig[] = [
    {
        element:<HomeProvider> <Home /></HomeProvider>,
        path: '/home'
    },
    {
        element: <HomeProvider><Account /></HomeProvider>,
        path: '/account'
    },
    {
        element: <Auth />,
        path: '/auth'
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