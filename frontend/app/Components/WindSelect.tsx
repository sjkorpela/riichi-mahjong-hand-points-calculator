import {useState} from "react";
import "./Tile.css";

const winds = ["we", "ws", "ww", "wn"];

interface WindSelectProps {
    selectFunc: (wind: string) => void;
}

export default function WindSelect({selectFunc}: WindSelectProps) {

    // move/connect to top layer?
    const [wind, setWind] = useState<string>();
    if (!wind) { selectWind("we"); }

    function selectWind(w: string) {
        setWind(w);
        selectFunc(w);
    }
    function tile(face: string, inactive?: boolean) {
        const base = "tile flex items-center justify-center";
        let colors = "bg-white border-b-orange-300 hover:border-b-amber-500 active:bg-neutral-300 active:border-b-orange-400";

        if (inactive) { colors = "bg-white border-b-amber-500"; }

        return (
            <div className={`${base} ${colors}`}>
                <a>{face ??= "xx"}</a>
            </div>
        )
    }

    return <div className="flex gap-1">
        {winds.map((w, key) => {
            if (w == wind) {
                return <div key={key}>
                    {tile(w, true)}
                </div>
            } else {
                return <button onClick={() => selectWind(w)} key={key}>
                    {tile(w)}
                </button>
            }
        })}
    </div>
}