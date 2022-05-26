import {API_PATH} from "../common";

export function getPtrnUsage(patternId) {
    let obj = {
        method: "POST",
        mode: "cors",
        headers: {
            "Content-type": "application/json",
            "Accept": "application/json"
        },
        body: `{"ids": ["${patternId.patternId}"]}`
    }
    return fetch(API_PATH + "statistic/patterns/usage", obj)
        .then(response => response.json())
}
