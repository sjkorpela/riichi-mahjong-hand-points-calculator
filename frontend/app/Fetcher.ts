
interface fetchYakuProps {
    hand: object,
    winningTile: string,
    roundWind: string,
    seatWind: string,
    doraIndicators: string[],
    openHand: boolean,
    tsumo: boolean,
    flags: {
        riichi?: boolean,
        ippatsu?: boolean,
        doubleRiichi?: boolean,
        lastTile?: boolean,
        afterKan?: boolean,
        blessedHand?: boolean,
    }
}

export class Fetcher {

    public static async getYaku(request: fetchYakuProps): Promise<object> {
        const raw = await fetch('http://localhost:8080', {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(request)
        });
        const yaku = await raw.json();
        console.log("Yaku: ", yaku);
        return yaku;
    }
}