"use client";

const tileTypes = ["s1", "s2", "s3", "s4", "s5", "s5r", "s6", "s7", "s8", "s9", "m1", "m2", "m3", "m4", "m5", "m5r", "m6", "m7", "m8", "m9", "p1", "p2", "p3", "p4", "p5", "p5r", "p6", "p7", "p8", "p9", "we", "ws", "ww", "wn", "dg", "dr", "dw", ];
const maxHandSize = 14;
const maxDoraIndSize = 10;
const maxTileCount = 4;

import {useEffect, useState} from "react";
import Hand from "@/app/Components/Hand";
import TileButton from "@/app/Components/TileButton";
import WindSelect from "@/app/Components/WindSelect";

export default function Home() {

    // const [hand, setHand] = useState<string[]>([])
    // const handFull = hand.length >= maxHandSize;
    //
    // function addTileToHand(tile: string) {
    //     if (handFull) {
    //         return;
    //     }
    //     setHand([...hand, tile]);
    // }
    //
    // function removeTileFromHand(index: number) {
    //     setHand(hand => hand.filter((_, i) => (i != index)))
    // }

    const [hand, setHand] = useState<{[index: string]: number}>({})
    const [handSize, setHandSize] = useState<number>(0);
    const handFull = handSize >= maxHandSize;

    // const [dora, setDora] = useState<string[]>([])
    // const doraFull = dora.length >= maxDoraIndSize;

    const [roundWind, setRoundWind] = useState("we");
    const [seatWind, setSeatWind] = useState("we");

    // SCUFFED!!!!! find better solution, maybe to do this in state init
    useEffect(()=>{
        const allTiles: {[index: string]: number} = {};

        tileTypes.map((tile) => {
            allTiles[tile] = 0;
        })

        const a = () => {setHand(allTiles)};
        a();
    }, [])

    function addTileToHand(tile: string) {
        if (hand[tile] == undefined) { setHand({...hand, [tile]: 1}) }
        else if (hand[tile] < maxTileCount) { setHand({...hand, [tile]: hand[tile] + 1}) }
        setHandSize(handSize + 1);
    }

    function removeTileFromHand(tile: string) {
        setHand({...hand, [tile]: hand[tile] - 1});
        setHandSize(handSize - 1);
    }

    // function addTileToDora(tile: string) {
    //     if (doraFull) {
    //         return;
    //     }
    //     setDora([...dora, tile]);
    // }
    //
    // function removeTileFromDora(index: number) {
    //     setDora(dora => dora.filter((_, i) => (i != index)))
    // }

    // preview of points calculation params
    if (handSize == maxHandSize) {
        const trimmedHand: {[index: string]: number} = {}
        Object.keys(hand).forEach((tile) => {
            if (hand[tile] > 0) {trimmedHand[tile] = hand[tile]}
        })

        const calculationInfo = {
            hand: trimmedHand,
            roundWind: roundWind,
            seatWind: seatWind,
            // doraIndicators: dora,
            openHand: false,
            flags: {riichi: true}
        }

        console.log("calculationInfo: ", calculationInfo);
    }

    return (
        <div className="flex flex-col min-h-screen items-center justify-center gap-3 bg-green-400">
            <div className="p-3 bg-green-600 rounded-xl">
                <h1 className="text-xl text-white font-bold">Hand</h1>
                <Hand hand={hand} removeTileFromHand={removeTileFromHand} maxHandSize={maxHandSize} />
            </div>
            <div className="flex items-start gap-3">
                <div className="grid grid-cols-10 mt p-3 gap-1 bg-green-600 rounded-xl">
                    {tileTypes.map((f, key) => {
                        return <TileButton
                            face={f}
                            clickFunc={() => addTileToHand(f)} key={key}
                            inactive={handFull || hand[f] >= maxTileCount}
                        />
                    })}
                </div>
                <div className="mt p-3 bg-green-600 rounded-xl">
                    <h1 className="text-xl text-white font-bold">Round Wind</h1>
                    <WindSelect
                        wind={roundWind}
                        setWind={setRoundWind}
                    />
                    <h1 className="text-xl text-white font-bold">Seat Wind</h1>
                    <WindSelect
                        wind={seatWind}
                        setWind={setSeatWind}
                    />
                </div>
            </div>
        </div>
    );
}
