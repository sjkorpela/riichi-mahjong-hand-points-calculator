"use client";


const tileTypes = ["s1", "s2", "s3", "s4", "s5", "s5r", "s6", "s7", "s8", "s9", "m1", "m2", "m3", "m4", "m5", "m5r", "m6", "m7", "m8", "m9", "p1", "p2", "p3", "p4", "p5", "p5r", "p6", "p7", "p8", "p9", "we", "ws", "ww", "wn", "gd", "rd", "wd", ];
const maxHandSize = 14;
const maxTileCount = 4;

import {useState} from "react";
import TileButton from "@/app/Components/TileButton";
import EmptyTile from "./Components/EmptyTile";
import WindSelect from "@/app/Components/WindSelect";

export default function Home() {

    const [hand, setHand] = useState<string[]>([])
    const handFull = hand.length >= maxHandSize;

    function addTile(tile: string) {
        if (handFull) {
            return;
        }
        setHand([...hand, tile]);
    }

    function removeTile(index: number) {
        setHand(hand => hand.filter((_, i) => (i != index)))
    }

    return (
        <div className="flex flex-col min-h-screen items-center justify-center gap-3 bg-green-400">
            <div className="flex items-center justify-center mt p-3 gap-2 bg-green-600 rounded-xl">
                {hand.map((f, key) => {
                    return <TileButton
                        face={f}
                        clickFunc={() => {
                            removeTile(key)
                        }}
                        key={key}
                    />
                })}
                {Array.from(
                    {length: maxHandSize - hand.length},
                    (_, key) => <EmptyTile key={key}/>
                )}
            </div>
            <div className="flex items-start gap-3">
                <div className="grid grid-cols-10 mt p-3 gap-1 bg-green-600 rounded-xl">
                    {tileTypes.map((f, key) => {
                        return <TileButton
                            face={f}
                            clickFunc={() => addTile(f)} key={key}
                            inactive={handFull}
                        />
                    })}
                </div>
                <div className="mt p-3 bg-green-600 rounded-xl">
                    <h1 className="text-xl text-white font-bold">Round Wind</h1>
                    <WindSelect
                        selectFunc={(w: string) => console.log(w)}
                    />
                    <h1 className="text-xl text-white font-bold">Seat Wind</h1>
                    <WindSelect
                        selectFunc={(w: string) => console.log(w)}
                    />
                </div>
            </div>
        </div>
    );
}
