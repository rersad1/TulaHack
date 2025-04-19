import React from 'react';
import { Link } from 'react-router-dom';

function UserDashboard() {
    // Placeholder data for user's tasks
    const myTasks = [
        { id: 1, title: "Помощь с покупкой продуктов", status: "Открыта", date: "2024-07-28" },
        { id: 3, title: "Сопровождение к врачу", status: "Завершена", date: "2024-07-20" },
    ];

    // Common card class
    const cardClass = "block bg-white border border-gray-200 rounded-xl p-4 hover:bg-gray-50 transition-colors mb-4 shadow-sm";
    // Common button class
    const buttonClass = "flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#1980e6] text-white text-sm font-bold leading-normal tracking-[0.015em]";

    // Function to get status color (example)
    const getStatusClass = (status) => {
        switch (status) {
            case 'Открыта': return 'bg-blue-100 text-blue-800';
            case 'В процессе': return 'bg-yellow-100 text-yellow-800';
            case 'Завершена': return 'bg-green-100 text-green-800';
            default: return 'bg-gray-100 text-gray-800';
        }
    };

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
                            {myTasks.length > 0 ? (
                                myTasks.map(task => (
                                    <div key={task.id} className={cardClass}>
                                        <div className="flex flex-col sm:flex-row justify-between sm:items-center gap-2">
                                            <div className="flex-1">
                                                <p className="text-[#111418] text-base font-bold leading-tight mb-1">{task.title}</p>
                                                <p className="text-[#637588] text-sm font-normal">Дата: {task.date}</p>
                                            </div>
                                            <div className="flex items-center gap-4 mt-2 sm:mt-0">
                                                <span className={`inline-block px-2 py-0.5 rounded-full text-xs font-medium ${getStatusClass(task.status)}`}>
                                                    {task.status}
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
                                <p className="text-[#637588] text-base">У вас пока нет активных заявок.</p>
                            )}
                            {/* Optional: Link to view all tasks */}
                            {/* <Link to="/my-tasks" className="mt-4 inline-block text-[#1980e6] font-medium hover:underline">Посмотреть все заявки</Link> */}
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
