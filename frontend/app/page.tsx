"use client";

const tileTypes = ["s1", "s2", "s3", "s4", "s5", "s5r", "s6", "s7", "s8", "s9", "m1", "m2", "m3", "m4", "m5", "m5r", "m6", "m7", "m8", "m9", "p1", "p2", "p3", "p4", "p5", "p5r", "p6", "p7", "p8", "p9", "we", "ws", "ww", "wn", "dg", "dr", "dw", ];

// better way to do this??
const allTiles: {[index: string]: number} = {};
tileTypes.forEach((tile) => {
    allTiles[tile] = 0;
})

const maxHandSize = 14;
const maxDora = 10;
const maxTileCount = 4;
const red5TileCount = 1;

import {useState} from "react";
import Hand from "@/app/Components/Hand";
import TileButton from "@/app/Components/TileButton";
import WindSelect from "@/app/Components/WindSelect";
import Boolean from "@/app/Components/Boolean";
import DoraIndicators from "@/app/Components/DoraIndicators";

export default function Home() {

    // if true, tile select adds tiles to hand, if false adds to dora
    const [handFocus, setHandFocus] = useState<boolean>(true);

    const [hand, setHand] = useState<{[index: string]: number}>(allTiles)
    const [handSize, setHandSize] = useState<number>(0);
    const handFull = handSize >= maxHandSize;

    const [dora, setDora] = useState<string[]>([])
    const doraFull = dora.length >= maxDora;

    const [spentTiles, setSpentTiles] = useState<{[index: string]: number}>(allTiles)

    const [roundWind, setRoundWind] = useState("we");
    const [seatWind, setSeatWind] = useState("we");

    const [openHand, setOpenHand] = useState<boolean>(false);
    const [riichi, setRiichi] = useState<boolean>(false);
    const [ippatsu, setIppatsu] = useState<boolean>(false);
    const [doubleRiichi, setDoubleRiichi] = useState<boolean>(false);
    const [tsumo, setTsumo] = useState<boolean>(false);
    const [lastTile, setLastTile] = useState<boolean>(false);
    const [afterKan, setAfterKan] = useState<boolean>(false);

    function addTileToHand(tile: string) {
        if (hand[tile] < maxTileCount) {
            setHand({...hand, [tile]: hand[tile] + 1})
            setSpentTiles({...spentTiles, [tile]: spentTiles[tile] + 1})
            setHandSize(handSize + 1);
        }
    }

    function removeTileFromHand(tile: string) {
        setHand({...hand, [tile]: hand[tile] - 1});
        setSpentTiles({...spentTiles, [tile]: spentTiles[tile] - 1});
        setHandSize(handSize - 1);
    }

    function addTileToDora(tile: string) {
        if (hand[tile] < maxTileCount) {
            setSpentTiles({...spentTiles, [tile]: spentTiles[tile] + 1})
            setDora([...dora, tile]);
        }
    }

    function removeTileFromDora(index: number) {
        setSpentTiles({...spentTiles, [dora[index]]: spentTiles[dora[index]] - 1});
        setDora(dora => dora.filter((_, i) => (i != index)))
    }

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
            doraIndicators: dora,
            openHand: openHand,
            flags: {
                riichi: riichi,
                ippatsu: ippatsu,
                doubleRiichi: doubleRiichi,
                tsumo: tsumo,
                lastTile: lastTile,
                afterKan: afterKan,
            }
        }

        console.log("calculationInfo: ", calculationInfo);
    }

    // console.log("spentTiles: ", handSize, dora.length, spentTiles);

    return (
        <div className="flex flex-col min-h-screen items-center justify-center gap-3 bg-green-400 min-w-175">
            <div className={`p-3 bg-green-600 rounded-xl ${handFocus? "outline-green-800 outline-5" : ""}`} onClick={() => setHandFocus(true)}>
                <h1 className="text-xl text-white font-bold text-start">Hand</h1>
                <Hand hand={hand} removeTileFromHand={removeTileFromHand} maxHandSize={maxHandSize} />
            </div>
            <div className="flex flex-wrap items-start gap-3">
                <div className="flex flex-col gap-3">
                    <div className="p-3 bg-green-600 rounded-xl">
                        <h1 className="text-xl text-white font-bold">Tile Select</h1>
                        <div className="grid grid-cols-10 gap-1">
                            {tileTypes.map((face, key) => {
                                let noMoreTile: boolean
                                // normally there's four copies of a tile,
                                // but because one five of every land is a red five
                                // there can only be 3 fives and 1 red five
                                if (face[2] == 'r') { noMoreTile = spentTiles[face] >= red5TileCount; }
                                else if (face[1] == '5') { noMoreTile = spentTiles[face] >= maxTileCount - red5TileCount; }
                                else { noMoreTile = spentTiles[face] >= maxTileCount; }
                                return <TileButton
                                    face={face}
                                    addTile={() => handFocus? addTileToHand(face) : addTileToDora(face)}
                                    key={key}
                                    inactive={(handFocus && handFull) || (!handFocus && doraFull) || noMoreTile}
                                />
                            })}
                        </div>
                    </div>
                    <div className={`p-3 bg-green-600 rounded-xl ${!handFocus? "outline-green-800 outline-5" : ""}`} onClick={() => setHandFocus(false)}>
                        <h1 className="text-xl text-white font-bold">Dora</h1>
                        <DoraIndicators dora={dora} removeTileFromDora={removeTileFromDora} maxDora={maxDora}/>
                    </div>
                </div>
                <div className="flex flex-col gap-3">
                    <div className="p-3 bg-green-600 rounded-xl">
                        <h1 className="text-xl text-white font-bold">Round Wind</h1>
                        <WindSelect wind={roundWind} updateWind={setRoundWind}/>
                        <h1 className="text-xl text-white font-bold">Seat Wind</h1>
                        <WindSelect wind={seatWind} updateWind={setSeatWind}/>
                    </div>
                    <div className="p-3 bg-green-600 rounded-xl">
                        <h1 className="text-xl text-white font-bold">Extra Han</h1>
                        <Boolean name={"Open Hand"} bool={openHand} updateBool={() => setOpenHand(!openHand)}/>
                        <Boolean name={"Riichi"} bool={riichi} updateBool={() => setRiichi(!riichi)} blocked={openHand}/>
                        <Boolean name={"Ippatsu"} bool={ippatsu} updateBool={() => setIppatsu(!ippatsu)} blocked={openHand || !riichi}/>
                        <Boolean name={"Double Riichi"} bool={doubleRiichi} updateBool={() => setDoubleRiichi(!doubleRiichi)} blocked={openHand || !riichi}/>
                        <Boolean name={"Tsumo"} bool={tsumo} updateBool={() => setTsumo(!tsumo)}/>
                        <Boolean name={"Last Tile"} bool={lastTile} updateBool={() => setLastTile(!lastTile)}/>
                        <Boolean name={"After a Kan"} bool={afterKan} updateBool={() => setAfterKan(!afterKan)}/>
                    </div>
                </div>
            </div>
        </div>
    );
}
