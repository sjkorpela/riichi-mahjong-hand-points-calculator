
interface FetchYakuProps {
    hand: object,
    winningTile: string,
    roundWind: string,
    seatWind: string,
    dora: string[],
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

export interface ResponseYaku {
    englishName: string,
    japaneseName: string,
    description: string,
    han: number,
    tiles?: string[],
}

export interface YakuResponse {
    yaku: ResponseYaku[],
    openHand: boolean,
}

export class Fetcher {

    public static async getYaku(request: FetchYakuProps): Promise<YakuResponse> {
        console.log("Fetching with:", request)
        const raw = await fetch('http://localhost:8080/points', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(request)
        });
        const response: YakuResponse = await raw.json();
        console.log("Response: ", response);
        return response;
    }
}