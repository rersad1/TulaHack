import React, { useState, useEffect } from 'react'
import { Link, useLocation } from 'react-router-dom'
import api from '../services/api'

function UserDashboard() {
  const location = useLocation()
  const [tasks, setTasks] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    setLoading(true)
    api.get('/api/tasks/my-tasks')
      .then(r => setTasks(r.data))
      .catch(() => setError('Не удалось загрузить ваши заявки'))
      .finally(() => setLoading(false))
  }, [])

  const cardClass = "block bg-white border border-gray-200 rounded-xl p-4 hover:bg-gray-50 transition-colors mb-4 shadow-sm"
  const buttonClass = "flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center rounded-xl h-10 px-4 bg-[#1980e6] text-white text-sm font-bold"

    // Function to get status color
    const getStatusClass = status => {
        switch(status) {
          case 'OPEN':       return 'bg-blue-100 text-blue-800'
          case 'IN_PROGRESS':return 'bg-yellow-100 text-yellow-800'
          case 'COMPLETED':  return 'bg-green-100 text-green-800'
          case 'CANCELLED':  return 'bg-red-100 text-red-800'
          default:           return 'bg-gray-100 text-gray-800'
        }
      }
    

    // Sidebar Component
    const Sidebar = () => {
        const isActive = path => location.pathname === path
        const linkBase = "flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-gray-100"
        return (
          <div className="flex flex-col w-64 bg-white p-4 border-r h-screen sticky top-0">
            <Link to="/tasks" className={`${linkBase} ${isActive('/tasks') ? 'bg-[#f0f2f4]' : ''}`}>
              📄 <span>Мои заявки</span>
            </Link>
            <Link to="/create-request" className={`${linkBase} ${isActive('/create-request') ? 'bg-[#f0f2f4]' : ''}`}>
              ➕ <span>Создать заявку</span>
            </Link>
            <Link to="/profile" className={`${linkBase} ${isActive('/profile') ? 'bg-[#f0f2f4]' : ''}`}>
              👤 <span>Профиль</span>
            </Link>
          </div>
        )
      }
    
      return (
        <div className="flex min-h-screen">
          <Sidebar/>
          <div className="flex-1 p-6 bg-gray-50">
            <h2 className="text-3xl font-bold mb-6">Мои заявки</h2>
            {loading && <p>Загрузка...</p>}
            {error && <p className="text-red-600">{error}</p>}
            {!loading && !error && (
              tasks.length
                ? tasks.map(t => (
                    <div key={t.id} className={cardClass}>
                      <div className="flex justify-between items-center">
                        <div>
                          <p className="font-bold">{t.title}</p>
                          <p className="text-sm text-gray-500">{t.category}</p>
                        </div>
                        <div className="flex items-center gap-3">
                          <span className={`px-2 py-0.5 rounded-full text-xs font-medium ${getStatusClass(t.status)}`}>
                            {t.status}
                          </span>
                          <Link to={`/task/${t.id}`} className="text-[#1980e6] hover:underline">
                            Подробнее
                          </Link>
                        </div>
                      </div>
                    </div>
                  ))
                : <p>У вас пока нет заявок.</p>
            )}
          </div>
        </div>
      )
    
    

    return (
        <div className="relative flex size-full min-h-screen flex-row bg-gray-50 group/design-root overflow-x-hidden" style={{ fontFamily: '"Plus Jakarta Sans", "Noto Sans", sans-serif' }}>
            <Sidebar />
            <div className="layout-content-container flex flex-col flex-1 overflow-y-auto">
                <div className="p-4 md:p-6 lg:p-8">

                    <h2 className="text-[#111418] text-2xl md:text-3xl font-bold leading-tight tracking-tight mb-6">Панель пользователя</h2>

                    <div className="bg-white border border-gray-200 rounded-xl p-6 mb-6 shadow-sm">
                        <h3 className="text-[#111418] text-xl font-bold mb-3">Быстрый доступ</h3>
                        <p className="text-[#637588] text-base mb-4">
                            Нужна помощь? Создайте новую заявку.
                        </p>
                        <Link to="/create-request">
                            <button className={buttonClass}>
                                <span className="truncate">Создать заявку</span>
                            </button>
                        </Link>
                    </div>

                    <div className="bg-white border border-gray-200 rounded-xl p-6 mb-6 shadow-sm">
                        <h3 className="text-[#111418] text-xl font-bold mb-4">Мои заявки</h3>
                        {myTasks.length > 0 ? (
                            myTasks.map(task => (
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
                            <p className="text-[#637588] text-base">У вас пока нет активных заявок.</p>
                        )}
                    </div>

                </div>
            </div>
        </div>
    );
}

export default UserDashboard;