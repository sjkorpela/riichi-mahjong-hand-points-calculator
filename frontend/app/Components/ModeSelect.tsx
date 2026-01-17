
interface ModeSelectProps {
    options: string[];
    activeMode: string | null;
    setMode: (mode: string | null) => void;
    canDisableActiveMode?: boolean;
}

export default function ModeSelect({options, activeMode, setMode}: ModeSelectProps) {

    return (
        <div className="flex gap-1">
            {options.map((mode: string, key: number) => {
                if (mode == activeMode && activeMode) {
                    return (
                        <button className="bg-white hover:bg-gray-300 rounded-full w-8" onClick={() => setMode(null)} key={key}>
                            <h1 className="text-xl decoration-2 text-black hover:text-green-800 font-bold">{mode}</h1>
                        </button>
                    )
                } else if (mode == activeMode) {
                    return (
                        <button className="bg-white rounded-full w-8" key={key}>
                            <h1 className="text-xl decoration-2 text-black font-bold">{mode}</h1>
                        </button>
                    )
                } else {
                    return (
                        <button className="bg-green-800 hover:bg-green-700 rounded-full w-8" onClick={() => setMode(mode)} key={key}>
                            <h1 className="text-xl decoration-2 text-white font-bold">{mode}</h1>
                        </button>
                    )
                }
            })}
        </div>
    )
}