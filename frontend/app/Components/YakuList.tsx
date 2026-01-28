import YakuItem from "@/app/Components/YakuItem";
import {YakuResponse} from "@/app/Fetcher";
import "@/app/CSS/Page.css";

interface YakuListProps {
    response: YakuResponse | null;
}

export default function YakuList({response}: YakuListProps) {

    if (!response) {return null; }
    if (!response.yaku) { return null;}



    return (
        <div className="my-box flex flex-col gap-1">
            {response.yaku.map((yakuItem, key) => (
                <YakuItem yaku={yakuItem} key={key}/>
            ))}
        </div>
    )
}