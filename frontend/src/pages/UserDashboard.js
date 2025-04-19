import React from 'react';
import { Link } from 'react-router-dom'; // Import Link for navigation

function UserDashboard() {
    // Placeholder data for tasks
    const tasks = [
        { id: 1, title: "Помощь с покупкой продуктов", status: "Открыта", date: "2024-07-28" },
        { id: 2, title: "Нужна помощь в уборке двора", status: "В процессе", date: "2024-07-25" },
        { id: 3, title: "Сопровождение к врачу", status: "Завершена", date: "2024-07-20" },
    ];

    // Common card class
    const cardClass = "block bg-white border border-gray-200 rounded-xl p-4 hover:bg-gray-50 transition-colors mb-4";
    // Common button class
    const buttonClass = "flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#1980e6] text-white text-sm font-bold leading-normal tracking-[0.015em]";
    // Secondary button class
    const secondaryButtonClass = buttonClass + " bg-[#f0f2f4] text-[#111418]";

    // Function to get status color
    const getStatusClass = (status) => {
        switch (status) {
            case 'Открыта': return 'bg-blue-100 text-blue-800';
            case 'В процессе': return 'bg-yellow-100 text-yellow-800';
            case 'Завершена': return 'bg-green-100 text-green-800';
            default: return 'bg-gray-100 text-gray-800';
        }
    };


    return (
        <div className="relative flex size-full min-h-screen flex-col bg-gray-50 group/design-root overflow-x-hidden" style={{ fontFamily: '"Plus Jakarta Sans", "Noto Sans", sans-serif' }}> {/* Changed background */}
            <div className="layout-container flex h-full grow flex-col">
                {/* Header is handled by App.js */}
                <div className="px-4 sm:px-10 md:px-20 lg:px-40 flex flex-1 justify-center py-5">
                    <div className="layout-content-container flex flex-col w-full max-w-[960px] py-5">

                        <h2 className="text-[#111418] text-3xl font-bold leading-tight tracking-[-0.015em] px-4 py-3 mb-6">Панель пользователя</h2>

                        {/* Section: Create New Task */}
                        <div className="bg-white border border-gray-200 rounded-xl p-6 mb-6 shadow-sm">
                            <div className="flex items-center gap-3 mb-3">
                                {/* Icon Placeholder */}
                                <div className="text-[#1980e6]" data-icon="NotePencil" data-size="24px" data-weight="regular">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor" viewBox="0 0 256 256"><path d="M227.31,73.37,182.63,28.68a16,16,0,0,0-22.63,0L36.69,152A15.86,15.86,0,0,0,32,163.31V208a16,16,0,0,0,16,16H92.69A15.86,15.86,0,0,0,104,219.31L227.31,96a16,16,0,0,0,0-22.63ZM92.69,208H48V163.31l88-88L180.69,120ZM192,108.68,147.31,64l24-24L216,84.68Z"></path></svg>
                                </div>
                                <h3 className="text-[#111418] text-xl font-bold">Создать новую заявку</h3>
                            </div>
                            <p className="text-[#637588] text-base mb-4 ml-9"> {/* Indent text */}
                                Нужна помощь? Опишите вашу задачу, и волонтеры откликнутся.
                            </p>
                            <Link to="/create-task" className="ml-9"> {/* Indent button */}
                                <button className={buttonClass}>
                                    <span className="truncate">Создать заявку</span>
                                </button>
                            </Link>
                        </div>

                        {/* Section: My Tasks */}
                        <div className="bg-white border border-gray-200 rounded-xl p-6 mb-6 shadow-sm">
                            <div className="flex items-center gap-3 mb-4">
                                {/* Icon Placeholder */}
                                <div className="text-[#1980e6]" data-icon="ListChecks" data-size="24px" data-weight="regular">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor" viewBox="0 0 256 256"><path d="M224,64a8,8,0,0,1-8,8H112a8,8,0,0,1,0-16h104A8,8,0,0,1,224,64Zm-8,64H112a8,8,0,0,0,0,16h104a8,8,0,0,0,0-16Zm0,72H112a8,8,0,0,0,0,16h104a8,8,0,0,0,0-16ZM80,56a24,24,0,1,0-24-24A24,24,0,0,0,80,56Zm0,72a24,24,0,1,0-24-24A24,24,0,0,0,80,128Zm0,72a24,24,0,1,0-24-24A24,24,0,0,0,80,200Z"></path></svg>
                                </div>
                                <h3 className="text-[#111418] text-xl font-bold">Мои заявки</h3>
                            </div>
                            {tasks.length > 0 ? (
                                tasks.map(task => (
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
                                                <Link to={`/task/${task.id}`} className="text-[#1980e6] text-sm font-medium hover:underline">
                                                    Подробнее
                                                </Link>
                                            </div>
                                        </div>
                                    </div>
                                ))
                            ) : (
                                <p className="text-[#637588] text-base ml-9">У вас пока нет активных заявок.</p> // Indent text
                            )}
                            <Link to="/my-tasks" className="mt-4 inline-block text-[#1980e6] font-medium ml-9 hover:underline"> {/* Indent link */}
                                Посмотреть все заявки
                            </Link>
                        </div>

                        {/* Section: Profile */}
                        <div className="bg-white border border-gray-200 rounded-xl p-6 shadow-sm">
                            <div className="flex items-center gap-3 mb-3">
                                {/* Icon Placeholder */}
                                <div className="text-[#1980e6]" data-icon="UserCircle" data-size="24px" data-weight="regular">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor" viewBox="0 0 256 256"><path d="M230.92,212c-15.23-26.33-38.7-45.21-66.09-54.16a72,72,0,1,0-73.66,0C63.78,166.78,40.31,185.66,25.08,212a8,8,0,1,0,13.85,8c18.84-32.56,52.14-52,89.07-52s70.23,19.44,89.07,52a8,8,0,1,0,13.85-8ZM72,96a56,56,0,1,1,56,56A56.06,56.06,0,0,1,72,96Z"></path></svg>
                                </div>
                                <h3 className="text-[#111418] text-xl font-bold">Профиль</h3>
                            </div>
                            <p className="text-[#637588] text-base mb-4 ml-9"> {/* Indent text */}
                                Просмотрите и отредактируйте вашу контактную информацию.
                            </p>
                            <Link to="/profile-settings" className="ml-9"> {/* Indent button */}
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

export default UserDashboard;
