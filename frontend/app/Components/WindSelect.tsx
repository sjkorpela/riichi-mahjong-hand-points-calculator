import "@/app/CSS/Tiles.css";
import {GetFaceVerbose} from "@/app/Util";
import Image from "next/image";

const winds = ["we", "ws", "ww", "wn"];

interface WindSelectProps {
    wind: string;
    updateWind: (wind: string) => void;
}

export default function WindSelect({wind, updateWind}: WindSelectProps) {

    function tile(face: string, inactive?: boolean) {
        const base = "tile flex items-center justify-center";
        let colors = "bg-white border-b-orange-300 hover:border-b-amber-500 active:bg-neutral-300 active:border-b-orange-400";

        if (inactive) { colors = "bg-white border-b-amber-500"; }

        face = face ?? "xx";
        const faceVerbose = GetFaceVerbose(face);

        return (
            <div className={`${base} ${colors}`}>
                <Image src={`/images/${face}.svg`} alt={faceVerbose} width={48} height={60} className="pointer-events-none"/>
            </div>
        )
    }

    return <div className="flex gap-1">
        {winds.map((w, key) => {
            if (w == wind) {
                return <button key={key}>
                    {tile(w, true)}
                </button>
            } else {
                return <button onClick={() => updateWind(w)} key={key}>
                    {tile(w)}
                </button>
            }
        })}
    </div>
}