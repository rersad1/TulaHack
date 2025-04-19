import React, { useState } from 'react';
import api from '../services/api';
import { useLocation, Link } from 'react-router-dom';

// Helper function to parse query params
function useQuery() {
    return new URLSearchParams(useLocation().search);
}

export default function Register() {
    const query = useQuery();
    const getInitialRole = () => {
        const roleFromQuery = query.get('role');
        return (roleFromQuery === 'USER' || roleFromQuery === 'VOLUNTEER') ? roleFromQuery : 'USER';
    };

    const initialRole = getInitialRole();

    const [data, setData] = useState({
        email: '',
        password: '',
        confirmPassword: '',
        firstName: '',
        lastName: '',
        middleName: '',
        role: initialRole
    });
    const [msg, setMsg] = useState('');
    const [error, setError] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const handleChange = e => {
        setData({ ...data, [e.target.name]: e.target.value });
        if (e.target.name === 'password' || e.target.name === 'confirmPassword') {
            setError('');
        }
    };

    const handleSubmit = async e => {
        e.preventDefault();
        setMsg('');
        setError('');

        if (data.password !== data.confirmPassword) {
            setError('Пароли не совпадают.');
            return;
        }

        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,}$/;
        if (!passwordRegex.test(data.password)) {
            setError('Пароль должен быть не менее 8 символов, содержать заглавную и строчную буквы, цифру и спецсимвол.');
            return;
        }

        try {
            const { confirmPassword, ...apiData } = data;
            await api.post('/api/register', apiData);
            setMsg('Ссылка для подтверждения отправлена на ваш email.');
        } catch (err) {
            const errorData = err.response?.data;
            let errorMessage = 'Ошибка регистрации. Пожалуйста, проверьте введенные данные.';
            if (typeof errorData === 'string') {
                errorMessage = errorData;
            } else if (errorData && Array.isArray(errorData)) {
                errorMessage = errorData.join(', ');
            } else if (errorData && errorData.message) {
                errorMessage = errorData.message;
            }
            setError(errorMessage);
            console.error("Registration error:", err.response || err);
        }
    };

    const inputClass = "form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#111418] border border-gray-300 bg-white focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500 h-12 placeholder:text-gray-400 p-3 text-base font-normal leading-normal transition duration-150 ease-in-out pr-10";
    const passwordInputContainerClass = "relative flex items-center";
    const toggleButtonClass = "absolute inset-y-0 right-0 flex items-center pr-3 cursor-pointer text-sm text-gray-600 hover:text-gray-800";

    const roleText = initialRole === 'VOLUNTEER' ? 'Регистрация Волонтера' : 'Регистрация (Запрос помощи)';

    return (
        <div className="relative flex size-full min-h-screen flex-col bg-gray-100 group/design-root overflow-x-hidden" style={{ fontFamily: '"Plus Jakarta Sans", "Noto Sans", sans-serif' }}>
            <div className="layout-container flex h-full grow flex-col">
                <div className="px-4 sm:px-10 flex flex-1 justify-center items-center py-10">
                    <div className="layout-content-container flex flex-col w-full max-w-md bg-white shadow-lg rounded-xl p-6 sm:p-8">
                        <div className="mb-6 text-center">
                            <h2 className="text-gray-800 text-2xl font-bold leading-tight tracking-tight">{roleText}</h2>
                        </div>

                        <form onSubmit={handleSubmit} className="space-y-4">
                            <label className="flex flex-col">
                                <span className="text-sm font-medium text-gray-700 mb-1.5">Имя*</span>
                                <input name="firstName" placeholder="Иван" className={inputClass} value={data.firstName} onChange={handleChange} required />
                            </label>

                            <label className="flex flex-col">
                                <span className="text-sm font-medium text-gray-700 mb-1.5">Фамилия*</span>
                                <input name="lastName" placeholder="Иванов" className={inputClass} value={data.lastName} onChange={handleChange} required />
                            </label>

                            <label className="flex flex-col">
                                <span className="text-sm font-medium text-gray-700 mb-1.5">Отчество</span>
                                <input name="middleName" placeholder="Иванович" className={inputClass} value={data.middleName} onChange={handleChange} />
                            </label>

                            <label className="flex flex-col">
                                <span className="text-sm font-medium text-gray-700 mb-1.5">Email*</span>
                                <input name="email" type="email" placeholder="user@example.com" className={inputClass} value={data.email} onChange={handleChange} required />
                            </label>

                            <label className="flex flex-col">
                                <span className="text-sm font-medium text-gray-700 mb-1.5">Пароль*</span>
                                <div className={passwordInputContainerClass}>
                                    <input
                                        name="password"
                                        type={showPassword ? 'text' : 'password'}
                                        placeholder="********"
                                        className={inputClass}
                                        value={data.password}
                                        onChange={handleChange}
                                        required
                                    />
                                    <button
                                        type="button"
                                        onClick={() => setShowPassword(!showPassword)}
                                        className={toggleButtonClass}
                                        aria-label={showPassword ? "Скрыть пароль" : "Показать пароль"}
                                    >
                                        {showPassword ? 'Скрыть' : 'Показать'}
                                    </button>
                                </div>
                            </label>

                            <label className="flex flex-col">
                                <span className="text-sm font-medium text-gray-700 mb-1.5">Подтвердите пароль*</span>
                                <div className={passwordInputContainerClass}>
                                    <input
                                        name="confirmPassword"
                                        type={showConfirmPassword ? 'text' : 'password'}
                                        placeholder="********"
                                        className={inputClass}
                                        value={data.confirmPassword}
                                        onChange={handleChange}
                                        required
                                    />
                                    <button
                                        type="button"
                                        onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                        className={toggleButtonClass}
                                        aria-label={showConfirmPassword ? "Скрыть пароль" : "Показать пароль"}
                                    >
                                        {showConfirmPassword ? 'Скрыть' : 'Показать'}
                                    </button>
                                </div>
                            </label>

                            <div className="pt-1">
                                {error && <p className="text-sm text-red-600">{error}</p>}
                                {msg && <p className="text-sm text-green-600">{msg}</p>}
                            </div>

                            <div className="flex flex-col sm:flex-row gap-3 pt-2">
                                <Link to="/choose-role" className="flex w-full sm:w-auto justify-center items-center rounded-lg h-10 px-4 bg-gray-200 hover:bg-gray-300 text-gray-800 text-sm font-semibold transition duration-150 ease-in-out order-2 sm:order-1">
                                    <span className="truncate">Отмена</span>
                                </Link>
                                <button type="submit" className="flex w-full sm:w-auto flex-1 justify-center items-center rounded-lg h-10 px-4 bg-blue-600 hover:bg-blue-700 text-white text-sm font-semibold transition duration-150 ease-in-out order-1 sm:order-2">
                                    <span className="truncate">Зарегистрироваться</span>
                                </button>
                            </div>
                        </form>
                        <div className="pt-6 text-center">
                            <p className="text-sm text-gray-600">Уже есть аккаунт? <Link to="/login" className="text-blue-600 font-medium hover:underline">Войти</Link></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
