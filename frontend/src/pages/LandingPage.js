import React from 'react';
import { Link } from 'react-router-dom';

function LandingPage() {
    return (
        <div className="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden" style={{ fontFamily: '"Plus Jakarta Sans", "Noto Sans", sans-serif' }}>
            <div className="layout-container flex h-full grow flex-col">
                {/* Header can be added later if needed, using App.js nav for now */}
                <div className="px-4 sm:px-10 md:px-20 lg:px-40 flex flex-1 justify-center py-5">
                    <div className="layout-content-container flex flex-col max-w-[960px] flex-1">
                        <div className="@container">
                            <div className="@[480px]:p-4">
                                <div
                                    className="flex min-h-[480px] flex-col gap-6 bg-cover bg-center bg-no-repeat @[480px]:gap-8 @[480px]:rounded-xl items-start justify-end px-4 pb-10 @[480px]:px-10"
                                    style={{ backgroundImage: 'linear-gradient(rgba(0, 0, 0, 0.1) 0%, rgba(0, 0, 0, 0.4) 100%), url("https://cdn.usegalileo.ai/sdxl10/3f2f0c3e-c1c8-4759-80cf-151e9cd7947b.png")' }}
                                >
                                    <div className="flex flex-col gap-2 text-left">
                                        <h1 className="text-white text-4xl font-black leading-tight tracking-[-0.033em] @[480px]:text-5xl @[480px]:font-black @[480px]:leading-tight @[480px]:tracking-[-0.033em]">
                                            Volunteerly - платформа №1 для волонтеров и запросов помощи
                                        </h1>
                                        <h2 className="text-white text-sm font-normal leading-normal @[480px]:text-base @[480px]:font-normal @[480px]:leading-normal">
                                            У нас широкий круг волонтеров, готовых помочь вам, и мы можем подобрать для вас наиболее подходящего волонтера.
                                        </h2>
                                    </div>
                                    <Link to="/choose-role">
                                        <button className="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 @[480px]:h-12 @[480px]:px-5 bg-[#1980e6] text-white text-sm font-bold leading-normal tracking-[0.015em] @[480px]:text-base @[480px]:font-bold @[480px]:leading-normal @[480px]:tracking-[0.015em]">
                                            <span className="truncate">Начать</span>
                                        </button>
                                    </Link>
                                </div>
                            </div>
                        </div>
                        <div className="flex flex-col gap-10 px-4 py-10 @container">
                            <h1 className="text-[#111418] tracking-light text-[32px] font-bold leading-tight @[480px]:text-4xl @[480px]:font-black @[480px]:leading-tight @[480px]:tracking-[-0.033em] max-w-[720px]">
                                Возможности Volunteerly
                            </h1>
                            <div className="grid grid-cols-[repeat(auto-fit,minmax(158px,1fr))] gap-3 p-0">
                                {/* Feature 1 */}
                                <div className="flex flex-1 gap-3 rounded-lg border border-[#dce0e5] bg-white p-4 flex-col">
                                    <div className="text-[#111418]" data-icon="MapTrifold" data-size="24px" data-weight="regular">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor" viewBox="0 0 256 256">
                                            <path d="M228.92,49.69a8,8,0,0,0-6.86-1.45L160.93,63.52,99.58,32.84a8,8,0,0,0-5.52-.6l-64,16A8,8,0,0,0,24,56V200a8,8,0,0,0,9.94,7.76l61.13-15.28,61.35,30.68A8.15,8.15,0,0,0,160,224a8,8,0,0,0,1.94-.24l64-16A8,8,0,0,0,232,200V56A8,8,0,0,0,228.92,49.69ZM104,52.94l48,24V203.06l-48-24ZM40,62.25l48-12v127.5l-48,12Zm176,131.5-48,12V78.25l48-12Z"></path>
                                        </svg>
                                    </div>
                                    <div className="flex flex-col gap-1">
                                        <h2 className="text-[#111418] text-base font-bold leading-tight">Находите волонтеров рядом</h2>
                                        <p className="text-[#637588] text-sm font-normal leading-normal">Наша платформа позволяет находить волонтеров поблизости для личной встречи.</p>
                                    </div>
                                </div>
                                {/* Feature 2 */}
                                <div className="flex flex-1 gap-3 rounded-lg border border-[#dce0e5] bg-white p-4 flex-col">
                                    <div className="text-[#111418]" data-icon="UsersThree" data-size="24px" data-weight="regular">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor" viewBox="0 0 256 256">
                                            <path d="M244.8,150.4a8,8,0,0,1-11.2-1.6A51.6,51.6,0,0,0,192,128a8,8,0,0,1-7.37-4.89,8,8,0,0,1,0-6.22A8,8,0,0,1,192,112a24,24,0,1,0-23.24-30,8,8,0,1,1-15.5-4A40,40,0,1,1,219,117.51a67.94,67.94,0,0,1,27.43,21.68A8,8,0,0,1,244.8,150.4ZM190.92,212a8,8,0,1,1-13.84,8,57,57,0,0,0-98.16,0,8,8,0,1,1-13.84-8,72.06,72.06,0,0,1,33.74-29.92,48,48,0,1,1,58.36,0A72.06,72.06,0,0,1,190.92,212ZM128,176a32,32,0,1,0-32-32A32,32,0,0,0,128,176ZM72,120a8,8,0,0,0-8-8A24,24,0,1,1,87.24,82a8,8,0,1,0,15.5-4A40,40,0,1,0,37,117.51,67.94,67.94,0,0,0,9.6,139.19a8,8,0,1,0,12.8,9.61A51.6,51.6,0,0,1,64,128,8,8,0,0,0,72,120Z"></path>
                                        </svg>
                                    </div>
                                    <div className="flex flex-col gap-1">
                                        <h2 className="text-[#111418] text-base font-bold leading-tight">Связывайтесь с волонтерами</h2>
                                        <p className="text-[#637588] text-sm font-normal leading-normal">Вы можете связаться с волонтерами через нашу платформу, и они будут рады помочь.</p>
                                    </div>
                                </div>
                                {/* Feature 3 */}
                                <div className="flex flex-1 gap-3 rounded-lg border border-[#dce0e5] bg-white p-4 flex-col">
                                    <div className="text-[#111418]" data-icon="ChatCircle" data-size="24px" data-weight="regular">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor" viewBox="0 0 256 256">
                                            <path d="M128,24A104,104,0,0,0,36.18,176.88L24.83,210.93a16,16,0,0,0,20.24,20.24l34.05-11.35A104,104,0,1,0,128,24Zm0,192a87.87,87.87,0,0,1-44.06-11.81,8,8,0,0,0-6.54-.67L40,216,52.47,178.6a8,8,0,0,0-.66-6.54A88,88,0,1,1,128,216Z"></path>
                                        </svg>
                                    </div>
                                    <div className="flex flex-col gap-1">
                                        <h2 className="text-[#111418] text-base font-bold leading-tight">Общайтесь с волонтерами</h2>
                                        <p className="text-[#637588] text-sm font-normal leading-normal">Общайтесь с волонтерами через платформу, они ответят на ваш запрос как можно скорее.</p>
                                    </div>
                                </div>
                                {/* Feature 4 */}
                                <div className="flex flex-1 gap-3 rounded-lg border border-[#dce0e5] bg-white p-4 flex-col">
                                    <div className="text-[#111418]" data-icon="WifiSlash" data-size="24px" data-weight="regular"> {/* Icon might need replacement */}
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor" viewBox="0 0 256 256">
                                            {/* Placeholder or relevant icon path */}
                                            <path d="M128,64a64,64,0,1,0,64,64A64.07,64.07,0,0,0,128,64Zm0,112a48,48,0,1,1,48-48A48.05,48.05,0,0,1,128,176Z"></path> {/* Example circle icon */}
                                        </svg>
                                    </div>
                                    <div className="flex flex-col gap-1">
                                        <h2 className="text-[#111418] text-base font-bold leading-tight">Настраивайте свой опыт</h2>
                                        <p className="text-[#637588] text-sm font-normal leading-normal">Настройте поиск, выбрав тип нужного волонтера, время и местоположение.</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default LandingPage;
