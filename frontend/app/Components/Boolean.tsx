
interface BooleanProps {
    name: string;
    bool: boolean;
    updateBool: () => void;
    blocked?: boolean;
}

export default function Boolean({name, bool, updateBool, blocked}: BooleanProps) {

    // tailwind was bugging out for some reason with ternary operators in the class name so separate returns for visuals

    if (blocked) {
        return (
            <div className="flex justify-between content-center">
                <h1 className="text-xl decoration-2 text-white font-bold line-through">{name}</h1>
                <div className={`bg-green-700 min-w-12 h-5 m-1 rounded-full`} />
            </div>
        )
    } else if (bool) {
        return (
            <div className="flex justify-between content-center">
                <h1 className="text-xl decoration-2 text-white font-bold">{name}</h1>
                <button onClick={updateBool} className={"min-w-12 h-5 flex m-1 rounded-full content-center bg-green-400 hover:bg-green-300 flex-row-reverse"}>
                    <div className="min-h-full min-w-5 bg-white rounded-full"/>
                </button>
            </div>
        )
    } else {
        return (
            <div className="flex justify-between content-center">
                <h1 className="text-xl decoration-2 text-white font-bold">{name}</h1>
                <button onClick={updateBool} className={"min-w-12 h-5 flex m-1 rounded-full content-center bg-red-400 hover:bg-red-500 flex-row"}>
                    <div className="min-h-full min-w-5 bg-white rounded-full"/>
                </button>
            </div>
        )
    }


}