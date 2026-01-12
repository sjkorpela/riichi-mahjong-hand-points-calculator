
interface BooleanProps {
    name: string;
    bool: boolean;
    updateBool: () => void;
    blocked?: boolean;
}

export default function Boolean({name, bool, updateBool, blocked}: BooleanProps) {

    if (blocked) {
        return (
            <div className="flex justify-between content-center">
                <h1 className="text-l line-through decoration-2 text-white font-bold">{name}</h1>
                <div className={`bg-green-700 min-w-9 min-h-4 flex m-1 flex-row${bool? "" : "-reverse"} rounded-full content-center`} />
            </div>
        )
    } else {
        return (
            <div className="flex justify-between content-center">
                <h1 className="text-l text-white font-bold">{name}</h1>
                <button onClick={updateBool} className={`bg-${bool? "green-400" : "red-400"} min-w-9 min-h-4 flex m-1 flex-row${bool? "-reverse" : ""} rounded-full content-center`}>
                    <div className="min-h-full min-w-4 bg-white rounded-full"/>
                </button>
            </div>
        )

    }


}