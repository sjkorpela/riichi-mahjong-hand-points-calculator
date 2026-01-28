import TileButton from "@/app/Components/TileButton";
import {ResponseYaku} from "@/app/Fetcher";
import EmptyTile from "@/app/Components/EmptyTile";

interface YakuItemProps {
    yaku: ResponseYaku
}


export default function YakuItem({yaku}: YakuItemProps) {

    function nameTag() {
        if (yaku.englishName) {
            return (
                <h1 className="text-2xl text-white font-bold pb-1">
                    {yaku.englishName} | {yaku.japaneseName}: {yaku.han} Han
                </h1>
            )
        } else {
            return (
                <h1 className="text-2xl text-white font-bold pb-1">
                    {yaku.japaneseName}: {yaku.han} Han
                </h1>
            )
        }

    }

    function tiles() {
        if (yaku.tiles && yaku.tiles.length > 0) {
            return (
                <div className="flex gap-1 p-3 bg-green-400 rounded-xl max-w-fit">
                    {yaku.tiles.map((tile, i) => {
                        return <TileButton face={tile} whenClicked={() => {}} inactive={true} key={i}/>
                    })}
                    {Array.from(
                        {length: 14 - yaku.tiles.length},
                        (_, key) => <EmptyTile key={key}/>
                    )}
                </div>
            )
        }
    }

    return (
        <div className="p-3 bg-green-600 rounded-xl max-w min-w-100">
            <div>
                {nameTag()}
                <h2 className="text-1xl text-white font-bold pb-1">
                    {yaku.description}
                </h2>
            </div>
        </div>
    )
}