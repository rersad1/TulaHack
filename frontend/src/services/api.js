import axios from 'axios';

const api = axios.create({
    baseURL: '/api', // Убедитесь, что baseURL указывает на ваш API
    headers: { 'Content-Type': 'application/json' }
});

// Добавляем interceptor для добавления токена в каждый запрос
api.interceptors.request.use(
    config => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// Опционально: добавить interceptor для обработки ошибок (например, 401 для обновления токена)
// api.interceptors.response.use(...)

export default api;
