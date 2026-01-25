import YakuItem from "@/app/Components/YakuItem";
import {YakuResponse} from "@/app/Fetcher";

interface YakuListProps {
    response: YakuResponse | null;
}

export default function YakuList({response}: YakuListProps) {

    if (!response) {return null; }



    return (
        <div>
            {response.yaku.map((yakuItem, key) => (
                <YakuItem yaku={yakuItem} key={key}/>
            ))}
        </div>
    )
}