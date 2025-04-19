import React, { useState } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [token, setToken] = useState('');
    const [step, setStep] = useState(1); // 1 for login, 2 for token entry
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLoginSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setMessage('');
        try {
            const response = await api.post('/login', { email, password });
            setMessage(response.data); // "Единоразовый код для входа отправлен вам на почту"
            setStep(2); // Move to token entry step
        } catch (err) {
            setError(err.response?.data?.message || err.message || 'Ошибка входа');
        }
    };

    const handleTokenSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setMessage('');
        try {
            const response = await api.post('/token-login', { token });
            // Сохраняем токены в localStorage
            localStorage.setItem('accessToken', response.data.accessToken);
            localStorage.setItem('refreshToken', response.data.refreshToken);
            setMessage('Вход выполнен успешно!');
            // Перенаправляем на защищенную страницу (например, профиль)
            navigate('/profile'); // Перенаправление после успешного входа
            console.log('Login successful, tokens received:', response.data);
        } catch (err) {
            setError(err.response?.data?.message || err.response?.data || err.message || 'Ошибка при вводе токена');
        }
    };

    return (
        <div>
            <h2>Вход</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {message && <p style={{ color: 'green' }}>{message}</p>}

            {step === 1 && (
                <form onSubmit={handleLoginSubmit}>
                    <div>
                        <label>Email:</label>
                        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                    </div>
                    <div>
                        <label>Пароль:</label>
                        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                    </div>
                    <button type="submit">Войти</button>
                </form>
            )}

            {step === 2 && (
                <form onSubmit={handleTokenSubmit}>
                    <div>
                        <label>Код из письма:</label>
                        <input type="text" value={token} onChange={(e) => setToken(e.target.value)} required />
                    </div>
                    <button type="submit">Подтвердить вход</button>
                </form>
            )}
        </div>
    );
}

export default Login;
