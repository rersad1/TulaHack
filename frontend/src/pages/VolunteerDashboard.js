import React, { useState, useEffect, useMemo } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios'; // Assuming axios is installed and configured for API calls
// import { FiFilter, FiBriefcase, FiUser, FiGrid, FiSearch, FiHome } from 'react-icons/fi'; // Пример иконок

function VolunteerDashboard() { // Возвращаем имя функции
    // Состояния для фильтров и данных
    const [filterCategory, setFilterCategory] = useState('all');
    const [filterLocationType, setFilterLocationType] = useState('all');
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Стили
    const buttonClass = "flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#1980e6] text-white text-sm font-bold leading-normal tracking-[0.015em] hover:bg-blue-700 transition-colors duration-200";
    const cardClass = "block bg-white border border-gray-200 rounded-xl p-4 md:p-6 hover:shadow-md transition-shadow duration-200 mb-4";

    // Загрузка данных
    useEffect(() => {
        const fetchTasks = async () => {
            setLoading(true);
            setError(null);
            try {
                // Замените '/api/tasks' на ваш реальный эндпоинт
                // const response = await api.get('/api/tasks');
                // setTasks(response.data.filter(task => task.status === 'OPEN') || []);

                // Заглушка данных
                const mockTasks = [
                    { id: 1, title: "Помощь с продуктами", description: "Нужно купить продукты пожилому человеку", status: 'OPEN', category: 'SOCIAL', locationType: 'OFFLINE', userEmail: 'user1@example.com' },
                    { id: 2, title: "Уборка парка", description: "Субботник в парке 'Центральный'", status: 'OPEN', category: 'ECOLOGY', locationType: 'OFFLINE', userEmail: 'user2@example.com' },
                    { id: 3, title: "Онлайн-урок английского", description: "Помощь школьнику с домашним заданием", status: 'OPEN', category: 'EDUCATION', locationType: 'ONLINE', userEmail: 'user3@example.com' },
                    { id: 4, title: "Сопроводить в больницу", description: "Нужна помощь в сопровождении до поликлиники", status: 'OPEN', category: 'MEDICAL', locationType: 'OFFLINE', userEmail: 'user4@example.com' },
                    { id: 5, title: "Помощь с переездом", description: "Нужно помочь перенести легкие вещи", status: 'OPEN', category: 'OTHER', locationType: 'OFFLINE', userEmail: 'user5@example.com' },
                ];
                await new Promise(resolve => setTimeout(resolve, 500)); // Имитация задержки
                setTasks(mockTasks);

            } catch (err) {
                console.error("Error fetching tasks:", err);
                setError("Не удалось загрузить заявки. Попробуйте позже.");
                setTasks([]);
            } finally {
                setLoading(false);
            }
        };
        fetchTasks();
    }, []);

    // Вспомогательные функции
    const getStatusClass = (status) => {
        switch (status) {
            case 'OPEN': return 'bg-blue-100 text-blue-800';
            case 'IN_PROGRESS': return 'bg-yellow-100 text-yellow-800';
            case 'COMPLETED': return 'bg-green-100 text-green-800';
            case 'CANCELLED': return 'bg-red-100 text-red-800';
            default: return 'bg-gray-100 text-gray-800';
        }
    };

    const getCategoryIcon = (category) => {
        switch (category) {
            case 'SOCIAL': return '🤝';
            case 'ECOLOGY': return '🌳';
            case 'EDUCATION': return '📚';
            case 'MEDICAL': return '⚕️';
            default: return '📌';
        }
    };

    // Фильтрация задач
    const filteredTasks = useMemo(() => {
        return tasks.filter(task => {
            const categoryMatch = filterCategory === 'all' || task.category === filterCategory;
            const locationMatch = filterLocationType === 'all' || task.locationType === filterLocationType;
            return categoryMatch && locationMatch; // Статус уже OPEN из fetch
        });
    }, [tasks, filterCategory, filterLocationType]);

    // Компонент Sidebar
    const Sidebar = () => (
        <div className="flex flex-col w-64 md:w-72 bg-white p-4 border-r border-gray-200 flex-shrink-0 h-screen sticky top-0">
            <div className="flex flex-col gap-4 flex-grow">
                <div className="flex items-center gap-3 mb-4">
                    <div
                        className="bg-center bg-no-repeat aspect-square bg-cover rounded-full size-10"
                        style={{ backgroundImage: 'url("https://cdn.usegalileo.ai/sdxl10/4ac9fb50-6ae0-44b6-994d-caf00c3bdb32.png")' }}
                    ></div>
                    <span className="font-semibold">Волонтер</span>
                </div>
                <div className="flex flex-col gap-1">
                    {/* Ссылка на эту же страницу (Найти заявки) теперь активна */}
                    <div className="flex items-center gap-3 px-3 py-2 rounded-lg bg-[#f0f2f4]">
                        <span className="text-gray-700">🔍</span>
                        <p className="text-[#111418] text-sm font-medium leading-normal">Найти заявки</p>
                    </div>
                    {/* Ссылка на другую панель (если есть) */}
                    {/* <Link to="/volunteer-dashboard-overview" className="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-gray-100">
                        <span className="text-gray-700">📊</span>
                        <p className="text-[#111418] text-sm font-medium leading-normal">Обзор</p>
                    </Link> */}
                    <Link to="/profile" className="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-gray-100">
                        <span className="text-gray-700">👤</span>
                        <p className="text-[#111418] text-sm font-medium leading-normal">Профиль</p>
                    </Link>
                </div>
            </div>
            <div className="mt-auto pt-4 border-t border-gray-200">
                <Link to="/" className="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-gray-100">
                    <span className="text-gray-700">🏠</span>
                    <p className="text-[#111418] text-sm font-medium leading-normal">На главную</p>
                </Link>
            </div>
        </div>
    );

    // Основной рендер компонента
    return (
        <div className="relative flex size-full min-h-screen flex-row bg-gray-50 group/design-root overflow-x-hidden" style={{ fontFamily: '"Public Sans", "Noto Sans", sans-serif' }}>
            <Sidebar />
            <div className="layout-content-container flex flex-col flex-1 overflow-y-auto">
                <div className="p-4 md:p-6 lg:p-8">
                    {/* Заголовок */}
                    <div className="flex flex-wrap justify-between gap-3 mb-6">
                        <div className="flex flex-col gap-2">
                            <p className="text-[#111418] tracking-light text-2xl md:text-3xl font-bold leading-tight">Найти заявку</p>
                            <p className="text-[#637588] text-sm font-normal leading-normal max-w-xl">
                                Просматривайте заявки от людей в вашем районе или фильтруйте по категориям, чтобы найти возможности, соответствующие вашим интересам.
                            </p>
                        </div>
                    </div>
                    {/* Фильтры */}
                    <div className="flex gap-2 md:gap-3 p-3 flex-wrap mb-6 border-b border-gray-200 pb-4">
                        <div className="flex h-8 shrink-0 items-center justify-center gap-x-2 rounded-xl bg-blue-100 text-blue-800 px-3 md:px-4">
                            <p className="text-sm font-medium leading-normal">Статус: Открытые</p>
                        </div>
                        <div className="flex h-8 shrink-0 items-center justify-center gap-x-1 rounded-xl bg-[#f0f2f4] px-1 md:px-2">
                            <label htmlFor="categoryFilterTag" className="text-[#111418] text-sm font-medium leading-normal pl-2">Категория:</label>
                            <select
                                id="categoryFilterTag"
                                name="categoryFilterTag"
                                className="rounded-lg border-none bg-transparent focus:ring-0 text-sm font-medium"
                                value={filterCategory}
                                onChange={(e) => setFilterCategory(e.target.value)}
                            >
                                <option value="all">Все</option>
                                <option value="SOCIAL">Соц. помощь</option>
                                <option value="ECOLOGY">Экология</option>
                                <option value="EDUCATION">Образование</option>
                                <option value="MEDICAL">Мед. помощь</option>
                                <option value="OTHER">Другое</option>
                            </select>
                        </div>
                        <div className="flex h-8 shrink-0 items-center justify-center gap-x-1 rounded-xl bg-[#f0f2f4] px-1 md:px-2">
                            <label htmlFor="locationFilterTag" className="text-[#111418] text-sm font-medium leading-normal pl-2">Тип:</label>
                            <select
                                id="locationFilterTag"
                                name="locationFilterTag"
                                className="rounded-lg border-none bg-transparent focus:ring-0 text-sm font-medium"
                                value={filterLocationType}
                                onChange={(e) => setFilterLocationType(e.target.value)}
                            >
                                <option value="all">Все</option>
                                <option value="OFFLINE">Офлайн</option>
                                <option value="ONLINE">Онлайн</option>
                            </select>
                        </div>
                    </div>
                    {/* Список задач */}
                    <div className="space-y-4">
                        {loading ? (
                            <p className="text-[#637588] text-base text-center py-10">Загрузка заявок...</p>
                        ) : error ? (
                            <p className="text-red-600 text-base text-center py-10">{error}</p>
                        ) : filteredTasks.length > 0 ? (
                            filteredTasks.map(task => (
                                <div key={task.id} className={cardClass}>
                                    <div className="flex flex-col md:flex-row justify-between md:items-center gap-3 md:gap-4">
                                        <div className="flex-1">
                                            <div className="flex items-center mb-1">
                                                <span className="text-xl mr-2">{getCategoryIcon(task.category)}</span>
                                                <p className="text-[#111418] text-lg font-semibold leading-tight">{task.title}</p>
                                            </div>
                                            <p className="text-[#637588] text-sm font-normal ml-8">
                                                Категория: {
                                                    {
                                                        'SOCIAL': 'Социальная помощь',
                                                        'ECOLOGY': 'Экология',
                                                        'EDUCATION': 'Образование',
                                                        'MEDICAL': 'Медицинская помощь',
                                                        'OTHER': 'Другое'
                                                    }[task.category] || task.category || 'Не указана'
                                                }
                                            </p>
                                            <p className="text-[#637588] text-xs font-normal ml-8 mt-1">
                                                Тип: {task.locationType === 'ONLINE' ? 'Онлайн' : task.locationType === 'OFFLINE' ? 'Офлайн' : 'Не указан'}
                                            </p>
                                        </div>
                                        <div className="flex items-center justify-end gap-3 md:gap-4 mt-2 md:mt-0 flex-wrap">
                                            <span className={`inline-block px-3 py-1 rounded-full text-xs font-medium ${getStatusClass(task.status)}`}>
                                                {task.status}
                                            </span>
                                            <Link to={`/task/${task.id}`} className="text-[#1980e6] text-sm font-medium hover:underline whitespace-nowrap">
                                                Подробнее
                                            </Link>
                                            {/* TODO: Реализовать логику принятия заявки */}
                                            <button className={`${buttonClass} h-9 px-3 text-xs whitespace-nowrap`}>
                                                Взять заявку
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            ))
                        ) : (
                            <p className="text-[#637588] text-base text-center py-10">Нет доступных заявок, соответствующих вашим фильтрам.</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default VolunteerDashboard; // Возвращаем правильный экспорт
