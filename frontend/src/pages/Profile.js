import React, { useEffect, useState } from 'react';
import api from '../services/api'; // Импортируем настроенный api

function Profile() {
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        // Пример запроса к защищенному эндпоинту для получения данных пользователя
        // Замените '/api/users/me' на ваш реальный эндпоинт
        const fetchUserData = async () => {
            try {
                // Запрос будет автоматически включать 'Authorization: Bearer <token>'
                // благодаря interceptor в api.js
                // const response = await api.get('/users/me'); // Пример эндпоинта
                // setUserData(response.data);
                setUserData({ message: "Данные пользователя загружены (заглушка)" }); // Заглушка
                setLoading(false);
            } catch (err) {
                setError('Не удалось загрузить данные пользователя. Возможно, требуется войти снова.');
                setLoading(false);
                console.error("Profile fetch error:", err);
                // Здесь можно добавить логику для обновления токена, если ошибка 401
            }
        };

        fetchUserData();
    }, []); // Пустой массив зависимостей означает, что эффект выполнится один раз при монтировании

    if (loading) {
        return <p>Загрузка профиля...</p>;
    }

    if (error) {
        return <p style={{ color: 'red' }}>{error}</p>;
    }

    return (
        <div>
            <h2>Профиль пользователя</h2>
            {userData ? (
                <pre>{JSON.stringify(userData, null, 2)}</pre>
            ) : (
                <p>Нет данных для отображения.</p>
            )}
            {/* Здесь можно отобразить данные пользователя */}
        </div>
    );
}

export default Profile;
