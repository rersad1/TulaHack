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
                await api.get(`/verify-email?token=${token}`);
                setVerificationStatus('success');
                setMessage('Email успешно подтвержден! Теперь вы можете войти.');
                // Опционально: перенаправить на страницу входа через несколько секунд
                setTimeout(() => {
                    navigate('/login');
                }, 3000);
            } catch (err) {
                setVerificationStatus('error');
                const errorMsg = err.response?.data || 'Ошибка подтверждения email.';
                // Уточняем сообщение об ошибке для истекшего токена
                if (errorMsg.includes("Токен устарел")) {
                    setMessage('Ссылка для подтверждения истекла. Пожалуйста, запросите новую ссылку или зарегистрируйтесь снова.');
                } else if (errorMsg.includes("Недействительный токен")) {
                    setMessage('Недействительная ссылка для подтверждения. Возможно, email уже подтвержден или ссылка некорректна.');
                }
                else {
                    setMessage(errorMsg);
                }
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
