import {API_PATH} from "../common";

export function getPtrnUsageMerged(patternId) {
    let obj = {
        method: "POST",
        mode: "cors",
        headers: {
            "Content-type": "application/json",
            "Accept": "application/json"
        },
        body: `{"ids": ["${patternId.patternId}"]}`
    }
    return fetch(API_PATH + "statistic/patterns/usage/merged", obj)
        .then(response => response.json())
}
