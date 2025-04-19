import React, { useEffect } from 'react';
import { Routes, Route, Link, useNavigate, useLocation, Navigate } from 'react-router-dom';
import Register from './pages/Register';
import Login from './pages/Login';
import VerifyEmail from './pages/VerifyEmail';
import UserDashboard from './pages/UserDashboard'; // Import the new User Dashboard
import VolunteerDashboard from './pages/VolunteerDashboard'; // Import Volunteer Dashboard
import Home from './pages/Home'; // This is now the Role Selection page
import LandingPage from './pages/LandingPage'; // Import the new Landing Page

function App() {
    const navigate = useNavigate();
    const location = useLocation();
    const accessToken = localStorage.getItem('accessToken');
    const userRole = localStorage.getItem('userRole');

    // Улучшенный отладочный вывод
    console.log("App.js: Текущая роль:", userRole);
    console.log("App.js: Тип роли:", typeof userRole);
    console.log("App.js: Сравнение роли:", userRole === 'VOLUNTEER', userRole === "VOLUNTEER");
    console.log("App.js: Должен вести на:", userRole === 'VOLUNTEER' ? '/volunteer-dashboard' : '/user-dashboard');

    // Ensure we're using strict equality and proper string comparison
    const dashboardPath = userRole && userRole.trim() === 'VOLUNTEER'
        ? '/volunteer-dashboard'
        : '/user-dashboard';

    // Redirect logged-in users trying to access public pages to their specific dashboard
    useEffect(() => {
        if (accessToken && ['/', '/choose-role', '/login', '/register'].includes(location.pathname)) {
            navigate(dashboardPath, { replace: true }); // Redirect to specific dashboard
        }
    }, [accessToken, location.pathname, navigate, dashboardPath, userRole]);

    const handleLogout = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('userRole'); // Remove role on logout
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
                            {/* Link to the correct dashboard */}
                            <Link to={dashboardPath} className="text-[#111418] text-sm font-medium leading-normal mr-4">
                                {userRole === 'VOLUNTEER' ? 'Панель волонтера' : 'Моя панель'}
                            </Link>
                            <button onClick={handleLogout} className="text-[#111418] text-sm font-medium leading-normal">Выход</button>
                        </>
                    )}
                </div>
            </nav>

            <Routes>
                {/* Public Routes - Redirect if logged in */}
                <Route path="/" element={!accessToken ? <LandingPage /> : <Navigate to={dashboardPath} replace />} />
                <Route path="/choose-role" element={!accessToken ? <Home /> : <Navigate to={dashboardPath} replace />} />
                <Route path="/register" element={!accessToken ? <Register /> : <Navigate to={dashboardPath} replace />} />
                <Route path="/login" element={!accessToken ? <Login /> : <Navigate to={dashboardPath} replace />} />
                <Route path="/verify-email/:token" element={<VerifyEmail />} /> {/* Keep accessible */}

                {/* Protected Routes */}
                {/* Use Navigate for routes accessible only when logged in */}
                <Route
                    path="/user-dashboard"
                    element={accessToken ? <UserDashboard /> : <Navigate to="/login" replace />}
                />
                <Route
                    path="/volunteer-dashboard"
                    element={accessToken ? <VolunteerDashboard /> : <Navigate to="/login" replace />}
                />

                {/* Catch-all Redirect */}
                {/* Redirect unknown paths to the appropriate dashboard if logged in, or landing page if not */}
                <Route path="*" element={<Navigate to={accessToken ? dashboardPath : "/"} replace />} />
            </Routes>
        </div>
    );
}

export default App;