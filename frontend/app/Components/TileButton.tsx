import { MouseEvent } from "react";

interface TileButtonProps {
    face?: string,
    addTile: (event: MouseEvent) => void,
    inactive?: boolean,
}

import "@/app/CSS/Tile.css";
export default function TileButton({face, addTile, inactive}: TileButtonProps) {
    function tile(inactive?: boolean) {
        const base = "tile flex items-center justify-center";
        let colors = "bg-white border-b-orange-300 hover:border-b-amber-500 active:bg-neutral-300 active:border-b-orange-400";

        if (inactive) { colors = "bg-white border-b-gray-400"; }

        return (
            <div className={`${base} ${colors}`}>
                <a>{face ??= "xx"}</a>
            </div>
        )
    }

    if (inactive) {
        return tile(inactive = true);
    } else {
        return (
            <button onClick={addTile}>
                {tile()}
            </button>
        )
    }
}