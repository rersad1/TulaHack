import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api'; // Раскомментируем для отправки данных

function CreateRequestForm() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        assistanceType: '', // Это будет category на бэкенде
        description: '',
        preferredDateTime: '',
        address: '',
        locationType: 'OFFLINE', // 'ONLINE' или 'OFFLINE'
        agreedToTerms: false,
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleInputChange = (e) => {
        const { name, value, type, checked } = e.target;

        if (name === 'isOnlineCheckbox') {
            const newLocationType = checked ? 'ONLINE' : 'OFFLINE';
            setFormData(prevData => ({
                ...prevData,
                locationType: newLocationType,
                // Очищаем адрес, если выбрана онлайн помощь
                address: checked ? '' : prevData.address,
            }));
        } else {
            setFormData(prevData => ({
                ...prevData,
                [name]: type === 'checkbox' ? checked : value,
            }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        if (!formData.agreedToTerms) {
            setError('Необходимо согласиться с условиями.');
            setLoading(false);
            return;
        }

        const isOffline = formData.locationType === 'OFFLINE';
        // Проверяем обязательные поля
        if (!formData.assistanceType || !formData.description || (isOffline && !formData.address)) {
            setError(`Пожалуйста, заполните все обязательные поля (${isOffline ? 'тип помощи, описание, адрес' : 'тип помощи, описание'}).`);
            setLoading(false);
            return;
        }

        // Формируем данные для отправки на бэкенд (TaskDTO)
        const dataToSend = {
            title: formData.assistanceType, // Используем тип помощи как заголовок
            description: formData.description,
            category: formData.assistanceType, // assistanceType соответствует category
            locationType: formData.locationType,
            address: formData.locationType === 'OFFLINE' ? formData.address : null, // Адрес только для оффлайн
            preferredDateTime: formData.preferredDateTime || null, // Отправляем null, если дата не выбрана
            status: 'OPEN', // Новая заявка всегда OPEN
            // userEmail будет определен на бэкенде из JWT токена
        };

        console.log("Отправка заявки:", dataToSend);

        try {
          // Отправляем POST запрос на эндпоинт создания задачи
          const response = await api.post('/api/tasks', dataToSend);
          console.log("Заявка успешно отправлена:", response.data);
          // Перенаправляем пользователя на его дашборд после успеха
          navigate('/user-dashboard');
        } catch (err) {
          // Обрабатываем ошибки от API
          setError(err.response?.data?.message || err.response?.data || 'Не удалось отправить заявку. Пожалуйста, попробуйте снова.');
          console.error("Ошибка отправки заявки:", err.response || err);
        } finally {
          // В любом случае убираем индикатор загрузки
          setLoading(false);
        }

        // Убираем имитацию отправки
        // setTimeout(() => {
        //     setLoading(false);
        //     console.log("Имитация успешной отправки.");
        //     navigate('/user-dashboard');
        // }, 1000);
    };

    // ... стили остаются без изменений ...
    const pageStyle = "relative flex size-full min-h-screen flex-col bg-gray-50 group/design-root overflow-x-hidden";
    const containerStyle = "px-4 sm:px-10 md:px-20 lg:px-40 flex flex-1 justify-center py-5";
    const contentBoxStyle = "layout-content-container flex flex-col w-full max-w-2xl py-5";
    const titleStyle = "text-[#111418] text-3xl md:text-4xl font-bold leading-tight tracking-tight px-4 py-3 mb-6 text-center";
    const formContainerStyle = "bg-white border border-gray-200 rounded-xl p-6 md:p-8 shadow-lg";
    const fieldGroupStyle = "flex flex-col gap-1.5 px-4 py-3";
    const labelStyle = "text-[#111418] text-base font-medium leading-normal block";
    const inputBaseStyle = "form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#111418] focus:outline-0 focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50 border border-[#dce0e5] bg-white focus:border-[#a0a8b1] h-12 placeholder:text-[#637588] p-[15px] text-base font-normal leading-normal transition duration-150 ease-in-out";
    const selectStyle = `${inputBaseStyle} bg-[image:--select-button-svg] appearance-none`;
    const textareaStyle = `${inputBaseStyle} min-h-36 py-3`;
    const checkboxContainerStyle = "flex items-center gap-x-3 py-3 px-4";
    const checkboxStyle = "h-5 w-5 rounded border-[#dce0e5] border-2 bg-transparent text-[#1980e6] checked:bg-[#1980e6] checked:border-[#1980e6] checked:bg-[image:--checkbox-tick-svg] focus:ring-0 focus:ring-offset-0 focus:border-[#a0a8b1] focus:outline-none";
    const checkboxLabelStyle = "text-[#111418] text-base font-normal leading-normal";
    const buttonContainerStyle = "flex px-4 py-3 mt-4";
    const buttonStyle = "flex min-w-[84px] w-full cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#1980e6] text-white text-sm font-bold leading-normal tracking-[0.015em] hover:bg-[#1367b8] transition-colors disabled:opacity-50";
    const errorStyle = "text-red-600 text-sm px-4 py-2 text-center";

    const svgStyles = {
        '--checkbox-tick-svg': `url("data:image/svg+xml,%3csvg viewBox='0 0 16 16' fill='rgb(255,255,255)' xmlns='http://www.w3.org/2000/svg'%3e%3cpath d='M12.207 4.793a1 1 0 010 1.414l-5 5a1 1 0 01-1.414 0l-2-2a1 1 0 011.414-1.414L6.5 9.086l4.293-4.293a1 1 0 011.414 0z'/%3e%3c/svg%3e")`,
        '--select-button-svg': `url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' width='24px' height='24px' fill='rgb(99,117,136)' viewBox='0 0 256 256'%3e%3cpath d='M181.66,170.34a8,8,0,0,1,0,11.32l-48,48a8,8,0,0,1-11.32,0l-48-48a8,8,0,0,1,11.32-11.32L128,212.69l42.34-42.35A8,8,0,0,1,181.66,170.34Zm-96-84.68L128,43.31l42.34,42.35a8,8,0,0,0,11.32-11.32l-48-48a8,8,0,0,0-11.32,0l-48,48A8,8,0,0,0,85.66,85.66Z'/%3e%3c/svg%3e")`,
    };

    return (
        <div className={pageStyle} style={{ fontFamily: '"Plus Jakarta Sans", "Noto Sans", sans-serif', ...svgStyles }}>
            <div className="layout-container flex h-full grow flex-col">
                <div className={containerStyle}>
                    <div className={contentBoxStyle}>
                        <h1 className={titleStyle}>Заявка на помощь волонтеров</h1>
                        <form onSubmit={handleSubmit} className={formContainerStyle}>
                            {/* Поле выбора типа помощи */}
                            <div className={fieldGroupStyle}>
                                <label htmlFor="assistanceType" className={labelStyle}>Требуемая помощь*</label>
                                <select
                                    id="assistanceType"
                                    name="assistanceType" // Имя для состояния React
                                    className={selectStyle}
                                    value={formData.assistanceType}
                                    onChange={handleInputChange}
                                    required
                                >
                                    <option value="" disabled>Выберите тип помощи</option>
                                    <option value="SOCIAL">Социальная помощь (продукты, лекарства, сопровождение)</option>
                                    <option value="MEDICAL">Медицинская помощь (не экстренная)</option>
                                    <option value="HOUSEHOLD">Помощь по дому</option>
                                    <option value="TRANSPORT">Транспортная помощь</option>
                                    <option value="EDUCATION">Образование (помощь с уроками, репетиторство)</option>
                                    <option value="ECOLOGY">Экологические акции</option>
                                    <option value="DIGITAL">Компьютерная помощь</option>
                                    <option value="OTHER">Другое (укажите в описании)</option>
                                </select>
                            </div>

                            {/* Чекбокс для онлайн помощи */}
                            <div className={checkboxContainerStyle}>
                                <input
                                    type="checkbox"
                                    id="isOnlineCheckbox"
                                    name="isOnlineCheckbox" // Имя для обработчика
                                    className={checkboxStyle}
                                    checked={formData.locationType === 'ONLINE'}
                                    onChange={handleInputChange}
                                />
                                <label htmlFor="isOnlineCheckbox" className={checkboxLabelStyle}>Нужна помощь онлайн?</label>
                            </div>

                            {/* Поле адреса (только если не онлайн) */}
                            {formData.locationType === 'OFFLINE' && (
                                <div className={fieldGroupStyle}>
                                    <label htmlFor="address" className={labelStyle}>Адрес (где нужна помощь)*</label>
                                    <input
                                        type="text"
                                        id="address"
                                        name="address"
                                        placeholder="Город, улица, дом, квартира"
                                        className={inputBaseStyle}
                                        value={formData.address}
                                        onChange={handleInputChange}
                                        required={formData.locationType === 'OFFLINE'} // Обязательно только для оффлайн
                                    />
                                </div>
                            )}

                            {/* Поле описания */}
                            <div className={fieldGroupStyle}>
                                <label htmlFor="description" className={labelStyle}>Опишите необходимую помощь*</label>
                                <textarea
                                    id="description"
                                    name="description"
                                    placeholder="Предоставьте детали о задаче (что, когда, кому)"
                                    className={textareaStyle}
                                    value={formData.description}
                                    onChange={handleInputChange}
                                    required
                                ></textarea>
                            </div>

                            {/* Поле даты и времени */}
                            <div className={fieldGroupStyle}>
                                <label htmlFor="preferredDateTime" className={labelStyle}>Предпочитаемая дата и время</label>
                                <input
                                    type="datetime-local"
                                    id="preferredDateTime"
                                    name="preferredDateTime"
                                    className={inputBaseStyle}
                                    value={formData.preferredDateTime}
                                    onChange={handleInputChange}
                                />
                            </div>

                            {/* Чекбокс согласия с условиями */}
                            <div className={checkboxContainerStyle}>
                                <input
                                    type="checkbox"
                                    id="agreedToTerms"
                                    name="agreedToTerms"
                                    className={checkboxStyle}
                                    checked={formData.agreedToTerms}
                                    onChange={handleInputChange}
                                />
                                <label htmlFor="agreedToTerms" className={checkboxLabelStyle}>Я согласен с условиями использования</label>
                            </div>

                            {/* Отображение ошибки */}
                            {error && <p className={errorStyle}>{error}</p>}

                            {/* Кнопка отправки */}
                            <div className={buttonContainerStyle}>
                                <button
                                    type="submit"
                                    className={buttonStyle}
                                    disabled={loading || !formData.agreedToTerms} // Блокируем кнопку во время загрузки или если не согласен с условиями
                                >
                                    {loading ? 'Отправка...' : 'Отправить заявку'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CreateRequestForm;