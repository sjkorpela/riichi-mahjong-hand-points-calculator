
export function GetFaceVerbose(face: string) {
    let faceVerbose = "";

    switch (face[0]) {
        case "s":
            faceVerbose += "Sou " + (face[2] == "r" ? "Red " : "") + face[1];
            break;
        case "m":
            faceVerbose += "Man " + (face[2] == "r" ? "Red " : "") + face[1];
            break;
        case "p":
            faceVerbose += "Pin " + (face[2] == "r" ? "Red " : "") + face[1];
            break;
        case "w":
            switch (face[1]) {
                case "e":
                    faceVerbose += "East Wind";
                    break;
                case "s":
                    faceVerbose += "South Wind";
                    break;
                case "w":
                    faceVerbose += "West Wind";
                    break;
                case "n":
                    faceVerbose += "North Wind";
                    break;
            }
            break;
        case "d":
            switch (face[1]) {
                case "g":
                    faceVerbose += "Green Dragon";
                    break;
                case "r":
                    faceVerbose += "Red Dragon";
                    break;
                case "w":
                    faceVerbose += "White Dragon";
                    break;
            }
            break;
        default:
            faceVerbose += "No Face";
    }

    return faceVerbose;
}