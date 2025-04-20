import React, { useState, useEffect } from 'react';
import { useParams, Link, Navigate } from 'react-router-dom';
import api from '../services/api';

export default function TaskDetail() {
    const { id } = useParams();
    const [task, setTask] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        api.get(`/api/tasks/${id}`)
            .then(r => setTask(r.data))
            .catch(() => setError('Не удалось загрузить детали заявки'));
    }, [id]);

    if (error) return <p className="text-red-600">{error}</p>;
    if (!task) return <p>Загрузка...</p>;

    return (
        <div className="p-6">
            <h2 className="text-2xl font-bold mb-4">{task.title}</h2>
            <p className="mb-2"><strong>Описание:</strong> {task.description}</p>
            <p className="mb-2"><strong>Статус:</strong> {task.status}</p>
            <p className="mb-2"><strong>Категория:</strong> {task.category}</p>
            {task.assignedVolunteer ? (
                <div className="mt-4 p-4 border rounded">
                    <h3 className="text-xl font-semibold mb-2">Принял заявку</h3>
                    <p><strong>Имя:</strong> {task.assignedVolunteer.firstName} {task.assignedVolunteer.lastName}</p>
                    <p><strong>Email:</strong> {task.assignedVolunteer.email}</p>
                </div>
            ) : (
                <p className="mt-4">Задача ещё не принята волонтёром.</p>
            )}
            <Link to="/user-dashboard" className="text-blue-600 hover:underline mt-4 block">Назад</Link>
        </div>
    );
}
