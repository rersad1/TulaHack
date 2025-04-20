import React from 'react';
import { useNavigate } from 'react-router-dom'; // Импортируем useNavigate

// Стили, перенесенные или адаптированные из Profile.js
const cardContainerStyle = "flex flex-col md:flex-row gap-6 md:gap-8"; // Flex container for sidebar and content
const sidebarStyle = "w-full md:w-1/4 flex flex-col gap-3"; // Sidebar style
const buttonStyle = "w-full text-left px-4 py-2 rounded-lg border border-gray-300 bg-white hover:bg-gray-100 transition-colors duration-150 text-sm font-medium text-gray-700"; // Button style
const contentAreaStyle = "w-full md:w-3/4"; // Content area style
const cardStyle = "bg-white border border-gray-200 rounded-xl p-6 md:p-8 shadow-lg"; // Card style remains for the content
const cardTitleStyle = "text-[#111418] text-xl md:text-2xl font-bold mb-6 border-b pb-3 border-gray-200";
const infoRowStyle = "flex flex-col sm:flex-row justify-between items-start sm:items-center mb-4 pb-4 border-b border-gray-100 last:border-b-0 last:mb-0 last:pb-0";
const labelStyle = "font-semibold text-gray-600 mb-1 sm:mb-0 w-full sm:w-1/3";
const valueStyle = "text-[#111418] text-base sm:text-right w-full sm:w-2/3";
const textStyle = "text-gray-700";

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

function UserProfileCard({ userData }) {
    const navigate = useNavigate(); // Инициализируем navigate

    // Обработчики для кнопок
    const handleCreateRequest = () => {
        navigate('/create-request'); // Переход на страницу создания заявки
    };

    const handleViewHistory = () => {
        console.log("Переход на страницу истории заявок");
        // navigate('/request-history'); // Пример навигации (раскомментировать и настроить маршрут)
    };

    if (!userData) {
        return <div className={cardStyle}><p className={textStyle}>Загрузка данных...</p></div>;
    }

    return (
        <div className={cardContainerStyle}>
            {/* Sidebar */}
            <div className={sidebarStyle}>
                <button onClick={handleCreateRequest} className={buttonStyle}>
                    Создать заявку
                </button>
                <button onClick={handleViewHistory} className={buttonStyle}>
                    История заявок
                </button>
            </div>

            {/* Main Content Area */}
            <div className={contentAreaStyle}>
                <div className={cardStyle}>
                    <h3 className={cardTitleStyle}>Ваши данные</h3>
                    <div>
                        <div className={infoRowStyle}>
                            <span className={labelStyle}>Фамилия:</span>
                            <span className={valueStyle}>{userData.lastName}</span>
                        </div>
                        <div className={infoRowStyle}>
                            <span className={labelStyle}>Имя:</span>
                            <span className={valueStyle}>{userData.firstName}</span>
                        </div>
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
                        {userData.phoneNumber && (
                            <div className={infoRowStyle}>
                                <span className={labelStyle}>Телефон:</span>
                                <span className={valueStyle}>{userData.phoneNumber}</span>
                            </div>
                        )}
                        <div className={infoRowStyle}>
                            <span className={labelStyle}>Роль:</span>
                            <span className={valueStyle}>{displayRole(userData.role)}</span>
                        </div>
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
                                    <span className={valueStyle}>{displayRating(userData.rating)}</span>
                                </div>
                            </>
                        )}
                        <p className="mt-6 text-sm text-gray-500 text-center">(Данные-заглушка)</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default UserProfileCard;
