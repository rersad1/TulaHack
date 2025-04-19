import React from 'react';
import { Link } from 'react-router-dom';

function Home() {
    return (
        <div className="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden" style={{ fontFamily: '"Plus Jakarta Sans", "Noto Sans", sans-serif' }}>
            <div className="layout-container flex h-full grow flex-col">
                <div className="px-4 sm:px-10 md:px-20 lg:px-40 flex flex-1 justify-center py-5">
                    <div className="layout-content-container flex flex-col w-full max-w-[512px] py-5">
                        <div className="flex flex-wrap justify-between gap-3 p-4">
                            <div className="flex min-w-72 flex-col gap-3">
                                <p className="text-[#111418] text-4xl font-black leading-tight tracking-[-0.033em]">Чем мы можем вам сегодня помочь?</p>
                                <p className="text-[#637588] text-base font-normal leading-normal">Выберите вариант, который лучше всего вас описывает.</p>
                            </div>
                        </div>

                        {/* Option 1: Need Help */}
                        <div className="p-4">
                            <Link to="/register?role=USER" className="block hover:bg-gray-50 rounded-xl border border-gray-200 p-4 transition-colors">
                                <div className="flex flex-col sm:flex-row items-stretch justify-between gap-4">
                                    <div className="flex flex-col gap-1 flex-[2_2_0px]">
                                        <p className="text-[#637588] text-sm font-normal leading-normal">Мне нужна помощь</p>
                                        <p className="text-[#111418] text-base font-bold leading-tight">
                                            Если вам или кому-то из ваших знакомых нужна помощь, наше сообщество здесь для вас. Мы предлагаем помощь в различных задачах и потребностях.
                                        </p>
                                        <div className="mt-4 flex gap-2">
                                            <span className="inline-block min-w-[84px] cursor-pointer rounded-xl h-10 px-4 bg-[#1980e6] text-white text-sm font-bold leading-10 tracking-[0.015em] text-center">
                                                Регистрация (Нужна помощь)
                                            </span>
                                        </div>
                                    </div>
                                    <div
                                        className="w-full sm:w-auto bg-center bg-no-repeat aspect-video bg-cover rounded-xl flex-1 min-h-[100px] sm:min-h-0"
                                        style={{ backgroundImage: 'url("https://cdn.usegalileo.ai/sdxl10/90566ccd-4af0-4599-9fa0-e0dcd76f27dd.png")' }}
                                    ></div>
                                </div>
                            </Link>
                        </div>

                        {/* Option 2: Volunteer */}
                        <div className="p-4">
                            <Link to="/register?role=VOLUNTEER" className="block hover:bg-gray-50 rounded-xl border border-gray-200 p-4 transition-colors">
                                <div className="flex flex-col sm:flex-row items-stretch justify-between gap-4">
                                    <div className="flex flex-col gap-1 flex-[2_2_0px]">
                                        <p className="text-[#637588] text-sm font-normal leading-normal">Я хочу стать волонтером</p>
                                        <p className="text-[#111418] text-base font-bold leading-tight">
                                            Волонтеры - сердце нашего сообщества. Они помогают другим, предоставляя свое время, навыки и ресурсы.
                                        </p>
                                        <div className="mt-4 flex gap-2">
                                            <span className="inline-block min-w-[84px] cursor-pointer rounded-xl h-10 px-4 bg-[#1980e6] text-white text-sm font-bold leading-10 tracking-[0.015em] text-center">
                                                Регистрация (Волонтер)
                                            </span>
                                        </div>
                                    </div>
                                    <div
                                        className="w-full sm:w-auto bg-center bg-no-repeat aspect-video bg-cover rounded-xl flex-1 min-h-[100px] sm:min-h-0"
                                        style={{ backgroundImage: 'url("https://cdn.usegalileo.ai/sdxl10/40a05b2b-e443-44f5-b0c9-71b185069937.png")' }}
                                    ></div>
                                </div>
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Home;
