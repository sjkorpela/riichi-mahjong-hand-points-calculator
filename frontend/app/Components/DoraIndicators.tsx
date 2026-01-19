import TileButton from "@/app/Components/TileButton";
import EmptyTile from "@/app/Components/EmptyTile";
import {MouseEvent} from "react";

interface DoraIndicatorProps {
    dora: string[];
    removeTileFromDora: (index: number) => void;
    maxDora: number;
}

export default function DoraIndicators({ dora, removeTileFromDora, maxDora }: DoraIndicatorProps) {

    return (
        <div className="flex gap-1">
            {dora.map((f, key) => {
                return <TileButton
                    face={f}
                    whenClicked={(e: MouseEvent) => {
                        // stops removing a tile from selecting the bar for adding tiles
                        e.stopPropagation()
                        removeTileFromDora(key);
                    }}
                    key={key}
                />
            })}
            {Array.from(
                {length: maxDora - dora.length},
                (_, key) => <EmptyTile key={key}/>
            )}
        </div>
    )
}