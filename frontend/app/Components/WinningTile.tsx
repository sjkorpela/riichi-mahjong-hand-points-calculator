import TileButton from "@/app/Components/TileButton";
import {MouseEvent} from "react";
import EmptyTile from "@/app/Components/EmptyTile";

interface WinningTileProps {
    face?: string | null,
    removeTileFromWinning: (tile: string) => void;
}

export default function WinningTile({face, removeTileFromWinning}: WinningTileProps) {

    if (!face) {
        return (
            <EmptyTile big={true} />
        )
    }

    return (
        <TileButton
            face={face}
            whenClicked={(e: MouseEvent) => {
                e.stopPropagation()
                removeTileFromWinning(face);
            }}
            big={true}
        />
    )


}