import React, { useState } from 'react';
import api from '../services/api';
import { useNavigate, Link } from 'react-router-dom'; // Import Link

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
            const response = await api.post('/api/login', { email, password }); // Ensure /api prefix
            setMessage(response.data); // "Единоразовый код для входа отправлен вам на почту"
            setStep(2); // Move to token entry step
        } catch (err) {
            setError(err.response?.data?.message || err.response?.data || err.message || 'Ошибка входа');
        }
    };

    const handleTokenSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setMessage('');
        try {
            const response = await api.post('/api/token-login', { token }); // Ensure /api prefix
            const { accessToken, refreshToken, role } = response.data; // Destructure role

            // Сохраняем токены и роль в localStorage
            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);
            localStorage.setItem('userRole', role); // Store the role
            setMessage('Вход выполнен успешно!');

            // --- Role-based redirection ---
            if (!role) {
                console.warn("User role not found in token/response. Redirecting to generic profile.");
                navigate('/user-dashboard'); // Fallback redirect to user dashboard
            } else if (role === 'VOLUNTEER') {
                navigate('/volunteer-dashboard'); // Redirect volunteer
            } else {
                navigate('/user-dashboard'); // Redirect regular user (or any other role)
            }
            // --- End Role-based redirection ---

            window.location.reload(); // Reload to update App state and routing
            console.log('Login successful, tokens received:', response.data);
        } catch (err) {
            setError(err.response?.data?.message || err.response?.data || err.message || 'Ошибка при вводе токена');
        }
    };

    // Common input field class
    const inputClass = "form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#111418] focus:outline-0 focus:ring-0 border-none bg-[#f0f2f4] focus:border-none h-14 placeholder:text-[#637588] p-4 text-base font-normal leading-normal";


    return (
        <div className="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden" style={{ fontFamily: '"Plus Jakarta Sans", "Noto Sans", sans-serif' }}>
            <div className="layout-container flex h-full grow flex-col">
                {/* Header can be added from App.js or here if needed */}
                <div className="px-4 sm:px-10 md:px-20 lg:px-40 flex flex-1 justify-center py-5">
                    <div className="layout-content-container flex flex-col w-full max-w-[512px] py-5">
                        <h2 className="text-[#111418] text-3xl font-bold leading-tight tracking-[-0.015em] px-4 py-3 text-center">Вход в аккаунт</h2>

                        {/* Step 1: Email and Password */}
                        {step === 1 && (
                            <form onSubmit={handleLoginSubmit}>
                                <div className="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
                                    <label className="flex flex-col min-w-40 flex-1">
                                        <span className="text-sm font-medium text-[#111418] mb-1">Email*</span>
                                        <input type="email" placeholder="user@example.com" className={inputClass} value={email} onChange={(e) => setEmail(e.target.value)} required />
                                    </label>
                                </div>
                                <div className="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
                                    <label className="flex flex-col min-w-40 flex-1">
                                        <span className="text-sm font-medium text-[#111418] mb-1">Пароль*</span>
                                        <input type="password" placeholder="********" className={inputClass} value={password} onChange={(e) => setPassword(e.target.value)} required />
                                    </label>
                                </div>

                                {/* Messages */}
                                {error && <p className="px-4 py-2 text-red-600">{error}</p>}
                                {message && <p className="px-4 py-2 text-green-600">{message}</p>}


                                {/* Buttons */}
                                <div className="flex justify-stretch">
                                    <div className="flex flex-1 gap-3 flex-wrap px-4 py-3 justify-start">
                                        <Link to="/" className="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#f0f2f4] text-[#111418] text-sm font-bold leading-normal tracking-[0.015em]">
                                            <span className="truncate">Отмена</span>
                                        </Link>
                                        <button type="submit" className="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#1980e6] text-white text-sm font-bold leading-normal tracking-[0.015em]">
                                            <span className="truncate">Получить код</span>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        )}

                        {/* Step 2: Token Entry */}
                        {step === 2 && (
                            <form onSubmit={handleTokenSubmit}>
                                <p className="px-4 py-2 text-center text-[#637588]">На ваш email ({email}) отправлен одноразовый код подтверждения.</p>
                                <div className="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
                                    <label className="flex flex-col min-w-40 flex-1">
                                        <span className="text-sm font-medium text-[#111418] mb-1">Код подтверждения*</span>
                                        <input type="text" placeholder="Введите код из письма" className={inputClass} value={token} onChange={(e) => setToken(e.target.value)} required />
                                    </label>
                                </div>

                                {/* Messages */}
                                {error && <p className="px-4 py-2 text-red-600">{error}</p>}
                                {message && <p className="px-4 py-2 text-green-600">{message}</p>}

                                {/* Buttons */}
                                <div className="flex justify-stretch">
                                    <div className="flex flex-1 gap-3 flex-wrap px-4 py-3 justify-start">
                                        <button type="button" onClick={() => { setStep(1); setError(''); setMessage(''); }} className="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#f0f2f4] text-[#111418] text-sm font-bold leading-normal tracking-[0.015em]">
                                            <span className="truncate">Назад</span>
                                        </button>
                                        <button type="submit" className="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#1980e6] text-white text-sm font-bold leading-normal tracking-[0.015em]">
                                            <span className="truncate">Подтвердить вход</span>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        )}

                        {/* Link to Register */}
                        <div className="px-4 py-3 text-center">
                            <p className="text-sm text-[#637588]">Нет аккаунта? <Link to="/choose-role" className="text-[#1980e6] font-medium">Зарегистрироваться</Link></p>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    );
}

export default Login;
