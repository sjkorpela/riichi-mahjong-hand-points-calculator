

interface TileButtonProps {
    face?: string,
    clickFunc: () => void,
    inactive?: boolean,
}

import "./TileButton.css";
export default function TileButton({face, clickFunc, inactive}: TileButtonProps) {
    function tile(colors?: string) {
        const base = "tile flex items-center justify-center";
        colors = colors || "bg-white border-b-orange-300 hover:border-b-amber-500 active:bg-neutral-300 active:border-b-orange-400";
        return (
            <div className={`${base} ${colors}`}>
                <a>{face ??= "xx"}</a>
            </div>
        )
    }

    if (inactive) {
        return tile("bg-white border-b-gray-400");
    } else {
        return (
            <button className="button" onClick={clickFunc ??= () => alert("No clickFunc bruh")}>
                {tile()}
            </button>
        )
    }
}