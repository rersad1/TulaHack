import React, { useState } from 'react';
import api from '../services/api';

export default function Register() {
    const [data, setData] = useState({ email: '', password: '', firstName: '', lastName: '', role: 'USER' });
    const [msg, setMsg] = useState('');

    const handleChange = e => setData({ ...data, [e.target.name]: e.target.value });
    const handleSubmit = async e => {
        e.preventDefault();
        try {
            await api.post('/register', data);
            setMsg('Ссылка для подтверждения отправлена');
        } catch (err) {
            setMsg(err.response?.data || 'Ошибка');
        }
    };

    return (
        <div>
            <h2>Регистрация</h2>
            <form onSubmit={handleSubmit}>
                <input name="email" placeholder="Email" onChange={handleChange} required /><br />
                <input name="password" type="password" placeholder="Пароль" onChange={handleChange} required /><br />
                <input name="firstName" placeholder="Имя" onChange={handleChange} required /><br />
                <input name="lastName" placeholder="Фамилия" onChange={handleChange} required /><br />
                <select name="role" onChange={handleChange}>
                    <option value="USER">USER</option>
                    <option value="VOLUNTEER">VOLUNTEER</option>
                </select><br />
                <button type="submit">Зарегистрироваться</button>
            </form>
            {msg && <p>{msg}</p>}
        </div>
    );
}
