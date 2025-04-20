import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Импортируем useNavigate
import api from '../services/api'; // Импортируем настроенный api

function Profile() {
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate(); // Инициализируем useNavigate

    useEffect(() => {
        const fetchUserData = async () => {
            setLoading(true);
            setError('');
            try {
                const response = await api.get('/api/profile');
                setUserData(response.data);
            } catch (err) {
                setError('Не удалось загрузить данные пользователя.');
                console.error("Profile fetch error:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchUserData();
    }, []);

    // Логика выхода из аккаунта
    const handleLogout = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('userRole');
        delete api.defaults.headers.common['Authorization']; // Удаляем заголовок из axios по умолчанию
        navigate('/'); // Перенаправляем на главную страницу
        window.location.reload(); // Перезагружаем страницу для обновления состояния
    };

    // Стили для страницы
    const pageStyle = "relative flex size-full min-h-screen flex-col bg-gradient-to-b from-gray-50 to-gray-100 group/design-root overflow-x-hidden"; // Легкий градиент фона
    const containerStyle = "px-4 sm:px-6 md:px-10 lg:px-20 xl:px-40 flex flex-1 justify-center py-8 md:py-10"; // Увеличены отступы
    const contentBoxStyle = "layout-content-container flex flex-col w-full max-w-2xl py-5"; // Уменьшил max-w для профиля
    const cardStyle = "bg-white border border-gray-200 rounded-xl p-6 md:p-8 mb-6 shadow-lg"; // Увеличил padding и тень
    const titleStyle = "text-[#111418] text-3xl md:text-4xl font-bold leading-tight tracking-tight px-4 py-3 mb-8 text-center"; // Центрировал заголовок
    const cardTitleStyle = "text-[#111418] text-xl md:text-2xl font-bold mb-6 border-b pb-3 border-gray-200"; // Добавил разделитель под заголовком карточки
    const infoRowStyle = "flex flex-col sm:flex-row justify-between items-start sm:items-center mb-4 pb-4 border-b border-gray-100 last:border-b-0 last:mb-0 last:pb-0"; // Стиль для строки информации с разделителем
    const labelStyle = "font-semibold text-gray-600 mb-1 sm:mb-0 w-full sm:w-1/3"; // Уточнил стиль метки
    const valueStyle = "text-[#111418] text-base sm:text-right w-full sm:w-2/3"; // Стиль для значения
    const logoutButtonStyle = "mt-8 w-full sm:w-auto flex min-w-[150px] cursor-pointer items-center justify-center gap-2 overflow-hidden rounded-xl h-10 px-4 bg-red-500 text-white text-sm font-bold leading-normal tracking-[0.015em] hover:bg-red-600 transition-colors duration-200";

    // Функция для отображения роли
    const displayRole = (role) => {
        if (role === 'VOLUNTEER') return 'Волонтер';
        if (role === 'USER') return 'Пользователь';
        return role; // Возвращаем как есть, если не VOLUNTEER или USER
    };

    // Функция для отображения рейтинга (опционально)
    const displayRating = (rating) => {
        if (rating === null || rating === undefined) return 'Нет оценки';
        return `${rating.toFixed(1)} / 5.0`; // Форматируем до одного знака после запятой
    };

    if (loading) {
        return <div className={pageStyle}><div className={containerStyle}><p>Загрузка профиля...</p></div></div>;
    }

    if (error) {
        return <div className={pageStyle}><div className={containerStyle}><p style={{ color: 'red' }}>{error}</p></div></div>;
    }

    return (
        <div className={pageStyle} style={{ fontFamily: '"Plus Jakarta Sans", "Noto Sans", sans-serif' }}>
            <div className="layout-container flex h-full grow flex-col">
                <div className={containerStyle}>
                    <div className={contentBoxStyle}>
                        <h2 className={titleStyle}>
                            Профиль
                        </h2>
                        <div className={cardStyle}>
                            <h3 className={cardTitleStyle}>Ваши данные</h3>
                            {userData ? (
                                <div>
                                    {/* Используем infoRowStyle для каждой строки */}
                                    <div className={infoRowStyle}>
                                        <span className={labelStyle}>Фамилия:</span>
                                        <span className={valueStyle}>{userData.lastName}</span>
                                    </div>
                                    <div className={infoRowStyle}>
                                        <span className={labelStyle}>Имя:</span>
                                        <span className={valueStyle}>{userData.firstName}</span>
                                    </div>
                                    {/* Отображаем отчество, если оно есть */}
                                    {userData.middleName && (
                                        <div className={infoRowStyle}>
                                            <span className={labelStyle}>Отчество:</span>
                                            <span className={valueStyle}>{userData.middleName}</span>
                                        </div>
                                    )}
                                    <div className={infoRowStyle}>
                                        <span className={labelStyle}>Email:</span>
                                        <span className={valueStyle}>{userData.email}</span>
                                    </div>
                                    {/* Отображаем телефон, если он есть */}
                                    {userData.phoneNumber && (
                                        <div className={infoRowStyle}>
                                            <span className={labelStyle}>Телефон:</span>
                                            <span className={valueStyle}>{userData.phoneNumber}</span>
                                        </div>
                                    )}
                                    <div className={infoRowStyle}>
                                        <span className={labelStyle}>Роль:</span>
                                        {/* Используем функцию displayRole */}
                                        <span className={valueStyle}>{displayRole(userData.role)}</span>
                                    </div>
                                    {/* Отображаем компанию и рейтинг только для волонтеров */}
                                    {userData.role === 'VOLUNTEER' && (
                                        <>
                                            {userData.organization && (
                                                <div className={infoRowStyle}>
                                                    <span className={labelStyle}>Компания:</span>
                                                    <span className={valueStyle}>{userData.organization}</span>
                                                </div>
                                            )}
                                            <div className={infoRowStyle}>
                                                <span className={labelStyle}>Рейтинг:</span>
                                                {/* Используем функцию displayRating */}
                                                <span className={valueStyle}>{displayRating(userData.rating)}</span>
                                            </div>
                                        </>
                                    )}

                                    <p className="mt-6 text-sm text-gray-500 text-center">(Данные-заглушка)</p>

                                    {/* Кнопка Выход */}
                                    <div className="flex justify-center">
                                        <button onClick={handleLogout} className={logoutButtonStyle}>
                                            Выйти из аккаунта
                                        </button>
                                    </div>
                                </div>
                            ) : (
                                <p className={textStyle}>Нет данных для отображения.</p>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Profile;
