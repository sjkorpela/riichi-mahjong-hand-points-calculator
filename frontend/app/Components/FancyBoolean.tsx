
interface FancyBooleanProps {
    trueOption: string,
    falseOption: string;
    bool: boolean;
    updateBool: () => void;
    flipOptions?: boolean;
}

export default function FancyBoolean({trueOption, falseOption, bool, updateBool, flipOptions}: FancyBooleanProps) {

    function chosenOption(name: string) {
        return (
            <button className="w-[80] rounded-full bg-white">
                <h1 className="select-none text-xl text-black font-bold">{name}</h1>
            </button>
        )
    }

    function otherOption(name: string) {
        return (
            <button className="w-[80] rounded-full bg-green-800 hover:bg-green-700" onClick={() => updateBool()}>
                <h1 className="select-none text-xl text-white font-bold">{name}</h1>
            </button>
        )
    }

    if (flipOptions) {
        if (bool) {
            return (
                <div className="flex gap-3 justify-center">
                    {otherOption(falseOption)}
                    <h1 className="select-none text-3xl text-white font-bold">/</h1>
                    {chosenOption(trueOption)}
                </div>
            )
        } else {
            return (
                <div className="flex gap-3 justify-center">
                    {chosenOption(falseOption)}
                    <h1 className="select-none text-3xl text-white font-bold">/</h1>
                    {otherOption(trueOption)}
                </div>
            )
        }
    } else {
        if (bool) {
            return (
                <div className="flex gap-3 justify-center">
                    {chosenOption(trueOption)}
                    <h1 className="select-none text-3xl text-white font-bold">/</h1>
                    {otherOption(falseOption)}
                </div>
            )
        } else {
            return (
                <div className="flex gap-3 justify-center">
                    {otherOption(trueOption)}
                    <h1 className="select-none text-3xl text-white font-bold">/</h1>
                    {chosenOption(falseOption)}
                </div>
            )
        }
    }
}