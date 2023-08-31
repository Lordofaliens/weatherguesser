import React from 'react';
import { BrowserRouter, BrowserRouterProps, Route, Routes } from 'react-router-dom';
import {HomeProvider} from "./contexts/HomeContext";
import Home from './pages/Home';
import Auth from './pages/Auth';
import Account from './pages/Account';
import Reset from './pages/Reset';

interface RouteConfig {
    element?: React.ReactNode;
    path: string;
    children?: RouteConfig[];
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
    },
    {
        element: <Reset type={"password"} />,
        path: '/resetpassword'
    },
    {
        element: <Reset type={"email"} />,
        path: 'resetemail'
    },
    {
        element: <Reset type={"verify"} />,
        path: 'verifyemail'
    },
    {
        path: '/reset',
        children: [
            {
                element: <Reset type={"password"} />,
                path: 'password'
            },
            {
                element: <Reset type={"email"} />,
                path: 'email'
            }
        ]
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