import React, { useState } from 'react';
import { Link } from 'react-router-dom';

function VolunteerDashboard() {
    // Placeholder data for available tasks
    const availableTasks = [
        { id: 1, title: "Помощь с покупкой продуктов", status: "Открыта", date: "2024-07-28", requester: "Иван П." },
        { id: 2, title: "Нужна помощь в уборке двора", status: "Открыта", date: "2024-07-25", requester: "Мария С." },
        { id: 4, title: "Помощь с мелким ремонтом", status: "Открыта", date: "2024-07-29", requester: "Петр В." },
    ];

    // State for filters (example)
    const [filterStatus, setFilterStatus] = useState('all'); // 'all', 'open', 'in_progress'
    const [filterCategory, setFilterCategory] = useState('all'); // Example categories

    // Common card class
    const cardClass = "block bg-white border border-gray-200 rounded-xl p-4 hover:bg-gray-50 transition-colors mb-4 shadow-sm";
    // Common button class
    const buttonClass = "flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#1980e6] text-white text-sm font-bold leading-normal tracking-[0.015em]";
    const secondaryButtonClass = `${buttonClass} bg-[#f0f2f4] text-[#111418]`;

    // Placeholder function to apply filters (implement actual logic later)
    const filteredTasks = availableTasks.filter(task => {
        // Add filtering logic based on filterStatus, filterCategory, etc.
        return true; // Show all for now
    });

    // Function to get status color (example)
    const getStatusClass = (status) => {
        switch (status) {
            case 'Открыта': return 'bg-blue-100 text-blue-800';
            // Add other statuses if needed
            default: return 'bg-gray-100 text-gray-800';
        }
    };

    return (
        <div className="relative flex size-full min-h-screen flex-col bg-gray-50 group/design-root overflow-x-hidden" style={{ fontFamily: '"Plus Jakarta Sans", "Noto Sans", sans-serif' }}>
            <div className="layout-container flex h-full grow flex-col">
                {/* Header might be handled globally in App.js */}
                <div className="px-4 sm:px-10 md:px-20 lg:px-40 flex flex-1 justify-center py-5">
                    <div className="layout-content-container flex flex-col w-full max-w-[960px] py-5">

                        <h2 className="text-[#111418] text-3xl font-bold leading-tight tracking-[-0.015em] px-4 py-3 mb-6">Панель волонтера</h2>

                        {/* Section: Filters */}
                        <div className="bg-white border border-gray-200 rounded-xl p-6 mb-6 shadow-sm">
                            <h3 className="text-[#111418] text-xl font-bold mb-4">Фильтры заявок</h3>
                            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                                {/* Placeholder Filters */}
                                <div>
                                    <label htmlFor="statusFilter" className="block text-sm font-medium text-gray-700 mb-1">Статус</label>
                                    <select
                                        id="statusFilter"
                                        name="statusFilter"
                                        className="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm h-10 px-3"
                                        value={filterStatus}
                                        onChange={(e) => setFilterStatus(e.target.value)}
                                    >
                                        <option value="all">Все</option>
                                        <option value="open">Открытые</option>
                                        {/* Add more statuses */}
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="categoryFilter" className="block text-sm font-medium text-gray-700 mb-1">Категория</label>
                                    <select
                                        id="categoryFilter"
                                        name="categoryFilter"
                                        className="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm h-10 px-3"
                                        value={filterCategory}
                                        onChange={(e) => setFilterCategory(e.target.value)}
                                    >
                                        <option value="all">Все</option>
                                        <option value="groceries">Продукты</option>
                                        <option value="cleaning">Уборка</option>
                                        <option value="medical">Мед. сопровождение</option>
                                        {/* Add more categories */}
                                    </select>
                                </div>
                            </div>
                            {/* Add more filters like location, date range etc. */}
                        </div>

                        {/* Section: Available Tasks */}
                        <div className="bg-white border border-gray-200 rounded-xl p-6 mb-6 shadow-sm">
                            <h3 className="text-[#111418] text-xl font-bold mb-4">Доступные заявки</h3>
                            {filteredTasks.length > 0 ? (
                                filteredTasks.map(task => (
                                    <div key={task.id} className={cardClass}>
                                        <div className="flex flex-col sm:flex-row justify-between sm:items-center gap-2">
                                            <div className="flex-1">
                                                <p className="text-[#111418] text-base font-bold leading-tight mb-1">{task.title}</p>
                                                <p className="text-[#637588] text-sm font-normal">Заявитель: {task.requester} | Дата: {task.date}</p>
                                            </div>
                                            <div className="flex items-center gap-4 mt-2 sm:mt-0">
                                                <span className={`inline-block px-2 py-0.5 rounded-full text-xs font-medium ${getStatusClass(task.status)}`}>
                                                    {task.status}
                                                </span>
                                                {/* Link to task details page */}
                                                <Link to={`/task/${task.id}`} className="text-[#1980e6] text-sm font-medium hover:underline">
                                                    Подробнее
                                                </Link>
                                                {/* Placeholder button to accept task */}
                                                <button className={`${buttonClass} h-8 px-3 text-xs`}>
                                                    Взять заявку
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                ))
                            ) : (
                                <p className="text-[#637588] text-base">Нет доступных заявок, соответствующих вашим фильтрам.</p>
                            )}
                        </div>

                        {/* Optional: Link to Profile Settings */}
                        <div className="bg-white border border-gray-200 rounded-xl p-6 shadow-sm">
                            <h3 className="text-[#111418] text-xl font-bold mb-3">Профиль</h3>
                            <p className="text-[#637588] text-base mb-4">
                                Просмотрите и отредактируйте вашу контактную информацию и настройки волонтера.
                            </p>
                            <Link to="/profile-settings">
                                <button className={secondaryButtonClass}>
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

export default VolunteerDashboard;
