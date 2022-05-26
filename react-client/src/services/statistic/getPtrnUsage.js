import {API_PATH} from "../common";

export function getPtrnUsage(pattenrnId) {
    let obj = {
        method: "POST",
        mode: "cors",
        headers: {
            "Content-type": "application/json",
            "Accept": "application/json"
        },
        body: `{"ids": ["${pattenrnId.pattenrnId}"]}`
    }
    return fetch(API_PATH + "statistic/patterns/usage", obj)
        .then(response => response.json())
}
