import TileButton from "@/app/Components/TileButton";

interface YakuItemProps {
    name: string,
    description: string,
    han: number,
    tiles?: string[],
}


export default function YakuItem({name, description, han, tiles}: YakuItemProps) {





    return (
        <div className="flex flex-col items-center">
            <h1 className="text-2xl text-white font-bold pb-1">
                {name}: {han} Han
            </h1>
            <h2 className="text-1xl text-white font-bold pb-1">
                {description}
            </h2>
            <div className="flex gap-1 p-3 bg-green-400 rounded-xl">
                {tiles?.map((tile, i) => {
                    return <TileButton face={tile} whenClicked={() => {}} inactive={true} key={i}/>
                })}
            </div>
        </div>
    )
}