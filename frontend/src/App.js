import React, { useEffect } from 'react';
import { Routes, Route, Link, useNavigate, useLocation, Navigate } from 'react-router-dom';
import Register from './pages/Register';
import Login from './pages/Login';
import VerifyEmail from './pages/VerifyEmail';
import UserDashboard from './pages/UserDashboard'; // Import the new User Dashboard
import Home from './pages/Home'; // This is now the Role Selection page
import LandingPage from './pages/LandingPage'; // Import the new Landing Page

function App() {
    const navigate = useNavigate();
    const location = useLocation();
    const accessToken = localStorage.getItem('accessToken');

    // Redirect logged-in users trying to access public pages to '/profile'
    useEffect(() => {
        if (accessToken && ['/', '/choose-role', '/login', '/register'].includes(location.pathname)) {
            navigate('/profile', { replace: true });
        }
    }, [accessToken, location.pathname, navigate]);

    const handleLogout = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        navigate('/'); // Navigate to landing page on logout
        window.location.reload();
    };

    return (
        <div>
            {/* Simple Nav - Can be replaced by LandingPage's header if needed */}
            <nav className="bg-gray-100 p-4 flex justify-between items-center">
                <Link to="/" className="text-lg font-bold text-[#111418]">Volunteerly</Link>
                <div>
                    {!accessToken ? (
                        <>
                            <Link to="/choose-role" className="text-[#111418] text-sm font-medium leading-normal mr-4">Начать</Link>
                            <Link to="/login" className="text-[#111418] text-sm font-medium leading-normal mr-2">Вход</Link>
                            {/* <Link to="/register" className="text-[#111418] text-sm font-medium leading-normal">Регистрация</Link> */}
                        </>
                    ) : (
                        <>
                            <Link to="/profile" className="text-[#111418] text-sm font-medium leading-normal mr-4">Профиль</Link>
                            <button onClick={handleLogout} className="text-[#111418] text-sm font-medium leading-normal">Выход</button>
                        </>
                    )}
                </div>
            </nav>

            <Routes>
                {/* Public Routes */}
                <Route path="/" element={<LandingPage />} />
                <Route path="/choose-role" element={!accessToken ? <Home /> : <Navigate to="/profile" replace />} />
                <Route path="/register" element={!accessToken ? <Register /> : <Navigate to="/profile" replace />} />
                <Route path="/login" element={!accessToken ? <Login /> : <Navigate to="/profile" replace />} />
                <Route path="/verify-email/:token" element={<VerifyEmail />} /> {/* Keep accessible */}

                {/* Profile/Dashboard Route - Now accessible without login */}
                <Route path="/profile" element={<UserDashboard />} />

                {/* Catch-all Redirect */}
                <Route path="*" element={<Navigate to={accessToken ? "/profile" : "/"} replace />} />
            </Routes>
        </div>
    );
}

export default App;