import TileButton from "@/app/Components/TileButton";
import EmptyTile from "@/app/Components/EmptyTile";
import {MouseEvent} from "react";
import WinningTile from "@/app/Components/WinningTile";

interface HandProps {
    hand: {[index: string]: number};
    removeTileFromHand: (tile: string) => void;
    maxHandSize: number;
}

export default function hand({ hand, removeTileFromHand, maxHandSize }: HandProps) {

    let tiles = 0;

    return (
        <div className="flex gap-1 flex-wrap">
            {Object.keys(hand).map((f) => {
                return Array.from({length: hand[f]}).map((_, i) => {
                    tiles++;
                    return <TileButton
                        face={f}
                        whenClicked={(e: MouseEvent) => {
                            e.stopPropagation()
                            removeTileFromHand(f);
                        }}
                        key={i}
                    />
                })
            })}
            {Array.from(
                {length: maxHandSize - tiles},
                (_, key) => <EmptyTile key={key}/>
            )}
        </div>
    )
}