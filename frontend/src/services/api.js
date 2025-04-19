import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080', // Ensure baseURL points to your API prefix correctly
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

export default api;
