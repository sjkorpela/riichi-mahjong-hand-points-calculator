import "@/app/CSS/Tiles.css";

interface EmptyTileProps {
    big?: boolean,
}

export default function EmptyTile({big}: EmptyTileProps) {
    return (
        <div className={`${big ? "big" : ""} empty tile`}></div>
    )
}