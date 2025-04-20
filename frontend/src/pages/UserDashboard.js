import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api'; // Assuming you have an api service configured

function UserDashboard() {
    const [myTasks, setMyTasks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Common card class
    const cardClass = "block bg-white border border-gray-200 rounded-xl p-4 hover:bg-gray-50 transition-colors mb-4 shadow-sm";
    // Common button class
    const buttonClass = "flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#1980e6] text-white text-sm font-bold leading-normal tracking-[0.015em]";

    // Function to get status color (example)
    const getStatusClass = (status) => {
        switch (status) {
            case 'OPEN': return 'bg-blue-100 text-blue-800'; // Match backend enum
            case 'IN_PROGRESS': return 'bg-yellow-100 text-yellow-800'; // Match backend enum
            case 'COMPLETED': return 'bg-green-100 text-green-800'; // Match backend enum
            case 'CANCELLED': return 'bg-red-100 text-red-800'; // Example
            default: return 'bg-gray-100 text-gray-800';
        }
    };

    // Function to format date (optional)
    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        // Assuming backend sends ISO string or similar, adjust formatting as needed
        try {
            const date = new Date(dateString);
            return date.toLocaleDateString('ru-RU'); // Example format
        } catch (e) {
            return dateString; // Fallback
        }
    };


    useEffect(() => {
        const fetchMyTasks = async () => {
            setLoading(true);
            setError(null);
            try {
                // Make sure your api service includes the auth token
                const response = await api.get('/tasks/my-tasks');
                setMyTasks(response.data);
            } catch (err) {
                console.error("Error fetching user tasks:", err);
                setError(err.response?.data?.message || "Не удалось загрузить ваши заявки.");
                // Handle specific errors like 401/403 if needed (e.g., redirect to login)
            } finally {
                setLoading(false);
            }
        };

        fetchMyTasks();
    }, []); // Empty dependency array means this runs once on mount

    return (
        <div className="relative flex size-full min-h-screen flex-col bg-gray-50 group/design-root overflow-x-hidden" style={{ fontFamily: '"Plus Jakarta Sans", "Noto Sans", sans-serif' }}>
            <div className="layout-container flex h-full grow flex-col">
                {/* Header might be handled globally in App.js */}
                <div className="px-4 sm:px-10 md:px-20 lg:px-40 flex flex-1 justify-center py-5">
                    <div className="layout-content-container flex flex-col w-full max-w-[960px] py-5">

                        <h2 className="text-[#111418] text-3xl font-bold leading-tight tracking-[-0.015em] px-4 py-3 mb-6">Панель пользователя</h2>

                        {/* Section: Create New Task */}
                        <div className="bg-white border border-gray-200 rounded-xl p-6 mb-6 shadow-sm">
                            <h3 className="text-[#111418] text-xl font-bold mb-3">Создать новую заявку</h3>
                            <p className="text-[#637588] text-base mb-4">
                                Нужна помощь? Опишите вашу задачу, и волонтеры откликнутся.
                            </p>
                            <Link to="/create-task"> {/* Link to the task creation page */}
                                <button className={buttonClass}>
                                    <span className="truncate">Создать заявку</span>
                                </button>
                            </Link>
                        </div>

                        {/* Section: My Tasks */}
                        <div className="bg-white border border-gray-200 rounded-xl p-6 mb-6 shadow-sm">
                            <h3 className="text-[#111418] text-xl font-bold mb-4">Мои заявки</h3>
                            {loading && <p className="text-[#637588]">Загрузка заявок...</p>}
                            {error && <p className="text-red-600">{error}</p>}
                            {!loading && !error && (
                                myTasks.length > 0 ? (
                                    myTasks.map(task => (
                                        <div key={task.id} className={cardClass}>
                                            <div className="flex flex-col sm:flex-row justify-between sm:items-center gap-2">
                                                <div className="flex-1">
                                                    <p className="text-[#111418] text-base font-bold leading-tight mb-1">{task.title}</p>
                                                    {/* Assuming TaskDTO has a createdAt or similar field */}
                                                    {/* <p className="text-[#637588] text-sm font-normal">Дата создания: {formatDate(task.createdAt)}</p> */}
                                                    <p className="text-[#637588] text-sm font-normal">Категория: {task.category || 'Не указана'}</p> {/* Display category */}
                                                </div>
                                                <div className="flex items-center gap-4 mt-2 sm:mt-0">
                                                    <span className={`inline-block px-2 py-0.5 rounded-full text-xs font-medium ${getStatusClass(task.status)}`}>
                                                        {task.status} {/* Display status from backend */}
                                                    </span>
                                                    {/* Link to task details page */}
                                                    <Link to={`/task/${task.id}`} className="text-[#1980e6] text-sm font-medium hover:underline">
                                                        Подробнее
                                                    </Link>
                                                </div>
                                            </div>
                                        </div>
                                    ))
                                ) : (
                                    <p className="text-[#637588] text-base">У вас пока нет созданных заявок.</p>
                                )
                            )}
                        </div>

                        {/* Optional: Link to Profile Settings */}
                        <div className="bg-white border border-gray-200 rounded-xl p-6 shadow-sm">
                            <h3 className="text-[#111418] text-xl font-bold mb-3">Профиль</h3>
                            <p className="text-[#637588] text-base mb-4">
                                Просмотрите и отредактируйте вашу контактную информацию.
                            </p>
                            <Link to="/profile-settings">
                                <button className={`${buttonClass} bg-[#f0f2f4] text-[#111418]`}> {/* Secondary style */}
                                    <span className="truncate">Настройки профиля</span>
                                </button>
                            </Link>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    );
}

export default UserDashboard;