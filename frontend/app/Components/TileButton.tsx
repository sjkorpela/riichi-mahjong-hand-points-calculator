import { MouseEvent } from "react";

interface TileButtonProps {
    face?: string,
    whenClicked: (event: MouseEvent) => void,
    inactive?: boolean,
    big?: boolean,
}

import "@/app/CSS/Tiles.css";
export default function TileButton({face, whenClicked, inactive, big}: TileButtonProps) {
    function tile(inactive?: boolean) {
        let base = "tile flex items-center justify-center";
        let colors = "bg-white border-b-orange-300 hover:border-b-amber-500 active:bg-neutral-300 active:border-b-orange-400";

        if (big) { base += " big"}
        if (inactive) { colors = "bg-white border-b-gray-400"; }

        return (
            <div className={`${base} ${colors}`}>
                <h1 className="select-none">{face ??= "xx"}</h1>
            </div>
        )
    }

    if (inactive) {
        return tile(inactive = true);
    } else {
        return (
            <button onClick={whenClicked} >
                {tile()}
            </button>
        )
    }
}