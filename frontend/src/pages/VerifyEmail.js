import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api'; // Убедитесь, что путь к api.js верный

function VerifyEmail() {
    const { token } = useParams(); // Получаем токен из URL
    const navigate = useNavigate();
    const [verificationStatus, setVerificationStatus] = useState('pending'); // 'pending', 'success', 'error'
    const [message, setMessage] = useState('Подтверждение email...');

    useEffect(() => {
        const verify = async () => {
            if (!token) {
                setVerificationStatus('error');
                setMessage('Токен верификации отсутствует.');
                return;
            }

            try {
                // Отправляем GET запрос на бэкенд с токеном в query параметрах
                // Бэкенд ожидает токен в @RequestParam("token")
                // Ожидаем ответ с токенами и ролью
                const response = await api.get(`/verify-email?token=${token}`);
                const { accessToken, refreshToken, role } = response.data;

                // Сохраняем токены и роль в localStorage
                localStorage.setItem('accessToken', accessToken);
                localStorage.setItem('refreshToken', refreshToken);
                localStorage.setItem('userRole', role);

                setVerificationStatus('success');
                setMessage('Email успешно подтвержден! Вы вошли в систему.');

                // Определяем путь для редиректа на основе роли
                const dashboardPath = role === 'VOLUNTEER' ? '/volunteer-dashboard' : '/user-dashboard';

                // Перенаправляем на соответствующую панель через 3 секунды
                setTimeout(() => {
                    navigate(dashboardPath, { replace: true });
                    window.location.reload(); // Перезагружаем страницу для обновления состояния App
                }, 3000);

            } catch (err) {
                setVerificationStatus('error');
                // Используем более общее сообщение или специфичное из ответа сервера
                const errorMsg = err.response?.data?.message || err.response?.data || 'Ошибка подтверждения email.';

                // Уточняем сообщение об ошибке для истекшего/невалидного токена
                if (errorMsg.includes("Токен устарел") || (err.response?.status === 401 && errorMsg.toLowerCase().includes("expired"))) {
                    setMessage('Ссылка для подтверждения истекла. Пожалуйста, запросите новую ссылку или зарегистрируйтесь снова.');
                } else if (errorMsg.includes("Недействительный токен") || (err.response?.status === 401 && errorMsg.toLowerCase().includes("invalid"))) {
                    setMessage('Недействительная ссылка для подтверждения. Возможно, email уже подтвержден или ссылка некорректна.');
                } else {
                    setMessage(`Ошибка: ${errorMsg}`); // Показываем общую ошибку
                }
                console.error("Verification error:", err.response || err);
            }
        };

        verify();
    }, [token, navigate]); // Зависимости useEffect

    return (
        <div>
            <h2>Статус подтверждения Email</h2>
            {/* Сообщения уже на русском */}
            {verificationStatus === 'pending' && <p>{message}</p>}
            {verificationStatus === 'success' && <p style={{ color: 'green' }}>{message}</p>}
            {verificationStatus === 'error' && <p style={{ color: 'red' }}>{message}</p>}
            {/* Можно добавить кнопку для повторной отправки или перехода на регистрацию/логин */}
        </div>
    );
}

export default VerifyEmail;
