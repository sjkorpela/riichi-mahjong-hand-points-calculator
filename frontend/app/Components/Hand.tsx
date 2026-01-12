import TileButton from "@/app/Components/TileButton";
import EmptyTile from "@/app/Components/EmptyTile";

interface Props {
    hand: {[index: string]: number};
    removeTileFromHand: (tile: string) => void;
    maxHandSize: number;
}

export default function hand({ hand, removeTileFromHand, maxHandSize }: Props) {

    let tiles = 0;

    return (
        <div className="flex gap-1 flex-wrap">
            {Object.keys(hand).map((f, key) => {
                return Array.from({length: hand[f]}).map((_, i) => {
                    tiles++;
                    return <TileButton
                        face={f}
                        addTile={() => {
                            removeTileFromHand(f)
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