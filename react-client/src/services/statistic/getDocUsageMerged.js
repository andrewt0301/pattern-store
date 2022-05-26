import {API_PATH} from "../common";

export function getDocUsageMerged(docId) {
    let obj = {
        method: "POST",
        mode: "cors",
        headers: {
            "Content-type": "application/json",
            "Accept": "application/json"
        },
        body: `{"docId": "${docId.docId}"}`
    }
    return fetch(API_PATH + "statistic/doc/usage/merged", obj)
        .then(response => response.json())
}
