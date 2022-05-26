import {API_PATH} from "../common";

export function getDocUsage(docId) {
    let obj = {
        method: "POST",
        mode: "cors",
        headers: {
            "Content-type": "application/json",
            "Accept": "application/json"
        },
        body: `{"docId": "${docId.docId}"}`
    }
    return fetch(API_PATH + "statistic/doc/usage", obj)
        .then(response => response.json())
}
