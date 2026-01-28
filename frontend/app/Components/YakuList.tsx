import YakuItem from "@/app/Components/YakuItem";
import {YakuResponse} from "@/app/Fetcher";
import "@/app/CSS/Page.css";

interface YakuListProps {
    response: YakuResponse | null;
}

export default function YakuList({response}: YakuListProps) {

    if (!response) {return null; }
    if (!response.yaku || response.yaku.length == 0) { return null;}



    return (
        <div className="my-box flex flex-col gap-1">
            <h1 className="text-3xl text-white font-bold pb-1 text-center">Yaku</h1>
            {response.yaku.map((yakuItem, key) => (
                <YakuItem yaku={yakuItem} key={key} last={key == response.yaku.length - 1}/>
            ))}
        </div>
    )
}