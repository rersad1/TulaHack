import React from 'react';
import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import Register from './pages/Register';
import Login from './pages/Login';
import VerifyEmail from './pages/VerifyEmail'; // Импортировать новый компонент
import Profile from './pages/Profile'; // Импортировать компонент профиля

function App() {
    const navigate = useNavigate();
    const accessToken = localStorage.getItem('accessToken');

    const handleLogout = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        // Опционально: уведомить бэкенд об аннулировании refresh токена, если это необходимо
        navigate('/login');
        // Можно перезагрузить страницу или обновить состояние, чтобы UI обновился
        window.location.reload(); // Простой способ обновить UI
    };

    return (
        <div>
            <nav>
                <Link to="/register">Регистрация</Link> | <Link to="/login">Вход</Link>
                {accessToken && ( // Показываем ссылку на профиль и выход только если пользователь вошел
                    <>
                        | <Link to="/profile">Профиль</Link>
                        | <button onClick={handleLogout}>Выход</button>
                    </>
                )}
            </nav>
            <Routes>
                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login />} />
                <Route path="/verify-email/:token" element={<VerifyEmail />} />
                {/* Новый маршрут для профиля */}
                <Route path="/profile" element={<Profile />} />
                {/* Добавить другие маршруты */}
            </Routes>
        </div>
    );
}

export default App;
