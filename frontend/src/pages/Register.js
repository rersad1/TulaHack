import React, { useState, useEffect } from 'react';
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
    const [activeTab, setActiveTab] = useState(initialRole);

    const handleTabClick = (role) => {
        setActiveTab(role);
        setData(prevData => ({ ...prevData, role: role }));
    };

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
            const { confirmPassword, ...apiData } = { ...data, role: activeTab };
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

    const inputClass = "form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#111418] focus:outline-0 focus:ring-0 border-none bg-[#f0f2f4] focus:border-none h-14 placeholder:text-[#637588] p-4 text-base font-normal leading-normal";

    return (
        <div className="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden" style={{ fontFamily: '"Plus Jakarta Sans", "Noto Sans", sans-serif' }}>
            <div className="layout-container flex h-full grow flex-col">
                <div className="px-4 sm:px-10 md:px-20 lg:px-40 flex flex-1 justify-center py-5">
                    <div className="layout-content-container flex flex-col w-full max-w-[512px] py-5">
                        <div className="pb-3">
                            <div className="flex border-b border-[#dce0e5] px-4 gap-8">
                                <button
                                    onClick={() => handleTabClick('VOLUNTEER')}
                                    className={`flex flex-col items-center justify-center border-b-[3px] pb-[13px] pt-4 ${activeTab === 'VOLUNTEER' ? 'border-b-[#111418] text-[#111418]' : 'border-b-transparent text-[#637588]'}`}
                                >
                                    <p className="text-sm font-bold leading-normal tracking-[0.015em]">Волонтер</p>
                                </button>
                                <button
                                    onClick={() => handleTabClick('USER')}
                                    className={`flex flex-col items-center justify-center border-b-[3px] pb-[13px] pt-4 ${activeTab === 'USER' ? 'border-b-[#111418] text-[#111418]' : 'border-b-transparent text-[#637588]'}`}
                                >
                                    <p className="text-sm font-bold leading-normal tracking-[0.015em]">Запросить помощь</p>
                                </button>
                            </div>
                        </div>

                        <form onSubmit={handleSubmit}>
                            <div className="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
                                <label className="flex flex-col min-w-40 flex-1">
                                    <span className="text-sm font-medium text-[#111418] mb-1">Имя*</span>
                                    <input name="firstName" placeholder="Иван" className={inputClass} value={data.firstName} onChange={handleChange} required />
                                </label>
                            </div>
                            <div className="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
                                <label className="flex flex-col min-w-40 flex-1">
                                    <span className="text-sm font-medium text-[#111418] mb-1">Фамилия*</span>
                                    <input name="lastName" placeholder="Иванов" className={inputClass} value={data.lastName} onChange={handleChange} required />
                                </label>
                            </div>
                            <div className="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
                                <label className="flex flex-col min-w-40 flex-1">
                                    <span className="text-sm font-medium text-[#111418] mb-1">Отчество</span>
                                    <input name="middleName" placeholder="Иванович" className={inputClass} value={data.middleName} onChange={handleChange} />
                                </label>
                            </div>
                            <div className="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
                                <label className="flex flex-col min-w-40 flex-1">
                                    <span className="text-sm font-medium text-[#111418] mb-1">Email*</span>
                                    <input name="email" type="email" placeholder="user@example.com" className={inputClass} value={data.email} onChange={handleChange} required />
                                </label>
                            </div>
                            <div className="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
                                <label className="flex flex-col min-w-40 flex-1">
                                    <span className="text-sm font-medium text-[#111418] mb-1">Пароль*</span>
                                    <input name="password" type="password" placeholder="********" className={inputClass} value={data.password} onChange={handleChange} required />
                                </label>
                            </div>
                            <div className="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
                                <label className="flex flex-col min-w-40 flex-1">
                                    <span className="text-sm font-medium text-[#111418] mb-1">Подтвердите пароль*</span>
                                    <input name="confirmPassword" type="password" placeholder="********" className={inputClass} value={data.confirmPassword} onChange={handleChange} required />
                                </label>
                            </div>

                            {error && <p className="px-4 py-2 text-red-600">{error}</p>}
                            {msg && <p className="px-4 py-2 text-green-600">{msg}</p>}

                            <div className="flex justify-stretch">
                                <div className="flex flex-1 gap-3 flex-wrap px-4 py-3 justify-start">
                                    <Link to="/choose-role" className="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#f0f2f4] text-[#111418] text-sm font-bold leading-normal tracking-[0.015em]">
                                        <span className="truncate">Отмена</span>
                                    </Link>
                                    <button type="submit" className="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#1980e6] text-white text-sm font-bold leading-normal tracking-[0.015em]">
                                        <span className="truncate">Зарегистрироваться</span>
                                    </button>
                                </div>
                            </div>
                        </form>
                        <div className="px-4 py-3 text-center">
                            <p className="text-sm text-[#637588]">Уже есть аккаунт? <Link to="/login" className="text-[#1980e6] font-medium">Войти</Link></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
