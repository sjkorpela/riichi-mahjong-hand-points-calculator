import TileButton from "@/app/Components/TileButton";
import {ResponseYaku} from "@/app/Fetcher";

interface YakuItemProps {
    yaku: ResponseYaku
}


export default function YakuItem({yaku}: YakuItemProps) {

    return (
        <div className="flex justify-between p-3 bg-green-600 rounded-xl min-w-full">
            <div>
                <h1 className="text-2xl text-white font-bold pb-1">
                    {yaku.englishName} / {yaku.japaneseName}: {yaku.han} Han
                </h1>
                <h2 className="text-1xl text-white font-bold pb-1">
                    {yaku.description}
                </h2>
            </div>
            <div className="flex gap-1 p-3 bg-green-400 rounded-xl max-w-fit">
                {yaku.tiles?.map((tile, i) => {
                    return <TileButton face={tile} whenClicked={() => {}} inactive={true} key={i}/>
                })}
            </div>
        </div>
    )
}